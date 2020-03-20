package edu.saddleback.cs4b.Backend;


import edu.saddleback.cs4b.Backend.PubSub.ClientObserver;
import edu.saddleback.cs4b.Backend.PubSub.ClientSubject;
import edu.saddleback.cs4b.Backend.PubSub.UIObserver;

import java.util.ArrayList;

//Client Subject
//Call notify when receive message
public class ChatListener implements ClientSubject {

    private ArrayList<UIObserver> observers;

    /*
     *Create a thread that is always listening for messages from the input stream
     */


    public void listenForMessages()
    {

    }

    public void updateChatMessagesBox()
    {

    }

    @Override
    public void notifyObservers()
    {
        for(int i = 0; i < observers.size(); i++)
        {
            observers.get(i).update(/*DATA?????*/);
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
