package com.mycompany.metartafreader.utils;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public final class TokenDateTimeUtil {
    private static final String DATE_TIME_OUTPUT_FORMAT = "dd 'day of month,' HH:mm 'UTC'";

    private TokenDateTimeUtil() {
    }

    public static LocalDateTime parseDayHour(String dayHour) {
        return of(Integer.valueOf(dayHour.substring(0, 2)), Integer.valueOf(dayHour.substring(2, 4)), 0);
    }

    public static LocalDateTime parseDayHourMinute(String dayHourMinute) {
        return of(Integer.valueOf(dayHourMinute.substring(0, 2)), Integer.valueOf(dayHourMinute.substring(2, 4)),
                Integer.valueOf(dayHourMinute.substring(4, 6)));
    }

    public static String format(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern(DATE_TIME_OUTPUT_FORMAT).format(localDateTime);
    }

    private static LocalDateTime of(int day, int hour, int minute) {
        return LocalDateTime.of(2000, Month.JANUARY, day, hour, minute);
    }
}
