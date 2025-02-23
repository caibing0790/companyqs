package com.others.kafka;

public class Message {
    private final String value;
    private final long timestamp;

    public Message(String value) {
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public String getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
