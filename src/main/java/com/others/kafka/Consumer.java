package com.others.kafka;

public class Consumer {
    public final ConsumerGroup consumerGroup;
    public final Broker broker;
    public boolean isActive;

    public Consumer(Broker broker, ConsumerGroup consumerGroup) {
        this.broker = broker;
        this.consumerGroup = consumerGroup;
        this.isActive = true;
    }

    public Message poll(String topicName, int partitionId) {
        if (!isActive) {
            throw new IllegalStateException("Consumer is deactivated");
        }

        Topic topic = broker.getTopic(topicName);
        if (topic == null) {
            throw new IllegalArgumentException("Invalid topic");
        }
        int offset = consumerGroup.getOffset(topicName, partitionId);
        if (offset > topic.getLatestOffset(partitionId)) {
            System.out.println("No more messages");
            return null;
        }
        if (consumerGroup.isOffsetConsumed(topicName, partitionId, offset)) {
            System.out.println("Offset already consumed");
            return null;
        }
        Message message = topic.pollMessage(partitionId, offset);
        if (message != null) {
            consumerGroup.markOffsetConsumed(topicName, partitionId, offset);
            consumerGroup.commitOffset(topicName, partitionId, offset + 1);
        }
        return message;
    }

    public void seek(String topicName, int partitionId, int offset) {
        if (!isActive) {
            throw new IllegalStateException("Consumer is deactivated");
        }
        Topic topic = broker.getTopic(topicName);
        if (topic == null) {
            throw new IllegalArgumentException("Invalid topic");
        }
        if (offset < 0 || offset > topic.getLatestOffset(partitionId)) {
            throw new IllegalArgumentException("Invalid offset");
        }
        if (consumerGroup.isExactlyOnce()) {
            consumerGroup.markOffsetConsumed(topicName, partitionId, offset - 1);
        }
        consumerGroup.commitOffset(topicName, partitionId, offset);
    }

    public boolean isActive() {
        return isActive;
    }

    public void deActive() {
        isActive = false;
    }
}
