package com.tradedest.factorynwaycache;

import java.util.LinkedHashMap;

public class MruCache<K, V> implements Cache<K, V> {
    private final LinkedHashMap<K, V> cache;

    public MruCache(int cacheSize) {
        this.cache = new LinkedHashMap<K, V>(cacheSize, 0.75f, false) {
            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry eldest) {
                return size() > cacheSize;
            }
        };
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }
}
