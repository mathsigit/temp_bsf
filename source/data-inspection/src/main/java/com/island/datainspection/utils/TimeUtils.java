package com.island.datainspection.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HHmm");
    static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");

    public static String getCurrentDate() {
        LocalDate now = LocalDate.now();
        return dateFormat.format(now);
    }

    public static String getCurrentTime() {
        LocalTime now = LocalTime.now();
        return timeFormat.format(now);
    }

    public static String getCurrentDatetime() {
        LocalDateTime now = LocalDateTime.now();
        return dateTimeFormat.format(now);
    }
}
