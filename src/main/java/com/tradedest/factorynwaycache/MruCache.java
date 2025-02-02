package com.tradedest.factorynwaycache;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class MruCache<K, V> implements Cache<K, V> {
    private final LinkedHashMap<K, V> cache;
    private final ReentrantLock lock;

    public MruCache(int cacheSize) {
        this.lock = new ReentrantLock();
        this.cache = new LinkedHashMap<K, V>(cacheSize, 0.75f, false) {
            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry eldest) {
                return size() > cacheSize;
            }
        };
    }

    @Override
    public void put(K key, V value) {
        lock.lock();
        try {
            cache.put(key, value);
        } finally {
            cache.put(key, value);
        }
    }

    @Override
    public V get(K key) {
        lock.lock();
        try {
            return cache.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void remove(K key) {
        lock.lock();
        try {
            cache.remove(key);
        } finally {
            lock.unlock();
        }
    }
}
