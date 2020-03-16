package edu.saddleback.cs4b.Backend.Messages;

import java.io.Serializable;

public abstract class BaseMessage implements Serializable
{
    protected String sender;
    private String type;

    public BaseMessage(String type)
    {
        this.type = type;
    }

    public abstract void setSender(String sender);

    public String getType()
    {
        return type;
    }

}
