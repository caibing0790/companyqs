package com.others.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaSystem {
    private final List<Broker> brokers;
    private final Map<String, Topic> topics;
    private int currentBrokerId;

    public KafkaSystem() {
        this.brokers = new ArrayList<>();
        topics = new ConcurrentHashMap<>();
        currentBrokerId = 0;
    }

    public void createTopic(String topicName, int partitionNum, int replicationFactor, int retentionTime) {
        if (partitionNum < 0 || replicationFactor < 0) {
            throw new IllegalArgumentException("partitionNum and replicationFactor must be non-negative");
        }
        Topic topic = new Topic(topicName, replicationFactor, partitionNum, retentionTime);
        topics.put(topicName, topic);
        for (Broker broker : brokers) {
            broker.addTopic(topic);
        }
    }

    public void addBroker(Broker broker) {
        brokers.add(broker);
    }

    public Topic getTopic(String topicName) {
        return topics.get(topicName);
    }

    public Broker getActiveBroker() {
        for (int i = 0; i < brokers.size(); i++) {
            Broker activeBroker = brokers.get(currentBrokerId);
            if (activeBroker.isActive()) {
                return activeBroker;
            } else {
                currentBrokerId = (currentBrokerId + 1) % brokers.size();
            }
        }
        throw new IllegalStateException("No active brokers available");
    }

    public void handleBrokerFailure(Broker failedBroker) {
        failedBroker.setActive(false);
        System.out.println("Broker failure detected. Handling broker failure...");
    }
}
