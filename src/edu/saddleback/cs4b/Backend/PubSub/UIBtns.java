package edu.saddleback.cs4b.Backend.PubSub;

import edu.saddleback.cs4b.Backend.Enums.SendTypes;

public class UIBtns implements Sendable {
    private SendTypes type;

    public UIBtns(SendTypes type) {
        this.type = type;
    }
    @Override
    public String getType() { return type.getType(); }
}
