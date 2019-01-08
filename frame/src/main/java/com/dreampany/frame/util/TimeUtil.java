package com.dreampany.frame.util;

import android.content.Context;

import com.dreampany.frame.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hawladar Roman on 6/11/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class TimeUtil {
    private TimeUtil() {
    }

    public static final long second = 1000;
    public static final long minute = 60 * second;
    public static final long hour = 60 * minute;
    public static final long day = 24 * hour;
    public static final long week = 7 * day;
    private static final String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String FULL_DATE_PATTERN = "MMM dd, yyyy, hh:mm aaa";

    public static long currentTime() {
        return System.currentTimeMillis();
    }

    public static String getUtcTime(long time) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(UTC_PATTERN);
        return fmt.print(time);
    }

    public static long getUtcTime(String time) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern(UTC_PATTERN);
        DateTime jodatime = dtf.parseDateTime(time);
        return jodatime.getMillis();
    }

    public static String getFullTime(long time) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(FULL_DATE_PATTERN);
        return fmt.print(time);
    }

    public static long getPreviousDay() {
/*        DateTime time = new DateTime(TimeUtil.currentTime());
        time.minusDays(1);
        return time.getMillis();*/
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        long time = calendar.getTimeInMillis();
        calendar.clear();
        return time;
    }

    public static long getPreviousWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        long time = calendar.getTimeInMillis();
        calendar.clear();
        return time;
    }

    public static long getPreviousMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        long time = calendar.getTimeInMillis();
        calendar.clear();
        return time;
    }

    public static long getPreviousMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -month);
        long time = calendar.getTimeInMillis();
        calendar.clear();
        return time;
    }

    public static long getPreviousYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        long time = calendar.getTimeInMillis();
        calendar.clear();
        return time;
    }

    public static long startOfDay() {
        return currentTime();
    }

    public static long endOfDay() {
        return currentTime();
    }

    public static String getDelayTime(Context context, long time) {
        if (time <= 0) return null;

        long endTime = currentTime();
        long startTime = time;
        if (startTime > endTime) {
            startTime = endTime;
            endTime = time;
        }

        Period period = new Period(startTime, endTime);

        int years = period.getYears();
        if (years > 0) {
            return TextUtil.getString(context, R.string.year_ago, years);
        }

        int months = period.getMonths();
        if (months > 0) {
            return TextUtil.getString(context, R.string.month_ago, months);
        }

        int weeks = period.getWeeks();
        if (weeks > 0) {
            return TextUtil.getString(context, R.string.week_ago, weeks);
        }

        int days = period.getDays();
        if (days > 0) {
            return TextUtil.getString(context, R.string.day_ago, days);
        }

        return TextUtil.getString(context, R.string.today);
    }

    public static boolean isExpired(long time, long delay) {
        return currentTime() - time > delay;
    }
}
