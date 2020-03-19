package edu.saddleback.cs4b.Backend.PubSub;

public class UIDisplayData implements Receivable {
    private Receivable type;
    public UIDisplayData(Receivable type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type.getType();
    }
}
