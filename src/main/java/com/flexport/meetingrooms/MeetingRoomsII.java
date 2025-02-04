package com.flexport.meetingrooms;

import java.util.Map;
import java.util.TreeMap;

public class MeetingRoomsII {
    public int minMeetingRooms(int[][] intervals) {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        for (int[] interval : intervals) {
            treeMap.put(interval[0], treeMap.getOrDefault(interval[0], 0) + 1);
            treeMap.put(interval[1], treeMap.getOrDefault(interval[1], 0) - 1);
        }

        int minRoomNeed = 0;
        int roomNeed = 0;
        for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
            int value = entry.getValue();
            roomNeed += value;
            minRoomNeed = Math.max(minRoomNeed, roomNeed);
        }

        return minRoomNeed;
    }
}
