package edu.saddleback.cs4b.Backend.Messages;

import java.io.Serializable;

public class DisconnectMessage extends BaseMessage
{
    private Runnable client;
    public DisconnectMessage(String sender, Runnable client)
    {
        super(sender, "Dis-Msg");
        this.client = client;
    }
    public DisconnectMessage(String sender)
    {
        super(sender, "Dis-Msg");
    }

    public Runnable getClient()
    {
        return client;
    }
}
