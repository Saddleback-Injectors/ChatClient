package edu.saddleback.cs4b.Backend.Enums;

public enum UserDisplayType {
    TEXT_AREA("text-area"),
    HOST("host");

    private String type;
    private UserDisplayType(String type) {
        this.type = type;
    }
    public String getType() { return type; }
}
