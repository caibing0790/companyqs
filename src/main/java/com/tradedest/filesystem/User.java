package com.tradedest.filesystem;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userName;
    private long capacity;
    private long usedSpace;
    private Map<String, Long> files;

    public User(String userName, long capacity) {
        this.userName = userName;
        this.capacity = capacity;
        this.usedSpace = 0;
        files = new HashMap<>();
    }

    public long getLeftSpace() {
        return capacity - usedSpace;
    }

    public void addFile(String fileName, long fileSize) {
        if (getLeftSpace() >= fileSize) {
            files.put(fileName, fileSize);
            usedSpace += fileSize;
        } else {
            throw new IllegalArgumentException("Not enough space");
        }
    }

    public void deleteFile(String fileName) {
        if (files.containsKey(fileName)) {
            long fileSize = files.get(fileName);
            files.remove(fileName);
            usedSpace -= fileSize;
        } else {
            throw new IllegalArgumentException("File not found");
        }
    }
}
