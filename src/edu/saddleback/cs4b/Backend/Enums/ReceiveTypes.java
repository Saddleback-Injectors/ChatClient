package edu.saddleback.cs4b.Backend.Enums;

public enum ReceiveTypes {
    TEXT_AREA("text-area"),
    HOST("host");

    private String type;
    private ReceiveTypes(String type) {
        this.type = type;
    }
    public String getType() { return type; }
}
