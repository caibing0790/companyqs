package com.flexport;

import com.flexport.meetingrooms.MeetingRoomsII;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MeetingRoomsIITest {

    private final MeetingRoomsII meetingRoomsII = new MeetingRoomsII();

    @Test
    public void testMinMeetingRooms() {
        // 测试用例1
        int[][] intervals1 = {{0, 30}, {5, 10}, {15, 20}};
        assertEquals(2, meetingRoomsII.minMeetingRooms(intervals1)); // 应该需要2个会议室

        // 测试用例2
        int[][] intervals2 = {{7, 10}, {2, 4}};
        assertEquals(1, meetingRoomsII.minMeetingRooms(intervals2)); // 只需要1个会议室

        // 测试用例3
        int[][] intervals3 = {{1, 5}, {5, 6}, {6, 10}, {10, 15}};
        assertEquals(1, meetingRoomsII.minMeetingRooms(intervals3)); // 连续会议，只需要1个会议室

        // 测试用例4: 空列表
        int[][] intervals4 = {};
        assertEquals(0, meetingRoomsII.minMeetingRooms(intervals4)); // 没有会议，不需要会议室
    }
}