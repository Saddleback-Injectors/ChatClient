package edu.saddleback.cs4b.Backend.Messages;

import java.io.Serializable;

public class TextMessage extends BaseMessage
{
    private String channel;
    private String message;

    public TextMessage( String channel, String message) {
        super("txt-Msg");
        this.channel = channel;
        this.message = message;
    }

    public String getChannel()
    {
        return channel;
    }

    public String getMessage()
    {
        return message;
    }



    @Override
    public void setSender(String sender)
    {
        this.sender = sender;
    }

    @Override
    public String toString()
    {
        return "Text-Message";
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
}
