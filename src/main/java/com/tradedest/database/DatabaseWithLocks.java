package com.tradedest.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class DatabaseWithLocks<K, V> extends DatabaseWithStats<K, V> {
    private Map<K, ReentrantLock> locks;

    public DatabaseWithLocks() {
        super();
        locks = new HashMap<>();
    }

    @Override
    public void create(K key, V value) {
        lock(key);
        super.create(key, value);
        unLock(key);
    }

    @Override
    public V read(K key) {
        lock(key);
        V value = super.read(key);
        unLock(key);
        return value;
    }

    @Override
    public void update(K key, V value) {
        lock(key);
        super.update(key, value);
        unLock(key);
    }

    @Override
    public void delete(K key) {
        lock(key);
        super.delete(key);
        unLock(key);
    }

    private void lock(K key) {
        locks.computeIfAbsent(key, e -> new ReentrantLock()).lock();
    }

    private void unLock(K key) {
        if (locks.containsKey(key)) {
            locks.get(key).unlock();
        }
    }
}
