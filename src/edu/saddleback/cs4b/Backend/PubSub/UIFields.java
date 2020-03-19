package edu.saddleback.cs4b.Backend.PubSub;

public class UIFields implements Sendable{
    private Sendable type;
    private String value;

    public UIFields(Sendable type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String getType() { return type.getType(); }
    public String getValue() { return value; }
}
