package com.tradedest.nwaycache;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class NWaySetAssociativeCache<K, V, M> implements Cache<K, V, M> {
    private final int setSize;
    private final int entrySize;
    private final ReplacementAlgorithm<K, V, M> replacementAlgorithm;
    private final List<CacheElement<K, V, M>>[] cacheContent;

    public NWaySetAssociativeCache(int setSize, int entrySize, ReplacementAlgorithm<K, V, M> replacementAlgorithm) {
        this.setSize = setSize;
        this.entrySize = entrySize;
        this.replacementAlgorithm = replacementAlgorithm;
        cacheContent = (List<CacheElement<K, V, M>>[]) Array.newInstance(List.class, this.setSize);
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public final V get(K key) {
        List<CacheElement<K, V, M>> searchSet = this.getSet(key);
        V data = null;
        for (CacheElement<K, V, M> ele : searchSet) {
            if (ele.getKey().equals(key)) {
                this.replacementAlgorithm.onGet(ele);
                data = ele.getData();
            }
        }
        return data;
    }

    private List<CacheElement<K, V, M>> getSet(K key) {
        int setIndex = Math.floorMod(key.hashCode(), setSize);
        if (cacheContent[setIndex] == null) {
            cacheContent[setIndex] = new ArrayList<CacheElement<K, V, M>>(this.entrySize);
        }
        return cacheContent[setIndex];
    }

    @Override
    public List<CacheElement<K, V, M>>[] getCacheContents() {
        return cacheContent;
    }
}
