package com.tradedest.factorynwaycache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> implements Cache<K, V> {

    private final LinkedHashMap<K, V> cache;

    public LruCache(int cacheSize) {
        this.cache = new LinkedHashMap<K, V>(cacheSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
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
