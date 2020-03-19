package edu.saddleback.cs4b.Backend.PubSub;

public class UIDisplayData implements Receivable {
    private Receivable type;
    private Object data;
    private String destination;

    public UIDisplayData(Receivable type, Object data, String dst) {
        this.type = type;
        this.data = data;
        this.destination = dst;
    }

    public UIDisplayData(Receivable type, Object data) {
        this(type, data, "");
    }

    @Override
    public String getType() {
        return type.getType();
    }
    public Object getData() { return data; }
}
