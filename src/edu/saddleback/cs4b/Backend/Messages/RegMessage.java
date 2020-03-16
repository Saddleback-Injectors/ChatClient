package edu.saddleback.cs4b.Backend.Messages;

import java.io.Serializable;
import java.util.List;

public class RegMessage extends BaseMessage
{
    private String userName;
    private List<String> channels;

    public RegMessage(String userName, List<String> channels)
    {
        super("Reg-Msg");
        this.userName = userName;
        this.channels = channels;
    }

    @Override
    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getUserName()
    {
        return userName;
    }

    public List<String> getChannels()
    {
        return channels;
    }

    @Override
    public String toString()
    {
        return "Reg-Message";
    }
}
