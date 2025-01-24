package com.speechify.LRUCache;

import java.util.HashMap;
import java.util.Map;

public class LRUCacheImpl<T> implements LRUCache<T> {
    Node<T> preHead;
    Node<T> afterTail;
    int capacity;
    int size;
    Map<String, Node<T>> key2Node;

    public LRUCacheImpl(int capacity) {
        this.capacity = capacity;
        size = 0;
        preHead = new Node<T>();
        afterTail = new Node<T>();
        preHead.next = afterTail;
        afterTail.pre = preHead;
        key2Node = new HashMap<>();
    }

    @Override
    public T get(String key) {
        if (key2Node.containsKey(key)) {
            Node<T> targetNode = key2Node.get(key);
            T val = (T) targetNode.val;
            putExistNode2Head(targetNode);
            return val;
        } else {
            return null;
        }
    }

    @Override
    public void set(String key, T value) {
        if (key2Node.containsKey(key)) {
            Node<T> updateNode = updateNode(key, value);
            putExistNode2Head(updateNode);
        } else {
            addNewNode2Map(key, value);
            putHead(key);
            size++;
        }
        if (size > capacity) {
            removeLeastUseNode();
        }
    }

    private Node<T> updateNode(String key, T value) {
        Node<T> targetNode = key2Node.get(key);
        targetNode.val = value;
        return targetNode;
    }

    private void putExistNode2Head(Node<T> node) {
        Node<T> currentHead = preHead.next;

        Node<T> tempNextNode = node.next;
        Node<T> tempPreNode = node.pre;
        tempPreNode.next = tempNextNode;
        tempNextNode.pre = tempPreNode;

        preHead.next = node;
        node.pre = preHead;
        preHead.next = currentHead;
        currentHead.pre = node;
    }

    private void putHead(String key) {
        Node<T> currentHead = preHead.next;
        Node<T> newHeadNode = key2Node.get(key);

        preHead.next = newHeadNode;
        newHeadNode.pre = preHead;
        newHeadNode.next = currentHead;
        currentHead.pre = newHeadNode;
    }

    private void addNewNode2Map(String key, T value) {
        Node<T> node = new Node<>(key, value);
        key2Node.put(key, node);
    }

    private void removeLeastUseNode() {
        Node<T> leastUseNode = removeLeastUseNodeFromLink();
        removeNodeFromMap(leastUseNode);
        size--;
    }

    private Node<T> removeLeastUseNodeFromLink() {
        Node<T> leastUseNode = afterTail.pre;
        System.out.println(leastUseNode.key);
        Node<T> nextLeastNode = leastUseNode.pre;
        nextLeastNode.next = afterTail;
        afterTail.pre = nextLeastNode;
        leastUseNode.next = null;
        return leastUseNode;
    }

    private void removeNodeFromMap(Node<T> node) {
        key2Node.remove(node.key);
    }
}
