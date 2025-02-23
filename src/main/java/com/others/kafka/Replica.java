package com.others.kafka;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Replica {
    private final List<Message> messages;

    public Replica() {
        this.messages = new CopyOnWriteArrayList<>();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public Message readMessage(int offset) {
        if (offset < 0 || offset >= this.messages.size()) {
            System.out.println("Invalid offset");
            return null;
        }
        return this.messages.get(offset);
    }

    public Message readLastMessage() {
        if (this.messages.isEmpty()) {
            throw new IllegalStateException("No messages available");
        }
        return this.messages.get(this.messages.size() - 1);
    }

    public void cleanUp(long retentionTime) {
        long currentTime = System.currentTimeMillis();
        messages.removeIf(message -> currentTime - message.getTimestamp() > retentionTime);
    }

    public boolean isMessageCommitted(long offset) {
        return offset < this.messages.size();
    }

    public int getLastOffset() {
        return this.messages.size() - 1;
    }
}
