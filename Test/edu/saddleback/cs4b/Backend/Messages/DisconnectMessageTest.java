package edu.saddleback.cs4b.Backend.Messages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisconnectMessageTest
{
    @Test
    @DisplayName("Test Type is Dis-Msg")
    void testTypeisDisMsg()
    {
        DisconnectMessage ms = new DisconnectMessage("Test");
        assertEquals("Dis-Msg", ms.getType());
    }
}