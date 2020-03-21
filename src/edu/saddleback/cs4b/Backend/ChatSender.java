package edu.saddleback.cs4b.Backend;

/*Imports*/
import edu.saddleback.cs4b.Backend.Enums.MessageType;
import edu.saddleback.cs4b.Backend.Enums.SendTypes;
import edu.saddleback.cs4b.Backend.Messages.PicMessage;
import edu.saddleback.cs4b.Backend.Messages.RegMessage;
import edu.saddleback.cs4b.Backend.Messages.TextMessage;
import edu.saddleback.cs4b.Backend.Messages.UpdateMessage;
import edu.saddleback.cs4b.Backend.PubSub.Sendable;
import edu.saddleback.cs4b.Backend.PubSub.UIFields;
import edu.saddleback.cs4b.Backend.PubSub.UIObserver;
import edu.saddleback.cs4b.Backend.PubSub.UISubject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


//Just getting message string need to make new message object


public class ChatSender implements UIObserver {

    private ObjectOutputStream out;
    private UISubject subject;
    private String username;
    private String focusedChannel;  //current channel window opened


    /* Constructors */
    public ChatSender()
    {
        this(null, null, null, null);
    }
    public ChatSender(ObjectOutputStream newOut, UISubject newSubject)
    {
        this("User", "A", newOut, newSubject)
    }
    public ChatSender(String newUsername, String newFocusedChannel, ObjectOutputStream newOut, UISubject newSubject)
    {
        subject = newSubject;
        out = newOut;
        username = newUsername;
        focusedChannel = newFocusedChannel;
        subject.registerObserver(this);
    }

    @Override
    public void update(Sendable data)
    {
        String type = data.getType();

        if(type.equals(SendTypes.MESSAGE.getType()))
        {
            UIFields messageField = (UIFields)data;
            if(messageField.getValue() instanceof String)
            {
                String stringMessage = (String) messageField.getValue();
                focusedChannel =messageField.getDestination();
                sendMessage(stringMessage);
            }
            else if(messageField.getValue() instanceof PicMessage)
            {
                sendPicture((PicMessage)messageField.getValue());
            }//END else of if(messageField.getValue() instanceof String)
        }//END if(type.equals(SendTypes.MESSAGE.getType()))
        else if(type.equals(SendTypes.JOIN.getType()))
        {
            UIFields message = (UIFields)data;
            RegMessage reg = (RegMessage)message.getValue();
            register(reg);
        }
        else if(type.equals(SendTypes.CHANNEL.getType()))
        {
            UIFields message = (UIFields)data;
            UpdateMessage update = (UpdateMessage)message.getValue();
            updateChannels(update);
        }//END else for if(type.equals(SendTypes.MESSAGE.getType()))

    }

    private void updateChannels(UpdateMessage updateMessage)
    {
        try
        {
            out.writeObject(new Packet(MessageType.IMAGE.getType(), updateMessage));
            out.flush();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    private void sendPicture(PicMessage pictureMessage)
    {
        try
        {
            out.writeObject(new Packet(MessageType.IMAGE.getType(), pictureMessage));
            out.flush();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    private void register(RegMessage regMessage)
    {
        try
        {
            out.writeObject(new Packet(MessageType.REGISTRATION.getType(), regMessage));
            out.flush();

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void sendMessage(String message)
    {
        TextMessage textMessage = new TextMessage(username, focusedChannel, message);
        Packet packet = new Packet(MessageType.TEXT.getType(), textMessage);

        try
        {
            out.writeObject(packet);
            out.flush();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
