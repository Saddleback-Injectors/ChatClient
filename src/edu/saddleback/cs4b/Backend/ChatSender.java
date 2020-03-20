package edu.saddleback.cs4b.Backend;

import edu.saddleback.cs4b.Backend.Messages.*;
import edu.saddleback.cs4b.Backend.PubSub.Sendable;
import edu.saddleback.cs4b.Backend.PubSub.UIObserver;
import org.w3c.dom.Text;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//Just getting message string need to make new message object


public class ChatSender implements UIObserver {

    private String name = null;
    private ObjectOutputStream out = null;
    private List<String> channels = new ArrayList<>();



    public ChatSender()
    {
        this(null, null, null);
    }
    public ChatSender(String newName, ObjectOutputStream newOut, List<String> channels)
    {
        name = newName;
        out = newOut;
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

    @Override
    public void update(Sendable data) {
        Serializable newMessage = null;

        if(data instanceof TextMessage)
        {
            newMessage = new TextMessage(name, ((TextMessage) data).getChannel(), ((TextMessage) data).getMessage());

        }
        else if(data instanceof PicMessage)
        {
            newMessage = new PicMessage(name, ((PicMessage) data).getImg(), ((PicMessage) data).getChannel());
        }
        else if(data instanceof DisconnectMessage)
        {
            //Does this constructor initialize client in the disconnect message?
            //Does Disconnect Message need a client variable?
            newMessage = new DisconnectMessage(name);
        }

        sendMessages(newMessage);
    }
}
