package com.others.kafka;

public class Producer {
    private final Broker broker;
    private final int acks;

    public Producer(Broker broker, int acks) {
        this.broker = broker;
        this.acks = acks;
    }

    public int send(String topicName, String messageValue) {
        Topic topic = broker.getTopic(topicName);
        if (topic == null) {
            throw new IllegalArgumentException("Invalid topic");
        }
        Message message = new Message(messageValue);
        int partitionId = broker.distributeMessage(topicName, message);
        Partition partition = topic.getPartitionMap().get(partitionId);
        int offset = partition.getLastOffset();

        if (acks == 0) {
            // do nothing
            System.out.println("ack == 0");
        } else if (acks == 1) {
            while (!partition.isLeaderMessageCommitted(offset)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted when sleeping");
                }
            }
            System.out.println("ack == 1");
        } else if (acks == -1) {
            while (!partition.isFollowerMessageCommitted(offset)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted when sleeping");
                }
            }
            System.out.println("ack == -1");
        } else {
            throw new IllegalArgumentException("Invalid acks");
        }
        return partitionId;
    }


}
