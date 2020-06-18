package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }
    public static boolean isBetweenHalfOpen(LocalDateTime ldt, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ldt.compareTo(startDateTime) >= 0 && ldt.compareTo(endDateTime) < 0;
    }

    public static LocalDate toDateOrMinDate(String startDate) {
        return startDate == null || startDate.isEmpty() ? LocalDate.MIN : LocalDate.parse(startDate);
    }
    public static LocalDate toDateOrMaxDate(String endDate) {
        return endDate == null || endDate.isEmpty() ? LocalDate.MAX : LocalDate.parse(endDate);
    }
    public static LocalTime toTimeOrMinTime(String startTime) {
        return startTime == null || startTime.isEmpty() ? LocalTime.MIN : LocalTime.parse(startTime,TIME_FORMATTER);
    }
    public static LocalTime toTimeOrMaxTime(String endTime) {
        return endTime == null || endTime.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTime,TIME_FORMATTER);
    }


    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

