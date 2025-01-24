package com.speechify.LRUCache;

public class Node<T> {
    String key;
    T val;
    Node<T> next;
    Node<T> pre;

    public Node(String key, T val) {
        this.key = key;
        this.val = val;
    }

    public Node() {
    }
}
