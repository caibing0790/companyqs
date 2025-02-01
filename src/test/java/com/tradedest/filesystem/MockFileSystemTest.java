package com.tradedest.filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.AbstractMap;

public class MockFileSystemTest {
    @Test
    public void testBasicOperations() {
        MockFileSystem fs = new MockFileSystem();
        fs.addFile("file1", 100);
        assertEquals(100, fs.getFileSize("file1"));
        fs.deleteFile("file1");
        assertFalse(fs.containsFile("file1"));
    }

    @Test
    public void testLargestNFilesWithPrefix() {
        MockFileSystem fs = new MockFileSystem();
        fs.addFile("fileA1", 100);
        fs.addFile("fileB1", 200);
        fs.addFile("fileA2", 50);
        List<Map.Entry<String, Long>> largestFiles = fs.largestNFilesWithPrefix("fileA", 2);
        assertEquals(new AbstractMap.SimpleEntry<>("fileA1", 100L), largestFiles.get(0));
        assertEquals(new AbstractMap.SimpleEntry<>("fileA2", 50L), largestFiles.get(1));
    }

    @Test
    public void testUserCapacityLimit() {
        MockFileSystem fs = new MockFileSystem();
        fs.addUser("user1", 150);
        fs.addUserFile("user1", "file1", 100);
        assertThrows(IllegalArgumentException.class, () -> fs.addUserFile("user1", "file2", 60));
    }

    @Test
    public void testMergeUsers() {
        MockFileSystem fs = new MockFileSystem();
        fs.addUser("user1", 150);
        fs.addUser("user2", 100);
        fs.addUserFile("user1", "file1", 50);
        fs.addUserFile("user2", "file2", 50);
        fs.mergeUsers("user1", "user2");
        assertEquals(200, fs.getCapacityLimitOfUser("user1"));
        assertTrue(fs.containsFileOfUser("user1", "file2"));
    }

    @Test
    public void testBackupAndRestore() {
        MockFileSystem fs = new MockFileSystem();
        fs.addUser("user1", 200);
        fs.addUserFile("user1", "file1", 100);
        fs.backupUserFiles("user1");
        fs.addUserFile("user1", "file2", 50);
        fs.restoreUserFiles("user1");
        assertFalse(fs.containsFileOfUser("user1", "file2"));
        assertTrue(fs.containsFileOfUser("user1", "file1"));
    }
}