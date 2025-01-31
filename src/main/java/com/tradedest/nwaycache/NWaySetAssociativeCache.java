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

    /**
     * 将键值对添加或更新到缓存中
     * 如果键已存在，则更新其值；如果缓存已满，则根据替换算法选择并替换元素
     *
     * @param key 要添加或更新的键
     * @param value 对应的值
     */
    @Override
    public synchronized void put(K key, V value) {
        CacheElement<K, V, M> evictionCandidate = null;

        List<CacheElement<K, V, M>> targetSet = getSet(key);

        for (CacheElement<K, V, M> element : targetSet) {
            if (this.entrySize == targetSet.size()) {
                evictionCandidate = chooseBetterEvictionCandidate(evictionCandidate, element);
            }
            if (element.getKey().equals(key)) {
                element.setData(value);
                this.replacementAlgorithm.onSet(element);
                return;
            }
        }

        if (this.entrySize > targetSet.size()) {
            CacheElement<K, V, M> newElement = new CacheElement<>(key, value);
            targetSet.add(newElement);
            this.replacementAlgorithm.onSet(newElement);
            return;
        }

        if (evictionCandidate != null) {
            evictionCandidate.setKeyData(key, value);
            this.replacementAlgorithm.onSet(evictionCandidate);
        }
    }

    private CacheElement<K, V, M> chooseBetterEvictionCandidate(CacheElement<K, V, M> current, CacheElement<K, V, M> candidate) {
        if (current == null) {
            return candidate;
        }
        if (this.replacementAlgorithm.compare(current, candidate) > 0) {
            return candidate;
        } else {
            return current;
        }
    }

    @Override
    public final V get(K key) {
        List<CacheElement<K, V, M>> searchSet = this.getSet(key);
        V data = null;
        for (CacheElement<K, V, M> ele : searchSet) {
            if (ele.getKey().equals(key)) {
                this.replacementAlgorithm.onGet(ele);
                data = ele.getData();
                break;
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
