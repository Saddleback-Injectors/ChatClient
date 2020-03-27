package edu.saddleback.cs4b.Backend.Messages;

import edu.saddleback.cs4b.Backend.Interfaces.Requestable;

public class RequestMessage extends BaseMessage {
    private RequestType type;
    private String channel;
    private Requestable requestable;
    private Runnable requester;

    private RequestMessage(String sender, String type,
                           RequestType requestType,
                           String channel, Requestable requestable) {
        super(sender, type);
        this.type = requestType;
        this.channel = channel;
        this.requestable = requestable;
        this.requester = null;
    }

    public RequestMessage(RequestType requestType, String channel) {
        this("", requestType.getType(), requestType, channel, null);
    }

    public RequestMessage(String sender, RequestType requestType, String channel) {
        this(sender, requestType.getType(), requestType, channel, null);
    }

    public RequestType getRequestType() { return type; }
    public String getChannel() { return channel; }
    public Requestable getRequestable() { return requestable; }
    public Runnable getRequester() { return requester; }

    public void setRequestable(Requestable requestable) {
        this.requestable = requestable;
    }
    public void setRequester(Runnable requester) { this.requester = requester; }
}
