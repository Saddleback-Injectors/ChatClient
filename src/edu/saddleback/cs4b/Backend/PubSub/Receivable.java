package edu.saddleback.cs4b.Backend.PubSub;

import edu.saddleback.cs4b.Backend.Enums.MessageType;
import edu.saddleback.cs4b.Backend.Enums.ReceiveTypes;

public interface Receivable {
    ReceiveTypes getType();
}
