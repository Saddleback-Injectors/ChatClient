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

public class Client implements UIObserver
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

    private void startUp()
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

    @Override
    public void update(Sendable data) {
        String type = data.getType();
        if (type.equals(SendTypes.PORT_NUMBER.getType())) {
            UIFields ui = (UIFields)data;
            String strPort = (String)ui.getValue();
            port = Integer.parseInt(strPort);
        } else if (type.equals(SendTypes.SERVER.getType())) {
            UIFields ui = (UIFields)data;
            host = (String)ui.getValue();
        } else if (data.getType().equals("connect")) {
            startUp();
        }
    }

}//END public class Client {