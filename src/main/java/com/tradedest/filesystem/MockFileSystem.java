package com.tradedest.filesystem;

import java.util.*;

public class MockFileSystem {
    private final Map<String, Long> files;
    private final Map<String, User> users;

    public MockFileSystem() {
        this.files = new HashMap<>();
        this.users = new HashMap<>();
    }

    public void addFile(String fileName, long fileSize) {
        files.put(fileName, fileSize);
    }

    public void deleteFile(String fileName) {
        files.remove(fileName);
    }

    public long getFileSize(String fileName) {
        return files.get(fileName);
    }

    public boolean containsFile(String fileName) {
        return files.containsKey(fileName);
    }

    public List<Map.Entry<String, Long>> largestNFilesWithPrefix(String prefix, int n) {
        PriorityQueue<Map.Entry<String, Long>> pq = new PriorityQueue<>((o1, o2) -> {
            if (o1.getValue().equals(o2.getValue())) {
                return o1.getKey().compareTo(o2.getKey());
            } else {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        for (Map.Entry<String, Long> entry : files.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                if (pq.size() < n) {
                    pq.add(entry);
                } else {
                    Map.Entry<String, Long> minEntry = pq.peek();
                    if (entry.getValue() > minEntry.getValue()) {
                        pq.poll();
                        pq.add(entry);
                    }
                }
            }
        }
        List<Map.Entry<String, Long>> result = new LinkedList<>();
        if (!pq.isEmpty()) {
            while (!pq.isEmpty()) {
                result.add(0, pq.poll());
            }
        }
        return result;
    }

    public void addUser(String userName, long capacity) {
        users.put(userName, new User(userName, capacity));
    }

    public void addUserFile(String userName, String fileName, long fileSize) {
        if (users.containsKey(userName)) {
            User user = users.get(userName);
            user.addFile(fileName, fileSize);
        }
    }

    public void mergeUsers(String userName1, String userName2) {

    }

    public long getCapacityLimitOfUser(String userName) {
        return 0;
    }

    public boolean containsFileOfUser(String userName, String fileName) {
        return false;
    }

    public void backupUserFiles(String userName) {

    }

    public void restoreUserFiles(String userName) {

    }
}
