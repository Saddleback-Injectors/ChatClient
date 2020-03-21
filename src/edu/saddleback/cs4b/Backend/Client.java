package edu.saddleback.cs4b.Backend;

import edu.saddleback.cs4b.Backend.Enums.SendTypes;
import edu.saddleback.cs4b.Backend.Messages.RegMessage;
import edu.saddleback.cs4b.Backend.PubSub.Sendable;
import edu.saddleback.cs4b.Backend.PubSub.UIFields;
import edu.saddleback.cs4b.Backend.PubSub.UIObserver;
import edu.saddleback.cs4b.UI.ClientChatController;
import javafx.scene.Scene;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client
{
    private ChatSender sender;
    private ChatListener listener;
    private Socket socket;
    private Thread listenThread;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ClientChatController controller;
    private String host;
    private int port;
    private boolean invalidCredentials;



    /* Constructors */
    public Client() {
        this(null, null, null);
    }
    public Client(String newName, Socket newSocket, ClientChatController controller) {
        socket = newSocket;
        sender = new ChatSender();

        try
        {
            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            sender = null;
            listener = null;
            listenThread.start();
        }
        catch(IOException ex)
        {
            System.out.println("Invalid IP address or portNumber");
        }

    }
    public Client(ClientChatController controller) {
        this.controller = controller;
        this.host = "";
        this.port = 0;
        this.invalidCredentials = false;
        controller.registerObserver(this);
    }


    private void createSocket()
    {
        try
        {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.flush();
            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        }
        catch(UnknownHostException unknown)
        {
            invalidCredentials = true;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private startUp()
    {
        createSocket();
        if(!invalidCredentials)
        {
            sender = new ChatSender(out, controller);
            listener = new ChatListener(in, controller);
            listenThread = new Thread(listener);
            listenThread.start();
        }
        else
        {
            invalidCredentials = false;
            host = "";
            port = 0;
        }
    }















    public void sendMessages(String channel) throws Exception
    {
        if(channel.contains(channel))
        {
            sender.sendMessages(out, name, channel);
        }
        else
        {
            throw new Exception("Not subscribed to this channel");
        }
    }

    public void addChannel(String newChannel)
    {
        /*
         * error check against server's list of channels to see if it's valid?
         */

        if(channels.contains(newChannel))
        {
            System.out.println("Already subscribed to \"" + newChannel + "\"");
            return;
        }
        channels.add(newChannel);
        sender.setChannels(channels);

        //connect message and update message
        sender.connectMessage(out, newChannel);
        try
        {
            out.writeObject(new UpdateMessage(name, "Adding-Channel", channels));
        }
        catch(IOException ex)
        {

        }
    }

    public void removeChannel(String newChannel)
    {
        if(!channels.contains(newChannel))
        {
            System.out.println("Not subscribed to this Channel");
        }


        //disconnectMessage and updateMessage
        try
        {
            out.writeObject(new DisconnectMessage(name, this));
            out.writeObject(new UpdateMessage(name, "Removing-Channel", channels));
        }
        catch(IOException ex)
        {

        }

    }

    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
    }
    public void setSocket(String ipAddress, int portNum) throws IOException {
        this.socket = new Socket(ipAddress, portNum);
    }

    public void promptAndSetSocket(Scanner scanner) {
        boolean inValidSocket = true;

        do//START do... while(inValidSocket);
        {
            System.out.print("What IP Address/Host Name shall we use?: ");
            String ipAddress = scanner.next();

            System.out.print("Which port number shall we use?: ");
            int portNumber = scanner.nextInt();

            try {
                setSocket(ipAddress, portNumber);
            } catch (IOException ex) {
                System.out.println("Sorry, invalid address or port");
                socket = null;
            }

            if (socket == null) {
                inValidSocket = true;
            } else {
                inValidSocket = false;
            }

        } while (inValidSocket);
    }

    //have the text output box in the GUI listen to this method
    /*
    public void displayMessage(BaseMessage message) throws Exception
    {
        //Filtering Messages
        if(message instanceof TextMessage)
        {
            //Display text to screen
            (textMessage)message.getMessage();
        }
        else if(message instanceof PicMessage)
        {
            //Display picture
            (PicMessage)message.getMessage();
        }
        else if(message instanceof DisconnectMessage)
        {
            //Display
            System.out.println(sender + " has left the chat room");
        }
        else if(message instanceof RegMessage)
        {
            //Display
            System.out.println(sender + " has now joined the chat room");
        }
        else if(message instanceof UpdateMessage)
        {
            //???What does this type of message do?
        }
        else
        {
            throw new Exception("Invalid Message Type");
        }
    }
    */

}//END public class Client {