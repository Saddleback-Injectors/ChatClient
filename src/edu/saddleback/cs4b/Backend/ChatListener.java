package edu.saddleback.cs4b.Backend;


import edu.saddleback.cs4b.Backend.PubSub.ClientObserver;
import edu.saddleback.cs4b.Backend.PubSub.ClientSubject;
import edu.saddleback.cs4b.Backend.PubSub.Receivable;
import edu.saddleback.cs4b.Backend.PubSub.UIObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.SocketException;
import java.util.ArrayList;

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

    public ChatListener(ArrayList<ClientObserver> newObservers, ObjectInputStream newIn)
    {
        receivable = null;
        observers = newObservers;
        in = newIn;
    }


    /*
     *Create a thread that is always listening for messages from the input stream
     */

    @Override
    public void run() {
        boolean listening = true;

        while(listening)
        {
            try
            {
                Packet message = (Packet) in.readObject();
                Serializable data = message.getData();
                if (data instanceof TextMessage)
                {
                    receivable = new UIDisplayData(ReceiveTypes.TEXT_AREA,
                            ((TextMessage) data).getMessage(),
                            ((TextMessage) data).getChannel());
                    notifyObservers();
                }
                else if (data instanceof PicMessage)
                {
                    // figure out how to display / save the picture etc
                }
            }
            catch (SocketException socketEx)
            {
                listening = false;
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            catch (ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
        }//END while(listening)
    }

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
        observers.add(newObserver)
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
