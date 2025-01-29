package com.tradedest.nwaycache;

public class LfuReplacementAlgorithm<K, V> implements ReplacementAlgorithm<K, V, Long> {
    @Override
    public void onGet(CacheElement<K, V, Long> element) {
        onSet(element);
    }

    @Override
    public void onSet(CacheElement<K, V, Long> element) {
        Long metadata = element.getMetadata();
        if (metadata == null) {
            metadata = 0L;
        }
        element.setMetadata(metadata + 1L);
    }

    @Override
    public int compare(CacheElement<K, V, Long> current, CacheElement<K, V, Long> candidate) {
        return Long.compare(current.getMetadata(), candidate.getMetadata());
    }
}
