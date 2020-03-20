package edu.saddleback.cs4b.Backend;

import edu.saddleback.cs4b.Backend.Messages.BaseMessage;
import edu.saddleback.cs4b.Backend.Messages.PicMessage;
import edu.saddleback.cs4b.Backend.Messages.TextMessage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class ChatSender {

    public void sendMessages(ObjectOutputStream out, String text, byte[] picture)
    {
        BaseMessage[] messages = createNewMessage(text, picture);


        try
        {
            out.writeObject(messages);
            out.flush();
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    //listen to GUI to obtain text
    public void sendMessages(ObjectOutputStream out, String name, String channel)
    {
        BaseMessage newMessage = null;
        String text = //Get from text box;
        byte[] picture = //get from add picture button

        if(text != null)
        {
            newMessage = new TextMessage(name, channel, text);

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

        if(picture != null)
        {
            newMessage = new PicMessage(name, picture);
            // why does pic message not specify a channel?

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



    /*
    public BaseMessage[] createNewMessage(String text, byte[] picture)
    {
        [2]BaseMessage newMessages;

        //Not picture
        if(picture == null)
        {
            newMessages[0] = new
        }
    }*/

    private void packageMessage()
    {

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

    public RegMessage connectMessage(String string)
    {

    }
}
