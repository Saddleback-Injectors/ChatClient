package edu.saddleback.cs4b.Backend.PubSub;

public interface ClientObserver {
    void update(Receivable data);
}
