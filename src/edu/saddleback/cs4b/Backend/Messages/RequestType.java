package edu.saddleback.cs4b.Backend.Messages;

public enum RequestType {
    HISTORY("History");

    private String type;

    private RequestType(String type) {
        this.type = type;
    }

    public String getType() { return type; }
}
