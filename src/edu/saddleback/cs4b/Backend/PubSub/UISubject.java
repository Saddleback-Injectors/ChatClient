package edu.saddleback.cs4b.Backend.PubSub;

public interface UISubject {
    void registerObserver(UIObserver o);
    void removeObserver(UIObserver o);
    void notifyObservers();
}
