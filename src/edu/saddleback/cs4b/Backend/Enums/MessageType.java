package edu.saddleback.cs4b.Backend.Enums;

public enum MessageType {
    TEXT("Text"),
    REGISTRATION("Registration"),
    IMAGE("Image"),
    UPDATE("Update"),
    DISCONNECT("Disconnection");

    private String type;
    private MessageType(String type) {
        this.type = type;
    }

    public String getType() { return type; }
}
