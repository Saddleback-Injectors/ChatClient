package edu.saddleback.cs4b.Backend.Messages;

import java.io.Serializable;

public class DisconnectMessage extends BaseMessage
{
    public DisconnectMessage(String sender)
    {
        super(sender, "Dis-Msg");
    }
}
