package edu.saddleback.cs4b.Backend.Messages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PicMessageTest
{

    @Test
    @DisplayName("Test Type for Pic-Msg")
    void testTypeForDisMsg()
    {
        byte[] test = new byte[6];

        PicMessage ms = new PicMessage("Test", test, "Channel");
        assertEquals("Pic-Msg", ms.getType());
    }

}