package edu.saddleback.cs4b.Backend.PubSub;

public interface ClientSubject {
    void registerObserver(ClientObserver o);
    void removeObserver(ClientObserver o);
    void notifyObservers();
}
