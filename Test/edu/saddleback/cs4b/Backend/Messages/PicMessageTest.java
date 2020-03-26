package edu.saddleback.cs4b.Backend.Messages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PicMessageTest
{

    @Test
    @DisplayName("Test Type for Pic-Msg")
    void testTypeForPicMsg()
    {
        byte[] test = new byte[6];

        PicMessage ms = new PicMessage("Test", test, "Channel");
        assertEquals("Pic-Msg", ms.getType());
    }

    @Test
    @DisplayName("Test Sender for Pic-Msg")
    void testSenderForPicMsg()
    {
        byte[] test = new byte[6];

        PicMessage ms = new PicMessage("Test", test, "Channel");
        assertEquals("Test", ms.getSender());
    }

    @Test
    @DisplayName("Test img for Pic-Msg")
    void testImgForPicMsg()
    {
        byte[] test = new byte[6];

        PicMessage ms = new PicMessage("Test", test, "Channel");
        assertEquals(test, ms.getImg());
    }

    @Test
    @DisplayName("Test Channel for Pic-Msg")
    void testChannelForPicMsg()
    {
        byte[] test = new byte[6];

        PicMessage ms = new PicMessage("Test", test, "Channel");
        assertEquals("Channel", ms.getChannel());
    }
}