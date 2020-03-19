package edu.saddleback.cs4b.Backend.Enums;

public enum UserBtnType {
    JOIN("join"),
    LEAVE("leave");

    private String type;
    private UserBtnType(String type) { this.type = type; }
    public String getType()  { return type; }
}
