package edu.saddleback.cs4b.Backend.Messages;

import java.awt.image.BufferedImage;

public class PicMessage extends BaseMessage
{
    private byte[] img;
    private String channel;

    public PicMessage(String sender, byte[] img, String channel)
    {
        super(sender,"Pic-Msg");
        this.img = img;
        this.channel = channel;
    }

    public PicMessage(String sender, byte[] img)
    {
        super(sender, "Pic-Msg");
        this.img = img;
    }

    public byte[] getImg()
    {
        return img;
    }
    public String getChannel() { return channel; }
}
