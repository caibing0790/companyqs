package com.tradedest.filesystem;

import java.util.*;

public class MockFileSystem {
    private final Map<String, Long> files;
    private final Map<String, User> users;
    private final Map<String, Map<String, Long>> backups;

    public MockFileSystem() {
        this.files = new HashMap<>();
        this.users = new HashMap<>();
        this.backups = new HashMap<>();
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

    public User getUser(String userName) {
        if (!users.containsKey(userName)) {
            throw new IllegalArgumentException("User does not exist");
        }
        return users.get(userName);
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

    public void mergeUsers(String mergeToUserName, String mergeFromUserName) {
        validateUser(mergeToUserName);
        validateUser(mergeFromUserName);
        User mergeToUser = users.get(mergeToUserName);
        User mergeFromUser = users.get(mergeFromUserName);
        mergeToUser.addCapacity(mergeFromUser.getCapacity());
        mergeToUser.addFiles(mergeFromUser.getFiles());
        users.remove(mergeFromUserName);
        mergeFromUser.deleteAllFiles();
    }

    private void validateUser(String userName) {
        if (!users.containsKey(userName)) {
            throw new IllegalArgumentException("User does not exist");
        }
    }

    public long getCapacityLimitOfUser(String userName) {
        if (users.containsKey(userName)) {
            return users.get(userName).getCapacity();
        } else {
            throw new IllegalArgumentException("User does not exist");
        }
    }

    public boolean containsFileOfUser(String userName, String fileName) {
        if (users.containsKey(userName)) {
            return users.get(userName).getFiles().containsKey(fileName);
        }
        return false;
    }

    public void backupUserFiles(String userName) {
        this.backups.put(userName, new HashMap<>(users.get(userName).getFiles()));
    }

    public void restoreUserFiles(String userName) {
        User user = users.get(userName);
        if (!backups.containsKey(userName)) {
            throw new IllegalArgumentException("No backup found for user");
        }
        Map<String, Long> userBackup = backups.get(userName);
        long totalSizeOfBackup = 0;
        for (Map.Entry<String, Long> entry : userBackup.entrySet()) {
            String fileName = entry.getKey();
            if (isOccupyByOtherUsers(fileName, userName)) {
                continue;
            }
            totalSizeOfBackup += entry.getValue();
        }
        if (user.getCapacity() < totalSizeOfBackup) {
            throw new IllegalArgumentException("Not enough capacity to restore backup");
        }
        user.deleteAllFiles();
        for (Map.Entry<String, Long> entry : userBackup.entrySet()) {
            String fileName = entry.getKey();
            if (isOccupyByOtherUsers(fileName, userName)) {
                continue;
            }
            user.addFile(entry.getKey(), entry.getValue());
        }
    }

    private boolean isOccupyByOtherUsers(String fileName, String userName) {
        for (Map.Entry<String, User> entry : users.entrySet()) {
            if (!entry.getKey().equals(userName) && entry.getValue().getFiles().containsKey(fileName)) {
                return true;
            }
        }
        return false;
    }
}
