package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String formatTime(Instant timeStamp) {
        if (timeStamp == null) return "N/A";
        return timeStamp.atZone(ZoneId.systemDefault()).format(formatter);
    }
}
