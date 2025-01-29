package com.tradedest.nwaycache;

import java.time.Instant;

public class LruReplacementAlgorithm<K, V> implements ReplacementAlgorithm<K, V, Long> {
    @Override
    public void onGet(CacheElement<K, V, Long> element) {
        onSet(element);
    }

    @Override
    public void onSet(CacheElement<K, V, Long> element) {
        element.setMetadata(Instant.now().toEpochMilli());
    }

    @Override
    public int compare(CacheElement<K, V, Long> current, CacheElement<K, V, Long> candidate) {
        return Long.compare(current.getMetadata(), candidate.getMetadata());
    }
}
