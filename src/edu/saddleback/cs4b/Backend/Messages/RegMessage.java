package edu.saddleback.cs4b.Backend.Messages;

import java.io.Serializable;
import java.util.List;

public class RegMessage implements Serializable
{
    String userName;
    List<String> channels;

    public RegMessage(String userName, List<String> channels)
    {
        this.userName = userName;
        this.channels = channels;
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
