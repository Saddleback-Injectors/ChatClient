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
                sendMessages(stringMessage);
            }
            else if(messageField.getValue() instanceof PicMessage)
            {
                sendPicture((PicMessage)messageField.getDestination());
            }//END else of if(messageField.getValue() instanceof String)
        }//END if(type.equals(SendTypes.MESSAGE.getType()))
        else if(type.equals(SendTypes.JOIN.getType()))
        {
            UIFields message = (UIFields)data;
            RegMessage reg = (RegMessage)message.getValue();
            register(reg)
        }
        else if(type.equals(SendTypes.CHANNEL.getType()))
        {
            UIFields message = (UIFields)data;
            UpdateMessage update = (UpdateMessage)message.getValue();
            updateChannels(update)
        }

    }






    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public void sendMessages(Serializable newMessage)
    {
        try
        {
            out.writeObject(newMessage);
            out.flush();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }



    public byte[] addImage(Scanner scanner) throws Exception
    {
        String picType;
        boolean validType = false;
        String imageLocation;
        BufferedImage image = null;

        //Getting type of file from user
        do{
            System.out.println("How would you like to add your image?");
            System.out.print("File or URL: ");
            picType = scanner.next();

            if(picType.toLowerCase() == "url" || picType.toLowerCase() == "file")
            {
                validType = true;
            }
            else
            {
                System.out.println("Sorry, Invalid Picture Location");
            }

        }while(!validType);



        //URL or File input
        if(picType.toLowerCase() == "url")
        {
            System.out.print("What is the URL of your image?: ");
            imageLocation = scanner.next();
            URL url = new URL(imageLocation);

            image = ImageIO.read(url);
        }
        else if(picType.toLowerCase() == "file")
        {
            System.out.print("What is the file location of your image?: ");
            imageLocation = scanner.next();
            File file = new File(imageLocation);
            image = ImageIO.read(file);
        }
        else
        {
            throw new Exception("Invalid File Type");
        }

        //Converting image to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        return imageInByte;
    }

    public void connectMessage(ObjectOutputStream out, String string)
    {
        BaseMessage newMessage = new RegMessage(name, name, channels);

        try
        {
            out.writeObject(newMessage);
            out.flush();
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }


}
