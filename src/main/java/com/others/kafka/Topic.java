package com.others.kafka;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Topic {
    private final String topicName;
    private final int replicationFactor;
    private final int partitionCount;
    private final int retentionTime;
    private final Map<Integer, Partition> partitionMap;

    public Topic(String topicName, int replicationFactor, int partitionCount, int retentionTime) {
        this.topicName = topicName;
        this.replicationFactor = replicationFactor;
        this.partitionCount = partitionCount;
        this.retentionTime = retentionTime;
        partitionMap = new ConcurrentHashMap<>();
        for (int i = 0; i < partitionCount; i++) {
            partitionMap.put(i, new Partition(topicName, i, replicationFactor));
        }
    }

    public void sendMessage(int partitionId, Message message) {
        if (!partitionMap.containsKey(partitionId)) {
            throw new IllegalArgumentException("Invalid partition ID");
        }
        Partition partition = partitionMap.get(partitionId);
        partition.addMessage(message);
    }

    public Message pollMessage(int partitionId, int offset) {
        if (!partitionMap.containsKey(partitionId)) {
            throw new IllegalArgumentException("Invalid partition ID");
        }
        Partition partition = partitionMap.get(partitionId);
        return partition.readMessage(offset);
    }

    public Message pollLastMessage(int partitionId) {
        Partition partition = partitionMap.get(partitionId);
        return partition.readLastMessage();
    }

    public void cleanup() {
        for (Partition partition : partitionMap.values()) {
            partition.cleanUp(retentionTime);
        }
    }

    public Map<Integer, Partition> getPartitionMap() {
        return partitionMap;
    }

    public String getTopicName() {
        return topicName;
    }

    public int getPartitionCount() {
        return partitionCount;
    }

    public int getLatestOffset(int partitionId) {
        return partitionMap.get(partitionId).getLastOffset();
    }
}
