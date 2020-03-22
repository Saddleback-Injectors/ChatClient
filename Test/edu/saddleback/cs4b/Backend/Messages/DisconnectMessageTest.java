package edu.saddleback.cs4b.Backend.Messages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisconnectMessageTest
{
    @Test
    @DisplayName("Test Type is Dis-Msg")
    void testTypeForDisMsg()
    {
        DisconnectMessage ms = new DisconnectMessage("Test");
        assertEquals("Dis-Msg", ms.getType());
    }

    @Test
    @DisplayName("Test Sender is Dis-Msg")
    void testSenderForDisMsg()
    {
        DisconnectMessage ms = new DisconnectMessage("Test");
        assertEquals("Test", ms.getSender());
    }

}