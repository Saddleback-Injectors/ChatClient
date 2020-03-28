package edu.saddleback.cs4b.Backend.Enums;

public enum SendTypes {
    SERVER("Server"),
    USERNAME("Username"),
    PORT_NUMBER("Port Number"),
    CHANNEL("Channel"),
    MESSAGE("Message"),
    JOIN("join"),
    LEAVE("leave"),
    HISTORY_REQUEST("history request");

    private String type;
    private SendTypes(String type) {
        this.type = type;
    }
    public String getType() { return type; }
}
