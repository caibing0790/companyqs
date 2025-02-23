package com.others.kafka;

import java.util.ArrayList;
import java.util.List;

public class Partition {
    private final String topic;
    private final int partitionId;
    private Replica leaderReplica;
    private final List<Replica> followerReplicas;

    public Partition(String topic, int partitionId, int replicationFactor) {
        this.topic = topic;
        this.partitionId = partitionId;
        leaderReplica = new Replica();
        followerReplicas = new ArrayList<>();
        for (int i = 0; i < replicationFactor - 1; i++) {
            followerReplicas.add(new Replica());
        }
    }

    public void addMessage(Message message) {
        leaderReplica.addMessage(message);
        for (Replica replica : followerReplicas) {
            replica.addMessage(message);
        }
    }

    public Message readMessage(int offset) {
        return leaderReplica.readMessage(offset);
    }

    public Message readLastMessage() {
        return leaderReplica.readLastMessage();
    }

    public int getLastOffset() {
        return leaderReplica.getLastOffset();
    }

    public void cleanUp(long retentionTime) {
        leaderReplica.cleanUp(retentionTime);
        for (Replica replica : followerReplicas) {
            replica.cleanUp(retentionTime);
        }
    }

    public void electLeader() {
       if (!followerReplicas.isEmpty()) {
           leaderReplica = followerReplicas.get(0);
       } else {
           throw new IllegalStateException("No follower replicas available");
       }
    }

    public boolean isLeaderMessageCommitted(long offset) {
        return leaderReplica.isMessageCommitted(offset);
    }

    public boolean isFollowerMessageCommitted(long offset) {
        for (Replica replica : followerReplicas) {
            if (!replica.isMessageCommitted(offset)) {
                return false;
            }
        }
        return true;
    }
}
