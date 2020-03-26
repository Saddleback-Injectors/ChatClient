package edu.saddleback.cs4b.Backend.PubSub;

import edu.saddleback.cs4b.Backend.Enums.ReceiveTypes;

public class UIDisplayData implements Receivable {
    private ReceiveTypes type;
    private Object data;
    private String destination;

    public UIDisplayData(ReceiveTypes type, Object data, String dst) {
        this.type = type;
        this.data = data;
        this.destination = dst;
    }

    public UIDisplayData(ReceiveTypes type, Object data) {
        this(type, data, "");
    }

    @Override
    public String getType() {
        return type.getType();
    }
    public Object getData() { return data; }
    public String getDestination() { return destination; }
}
