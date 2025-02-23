package com.others.kafka;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConsumerGroup {
    private final String groupId;
    private final List<Consumer> consumers;
    private final Map<String, Map<Integer, Integer>> offsets;
    private boolean exactlyOnce;
    private final Map<String, Map<Integer, Integer>> consumedOffsets;
    private final Map<Integer, Consumer> partition2ConsumerMap;

    public ConsumerGroup(String groupId, boolean exactlyOnce) {
        this.groupId = groupId;
        this.consumers = new CopyOnWriteArrayList<>();
        this.offsets = new ConcurrentHashMap<>();
        this.consumedOffsets = new ConcurrentHashMap<>();
        this.partition2ConsumerMap = new ConcurrentHashMap<>();
        this.exactlyOnce = exactlyOnce;
    }

    public void addConsumer(Consumer consumer) {
        consumers.add(consumer);
    }

    public void removeConsumer(Consumer consumer) {
        consumer.deActive();
        consumers.remove(consumer);
    }

    public void commitOffset(String topicName, int partitionId, int offset) {
        offsets.computeIfAbsent(topicName, e -> new ConcurrentHashMap<>()).put(partitionId, offset);
    }

    public int getOffset(String topicName, int partitionId) {
        Map<Integer, Integer> partitionOffsets = offsets.get(topicName);
        if (partitionOffsets != null) {
            return partitionOffsets.getOrDefault(partitionId, 0);
        }
        return 0;
    }

    public boolean isExactlyOnce() {
        return exactlyOnce;
    }

    public void markOffsetConsumed(String topicName, int partitionId, int offset) {
        if (exactlyOnce) {
            consumedOffsets.computeIfAbsent(topicName, e -> new ConcurrentHashMap<>())
                    .compute(partitionId, (k, v) -> v == null ? offset : Math.max(v, offset));
        }
    }

    public boolean isOffsetConsumed(String topicName, int partitionId, int offset) {
        if (exactlyOnce) {
            Map<Integer, Integer> partitionOffsets = consumedOffsets.get(topicName);
            if (partitionOffsets != null) {
                Integer consumedOffset = partitionOffsets.get(partitionId);
                return consumedOffset != null && consumedOffset >= offset;
            }
        }
        return false;
    }

    public void setExactlyOnce(boolean exactlyOnce) {
        this.exactlyOnce = exactlyOnce;
    }
}
