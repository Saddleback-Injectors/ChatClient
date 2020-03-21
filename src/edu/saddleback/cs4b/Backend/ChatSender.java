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
    public void update(Sendable data) {
        String type = data.getType();
        if (type.equals(SendTypes.MESSAGE.getType()))
        {
            UIFields messageField = (UIFields)data;
            if (messageField.getValue() instanceof TextMessage)
            {
                TextMessage message = (TextMessage) messageField.getValue();
                focusedChannel = message.getChannel();
                sendMessage(message);
            }
            else if (messageField.getValue() instanceof PicMessage)
            {
                sendPicture((PicMessage)messageField.getValue());
            }
        }
        else if (type.equals(SendTypes.JOIN.getType()))
        {
            UIFields message = (UIFields)data;
            RegMessage reg = (RegMessage)message.getValue();
            register(reg);
        }
        else if (type.equals(SendTypes.CHANNEL.getType()))
        {
            UIFields message = (UIFields)data;
            UpdateMessage um = (UpdateMessage)message.getValue();
            updateChannels(um);
        }
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

    private void sendMessage(TextMessage textMessage)
    {
        try
        {
            out.writeObject(new Packet(MessageType.TEXT.getType(), textMessage););
            out.flush();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
