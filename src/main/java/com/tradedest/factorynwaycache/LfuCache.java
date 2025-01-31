package com.tradedest.factorynwaycache;

import java.util.*;

public class LfuCache<K, V> implements Cache<K, V> {
    private int cacheSize;
    private final Map<K, V> cache;
    private final Map<K, Integer> frequency;
    private final Map<Integer, LinkedHashSet<K>> freqMap;
    private int minFreq;

    public LfuCache(int cacheSize) {
        this.cacheSize = cacheSize;
        this.cache = new HashMap<>(cacheSize, 0.75f);
        frequency = new HashMap<>(cacheSize, 0.75f);
        freqMap = new HashMap<>();
        minFreq = 0;
    }

    @Override
    public void put(K key, V value) {
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
    }

    @Override
    public V get(K key) {
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
    }

    private void removeMinFreqElement() {
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
    }

    @Override
    public void remove(K key) {
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
    }
}
