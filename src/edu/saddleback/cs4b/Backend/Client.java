package edu.saddleback.cs4b.Backend;

import edu.saddleback.cs4b.Backend.Messages.DisconnectMessage;
import edu.saddleback.cs4b.Backend.Messages.UpdateMessage;
import edu.saddleback.cs4b.Backend.PubSub.ClientObserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client{
    private String name;
    private Socket socket;
    private  ChatSender sender;
    private  ChatListener listener;
    private  List<String> channels = new ArrayList<>();
    private  ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private Thread listenThread;


    //start listener thread in client constructor

    /* Constructors */
    public Client() {
        this(null, null);
    }

    public Client(String newName, Socket newSocket) {
        name = newName;
        socket = newSocket;
        sender = new ChatSender();

        try
        {
            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            sender = new ChatSender(newName, out, null);
            listener = new ChatListener(ArrayList<ClientObserver> newObservers, in);
            this.listenThread = new Thread(listener);
            listenThread.start();
        }
        catch(IOException ex)
        {
            System.out.println("Invalid IP address or portNumber");
        }

    }

    public Client(String newName, String ipAddress, int portNum) {
        try {
            setSocket(ipAddress, portNum);
            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            sender = new ChatSender(newName, out, null);
            listener = new ChatListener(ArrayList<ClientObserver> newObservers, in);
            this.listenThread = new Thread(listener);
            listenThread.start();
        } catch (IOException ex) {
            //Invalid IPAddress
            System.out.println("Invalid IP address or portNumber");
            socket = null;
        } finally {
            name = newName;

            channels = new ArrayList<>();
        }
    }//END public Client(String ipAddress, int portNum) {




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