package com.tradedest.factorynwaycache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LfuCache<K, V> implements Cache<K, V> {
    private final int cacheSize;
    private final ConcurrentHashMap<K, V> cache;
    private final ConcurrentHashMap<K, Integer> frequency;
    private final ConcurrentHashMap<Integer, LinkedHashSet<K>> freqMap;
    private int minFreq;
    private final ReentrantLock lock;

    public LfuCache(int cacheSize) {
        this.cacheSize = cacheSize;
        this.cache = new ConcurrentHashMap<>(cacheSize, 0.75f);
        frequency = new ConcurrentHashMap<>(cacheSize, 0.75f);
        freqMap = new ConcurrentHashMap<>();
        minFreq = 0;
        lock = new ReentrantLock();
    }

    @Override
    public void put(K key, V value) {
        lock.lock();
        try {
            if (cache.containsKey(key)) {
                cache.put(key, value);
                get(key);
                return;
            }
            if (cache.size() == cacheSize) {
                removeMinFreqElement();
            }
            cache.put(key, value);
            frequency.put(key, 1);
            freqMap.computeIfAbsent(1, e -> new LinkedHashSet<>()).add(key);
            minFreq = 1;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(K key) {
        lock.lock();
        try {
            if (cache.containsKey(key)) {
                int freq = frequency.get(key);
                frequency.put(key, freq + 1);
                freqMap.get(freq).remove(key);
                if (freq == minFreq && freqMap.get(freq).isEmpty()) {
                    minFreq++;
                }
                freqMap.computeIfAbsent(freq + 1, e -> new LinkedHashSet<>()).add(key);
                return cache.get(key);
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    private void removeMinFreqElement() {
        lock.lock();
        try {
            LinkedHashSet<K> keys = freqMap.get(minFreq);
            if (keys != null && !keys.isEmpty()) {
                K key = keys.iterator().next();
                keys.remove(key);
                if (keys.isEmpty()) {
                    freqMap.remove(minFreq);
                }
                cache.remove(key);
                frequency.remove(key);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void remove(K key) {
        lock.lock();
        try {
            if (!cache.containsKey(key)) {
                return;
            }
            int freq = frequency.get(key);
            LinkedHashSet<K> keys = freqMap.get(freq);
            keys.remove(key);
            if (keys.isEmpty() && freq == minFreq) {
                if (freqMap.isEmpty()) {
                    minFreq = 0;
                } else {
                    minFreq = Collections.min(freqMap.keySet());
                }
            }
            cache.remove(key);
            frequency.remove(key);
        } finally {
            lock.unlock();
        }
    }
}
