package edu.saddleback.cs4b.Backend.Messages;

import java.util.List;

public class UpdateMessage extends BaseMessage
{
    private List<String> updeatedChannels;

    public UpdateMessage(String type, List<String> updeatedChannels)
    {
        super("Update-Msg");
        this.updeatedChannels = updeatedChannels;
    }

    @Override
    public void setSender(String sender)
    {
        this.sender = sender;
    }

}
