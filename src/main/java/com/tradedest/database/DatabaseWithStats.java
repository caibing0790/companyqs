package com.tradedest.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseWithStats<K, V> extends SimpleDatabase<K, V> {
    private Map<String, Integer> stats;

    public DatabaseWithStats() {
        super();
        this.stats = new HashMap<>();
    }

    @Override
    public void create(K key, V value) {
        recodeOperation("CREATE");
        super.create(key, value);
    }

    @Override
    public V read(K key) {
        recodeOperation("READ");
        return super.read(key);
    }

    @Override
    public void update(K key, V value) {
        recodeOperation("UPDATE");
        super.update(key, value);
    }

    @Override
    public void delete(K key) {
        recodeOperation("DELETE");
        super.delete(key);
    }

    private void recodeOperation(String operation) {
        stats.put(operation, stats.getOrDefault(operation, 0) + 1);
    }

    public List<Map.Entry<String, Integer>> getTopNOperations(int topN) {
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(stats.entrySet());
        entryList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        return entryList.subList(0, Math.min(topN, entryList.size()));
    }
}
