package edu.saddleback.cs4b.Backend.Messages;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class PicMessage extends BaseMessage
{
    private BufferedImage img;

    public PicMessage(BufferedImage img)
    {
        super("Pic-Msg");
        this.img = img;
    }

    @Override
    public void setSender(String sender)
    {
        this.sender = sender;
    }
}
