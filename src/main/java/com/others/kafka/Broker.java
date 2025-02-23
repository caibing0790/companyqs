package com.others.kafka;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Broker {
    private final Map<String, Topic> topics;
    private boolean isActive;

    public Broker() {
        this.topics = new ConcurrentHashMap<>();
        this.isActive = true;
    }

    public void addTopic(Topic topic) {
        topics.put(topic.getTopicName(), topic);
    }

    public Topic getTopic(String topicName) {
        return topics.get(topicName);
    }

    public int distributeMessage(String topicName, Message message) {
        if (!topics.containsKey(topicName)) {
            throw new IllegalArgumentException("Invalid topic");
        }
        Topic topic = topics.get(topicName);
        int partitionId = message.hashCode() % topic.getPartitionCount();
        topic.sendMessage(partitionId, message);
        return partitionId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
