package edu.saddleback.cs4b.Backend.Messages;

import java.io.Serializable;

public class DisconnectMessage extends BaseMessage
{
    public DisconnectMessage() {
        super("Dis-Msg");
    }

    @Override
    public void setSender(String sender)
    {
        this.sender = sender;
    }
}
