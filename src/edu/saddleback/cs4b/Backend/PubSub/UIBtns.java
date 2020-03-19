package edu.saddleback.cs4b.Backend.PubSub;

import edu.saddleback.cs4b.Backend.Enums.SendTypes;

public class UIBtns implements Sendable {
    private Sendable type;

    public UIBtns(Sendable type) {
        this.type = type;
    }
    @Override
    public String getType() { return type.getType(); }
}
