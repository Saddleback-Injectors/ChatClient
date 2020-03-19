package edu.saddleback.cs4b.Backend.Enums;

public enum UserInputType {
    SERVER("Server"),
    USERNAME("Username"),
    PORT_NUMBER("Port Number"),
    CHANNEL("Channel"),
    MESSAGE("Message");

    private String type;
    private UserInputType(String type) {
        this.type = type;
    }
    public String getType() { return type; }
}
