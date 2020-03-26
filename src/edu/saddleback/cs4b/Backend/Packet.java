package edu.saddleback.cs4b.Backend;

import java.io.Serializable;

/**
 * This class will be used as a wrapper
 * to different types of messages that will
 * be sent
 */
public class Packet implements Serializable {
    private String type;
    private Serializable data;

    public Packet(String type, Serializable data) {
        setType(type);
        setData(data);
    }

    private void setType(String type) {
        this.type = type;
    }
    private void setData(Serializable data) {
        this.data = data;
    }

    public String getType() { return type; }
    public Serializable getData() { return data; }
}
