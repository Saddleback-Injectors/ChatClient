package edu.saddleback.cs4b.Backend.Messages;

import java.awt.image.BufferedImage;

public class PicMessage extends BaseMessage
{
    byte[] img;

    public PicMessage(String sender, byte[] img)
    {
        super(sender, "Pic-Msg");
        this.img = img;
    }

    public byte[] getImg()
    {
        return img;
    }
}
