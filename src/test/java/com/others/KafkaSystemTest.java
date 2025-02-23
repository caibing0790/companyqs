package com.others;

import com.others.kafka.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KafkaSystemTest {
    private KafkaSystem kafkaSystem;
    private Producer producer;
    private ConsumerGroup consumerGroup;
    private Consumer consumer1;
    private Consumer consumer2;

    @BeforeEach
    void setUp() {
        kafkaSystem = new KafkaSystem();
        Broker broker1 = new Broker();
        Broker broker2 = new Broker();
        kafkaSystem.addBroker(broker1);
        kafkaSystem.addBroker(broker2);
        kafkaSystem.createTopic("test-topic", 2, 2, 60000); // 2 partitions, replication factor 2, 60s retention
        producer = new Producer(kafkaSystem.getActiveBroker(), 1); // acks=1
        consumerGroup = new ConsumerGroup("group1", false); // 至少消费一次
        consumer1 = new Consumer(kafkaSystem.getActiveBroker(), consumerGroup);
        consumer2 = new Consumer(kafkaSystem.getActiveBroker(), consumerGroup);
        consumerGroup.addConsumer(consumer1);
        consumerGroup.addConsumer(consumer2);
    }

    @Test
    void testSendAndPoll() {
        int p1 = producer.send("test-topic", "message1");
        int p2 = producer.send("test-topic", "message2");

        assertEquals("message1", consumer1.poll("test-topic", p1).getValue());
        assertEquals("message2", consumer2.poll("test-topic", p2).getValue());
        assertNull(consumer1.poll("test-topic", p1)); // 队列为空
    }

    @Test
    void testSeekAndReplay() {
        int p1 = producer.send("test-topic", "message1");

        assertEquals("message1", consumer1.poll("test-topic", p1).getValue());
        consumer1.seek("test-topic", p1, 0); // 重置 Offset
        assertEquals("message1", consumer1.poll("test-topic", p1).getValue()); // 重新读取
    }

    @Test
    void testRetention() {
        int p1 = producer.send("test-topic", "message1");
        Topic topic = kafkaSystem.getTopic("test-topic");
        topic.cleanup(); // 清理过期消息
        assertEquals(0, topic.getLatestOffset(p1)); // 最新 Offset
    }

    @Test
    void testBrokerFailure() {
        Broker broker = kafkaSystem.getActiveBroker();
        kafkaSystem.handleBrokerFailure(broker);

        int p1 = producer.send("test-topic", "message1");
        assertEquals("message1", consumer1.poll("test-topic", p1).getValue());
    }

    @Test
    void testConsumerRemoval() {
        int p1 = producer.send("test-topic", "message1");
        int p2 = producer.send("test-topic", "message2");
        int p3 = producer.send("test-topic", "message3");

        assertEquals("message1", consumer1.poll("test-topic", p1).getValue());
        assertEquals("message2", consumer2.poll("test-topic", p2).getValue());

        // 移除 consumer2
        consumerGroup.removeConsumer(consumer2);

        // consumer2 不能再消费消息
        assertThrows(IllegalStateException.class, () -> consumer2.poll("test-topic", p3));

        // consumer1 仍然可以消费消息
        int p4 = producer.send("test-topic", "message4");
        assertEquals("message3", consumer1.poll("test-topic", p3).getValue());
        assertEquals("message4", consumer1.poll("test-topic", p4).getValue());
    }

    @Test
    void testExactlyOnceSemantics() {
        consumerGroup.setExactlyOnce(true);
        int p1 = producer.send("test-topic", "message1");
        int p2 = producer.send("test-topic", "message2");

        // consumer1 消费消息
        assertEquals("message1", consumer1.poll("test-topic", p1).getValue());
        assertEquals("message2", consumer1.poll("test-topic", p2).getValue());

        // consumer2 尝试消费相同的消息
        assertNull(consumer1.poll("test-topic", p1)); // 消息已被消费
        assertNull(consumer1.poll("test-topic", p2)); // 消息已被消费

        // 发送新消息
        int p3 = producer.send("test-topic", "message3");
        assertEquals("message3", consumer1.poll("test-topic", p3).getValue()); // consumer2 可以消费新消息
    }

    @Test
    void testAcks() {
        Producer producerAcks0 = new Producer(kafkaSystem.getActiveBroker(), 0); // acks=0
        Producer producerAcksAll = new Producer(kafkaSystem.getActiveBroker(), -1); // acks=all

        int p1 = producerAcks0.send("test-topic", "message1");
        int p2 = producerAcksAll.send("test-topic", "message2");

        assertEquals("message1", consumer1.poll("test-topic", p1).getValue());
        assertEquals("message2", consumer2.poll("test-topic", p2).getValue());
    }

    @Test
    void testPartitionAssignment() {
        // consumer1 分配分区 0
        assertEquals(consumer1, consumerGroup.getConsumerForPartition(0));

        // consumer2 分配分区 1
        assertEquals(consumer2, consumerGroup.getConsumerForPartition(1));

        // consumer1 不能消费分区 1
        assertThrows(IllegalStateException.class, () -> consumer1.poll("test-topic", 1));

        // consumer2 不能消费分区 0
        assertThrows(IllegalStateException.class, () -> consumer2.poll("test-topic", 0));
    }
}