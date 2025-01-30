package com.tradedest.database;

import java.util.HashMap;
import java.util.Map;

public class SimpleDatabase<K, V> {
    private final Map<K, V> db;

    public SimpleDatabase() {
        this.db = new HashMap<>();
    }

    public void create(K key, V value) {
        db.put(key, value);
    }

    public V read(K key) {
        return db.get(key);
    }

    public void update(K key, V value) {
        if (db.containsKey(key)) {
            db.put(key, value);
        }
    }

    public void delete(K key) {
        db.remove(key);
    }
}
