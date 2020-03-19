package edu.saddleback.cs4b.Backend.Messages;

import java.io.Serializable;

public abstract class BaseMessage implements Serializable
{
    private String sender;
    private String type;

    public BaseMessage(String sender, String type)
    {
        this.sender = sender;
        this.type = type;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getType()
    {
        return type;
    }

    public String getSender()
    {
        return sender;
    }
}
