package edu.saddleback.cs4b.Backend.Messages;

import java.io.Serializable;

public class TextMessage implements Serializable
{
    String Channel;
    String Message;

    public TextMessage(String channel, String message)
    {
        Channel = channel;
        Message = message;
    }

    public String getChannel()
    {
        return Channel;
    }

    public String getMessage()
    {
        return Message;
    }

    @Override
    public String toString()
    {
        return "Text-Message";
    }
}
