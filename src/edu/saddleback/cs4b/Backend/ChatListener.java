
package edu.saddleback.cs4b.Backend;

import edu.saddleback.cs4b.Backend.Enums.ReceiveTypes;
import edu.saddleback.cs4b.Backend.Messages.PicMessage;
import edu.saddleback.cs4b.Backend.Messages.TextMessage;
import edu.saddleback.cs4b.Backend.PubSub.ClientObserver;
import edu.saddleback.cs4b.Backend.PubSub.ClientSubject;
import edu.saddleback.cs4b.Backend.PubSub.Receivable;
import edu.saddleback.cs4b.Backend.PubSub.UIDisplayData;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;



//Client Subject
//Call notify when receive message
public class ChatListener implements ClientSubject, Runnable {

    private Receivable receivable;
    private ArrayList<ClientObserver> observers;
    private ObjectInputStream in;

    /* Constructors */
    public ChatListener()
    {
        this(null, null);
    }

    public ChatListener(ObjectInputStream newIn, ClientObserver controller) {
        observers = new ArrayList<>();
        registerObserver(controller);
        receivable = null;
        in = newIn;
        try
        {
            receivable = new UIDisplayData(ReceiveTypes.HOST, InetAddress.getLocalHost().getHostName(), "");
            notifyObservers();
        } catch (UnknownHostException ex)
        {
            ex.printStackTrace();
        }
    }


    @Override
    public void run() {
        boolean listening = true;
        while (listening)
        {
            try
            {
                Packet message = (Packet) in.readObject();
                Serializable data = message.getData();
                if (data instanceof TextMessage)
                {
                    receivable = new UIDisplayData(ReceiveTypes.TEXT_AREA,
                            (TextMessage)data,
                            ((TextMessage) data).getChannel());
                    notifyObservers();
                }
                else if (data instanceof PicMessage)
                {
                    receivable = new UIDisplayData(ReceiveTypes.TEXT_AREA, data, (
                            (PicMessage) data).getChannel());
                    notifyObservers();
                }
            }
            catch (SocketException socketEx)
            {
                listening = false;
            }
            catch (IOException ioEx)
            {
                ioEx.printStackTrace();
            }
            catch (ClassNotFoundException notFoundEx)
            {
                notFoundEx.printStackTrace();
            }
        }//END while (listening)
    }//end public void run()

    @Override
    public void notifyObservers()
    {
        for(int i = 0; i < observers.size(); i++)
        {
            observers.get(i).update(receivable);
        }
    }

    @Override
    public void registerObserver(ClientObserver newObserver)
    {
        observers.add(newObserver);
    }

    @Override
    public void removeObserver(ClientObserver oldObserver)
    {
        int deleteIndex = observers.indexOf(oldObserver);

        if(deleteIndex > -1)
        {
            observers.remove(oldObserver);
        }
        else
        {
            System.out.println("Observer could not be found");
        }
    }



}
