package edu.saddleback.cs4b.Backend.Messages;

import java.util.List;

public class RegMessage extends BaseMessage
{
    private String userName;
    private List<String> channels;

    public RegMessage(String sender, String userName, List<String> channels)
    {
        super(sender, "Reg-Msg");
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
