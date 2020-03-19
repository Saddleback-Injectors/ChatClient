package edu.saddleback.cs4b.Backend.PubSub;

import edu.saddleback.cs4b.Backend.MessageType;

public interface Sendable {
    MessageType getType();
}
