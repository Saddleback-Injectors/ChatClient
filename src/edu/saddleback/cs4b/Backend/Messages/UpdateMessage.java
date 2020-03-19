package edu.saddleback.cs4b.Backend.Messages;

import java.util.List;

public class UpdateMessage extends BaseMessage
{
    private List<String> updatedChannels;

    public UpdateMessage(String sender, String type, List<String> updeatedChannels)
    {
        super(sender, "Update-Msg");
        this.updatedChannels = updeatedChannels;
    }

    public List<String> getUpdatedChannels()
    {
        return updatedChannels;
    }
}
