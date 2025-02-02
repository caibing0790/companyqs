package com.tradedest.factorynwaycache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class LruCache<K, V> implements Cache<K, V> {

    private final LinkedHashMap<K, V> cache;
    private final ReentrantLock lock;

    public LruCache(int cacheSize) {
        lock = new ReentrantLock();
        this.cache = new LinkedHashMap<K, V>(cacheSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
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
            lock.unlock();
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
