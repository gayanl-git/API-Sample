package com.practical.assignment.util;

import java.util.Date;

public class DateUtil {

    public static boolean dateDiffLessThan(Date date1, Long diffInMilliSeconds) {
        Long diff = Math.abs(date1.getTime() - new Date().getTime());
        return diff <= diffInMilliSeconds;
    }
}
