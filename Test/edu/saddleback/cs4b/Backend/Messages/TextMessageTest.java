package edu.saddleback.cs4b.Backend.Messages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;

class TextMessageTest
{

    @Test
    @DisplayName("Test Type for txt-Msg")
    void testTypeForDisMsg()
    {
        TextMessage ms = new TextMessage("Test", "User", "Hello");
        assertEquals("txt-Msg", ms.getType());
    }


}