package edu.saddleback.cs4b.Backend.PubSub;

import edu.saddleback.cs4b.Backend.Enums.SendTypes;

public class UIFields implements Sendable{
    private SendTypes type;
    private Object value;
    private String destination;

    public UIFields(SendTypes type, Object value, String destination) {
        this.type = type;
        this.value = value;
        this.destination = destination;
    }

    public UIFields(SendTypes type, Object value) {
        this(type, value, "");
    }

    @Override
    public String getType() { return type.getType(); }
    public Object getValue() { return value; }
    public String getDestination() { return destination; }
}
