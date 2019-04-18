package com.cnpeak.expressreader.utils;

import android.content.Context;
import android.util.Log;

import com.cnpeak.expressreader.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式化相关操作工具类
 */
public class TimeUtils {
    private static final String TAG = "TimeUtils";

    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;

    private static Calendar calendar = Calendar.getInstance();

    /**
     * @return yyyy-mm-dd
     * 2012-12-25
     */
    public static String getDate() {
        return getYear() + "-" + getMonth() + "-" + getDay();
    }

    /**
     * @param format
     * @return yyyy年MM月dd HH:mm
     * MM-dd HH:mm 2012-12-25
     */
    public static String getDate(String format) {
        SimpleDateFormat simple = new SimpleDateFormat(format);
        return simple.format(calendar.getTime());
    }

    /**
     * @return yyyy-MM-dd HH:mm
     * 2012-12-29 23:47
     */
    public static String getDateAndMinute() {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simple.format(calendar.getTime());
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss
     * 2012-12-29 23:47:36
     */
    public static String getFullDate() {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simple.format(calendar.getTime());
    }

    /**
     * 距离今天多久
     *
     * @param dateStr 当前字符串
     * @return
     */
    public static String fromToday(Context context, String dateStr) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simple.parse(dateStr);
            calendar.setTime(date);

            long time = date.getTime() / 1000;
            long now = new Date().getTime() / 1000;
            long ago = now - time;
            if (ago <= ONE_HOUR)
                return ago / ONE_MINUTE + context.getResources().getString(R.string.expressreader_minutes_before);
            else if (ago <= ONE_DAY)
                return ago / ONE_HOUR + context.getResources().getString(R.string.expressreader_hour);
//                        + (ago % ONE_HOUR / ONE_MINUTE) + context.getResources().getString(R.string.expressreader_minutes_before);
            else if (ago <= ONE_DAY * 2)
                return context.getResources().getString(R.string.expressreader_yesterday);
//                        + calendar.get(Calendar.HOUR_OF_DAY)
//                        + context.getResources().getString(R.string.expressreader_point)
//                        + calendar.get(Calendar.MINUTE)
//                        + context.getResources().getString(R.string.expressreader_minute);
            else if (ago <= ONE_DAY * 3)
                return context.getResources().getString(R.string.expressreader_days_before);
//                        + calendar.get(Calendar.HOUR_OF_DAY)
//                        + context.getResources().getString(R.string.expressreader_point)
//                        + calendar.get(Calendar.MINUTE)
//                        + context.getResources().getString(R.string.expressreader_minute);
            else if (ago <= ONE_MONTH) {
                long day = ago / ONE_DAY;
                return day + context.getResources().getString(R.string.expressreader_day_before);
//                        + calendar.get(Calendar.HOUR_OF_DAY)
//                        + context.getResources().getString(R.string.expressreader_point)
//                        + calendar.get(Calendar.MINUTE)
//                        + context.getResources().getString(R.string.expressreader_minute);
            } else if (ago <= ONE_YEAR) {
                long month = ago / ONE_MONTH;
//                long day = ago % ONE_MONTH / ONE_DAY;
                return month + context.getResources().getString(R.string.expressreader_month);
//                        + day + context.getResources().getString(R.string.expressreader_day_before)
//                        + calendar.get(Calendar.HOUR_OF_DAY) + context.getResources().getString(R.string.expressreader_point)
//                        + calendar.get(Calendar.MINUTE) + context.getResources().getString(R.string.expressreader_minute);
            } else {
                long year = ago / ONE_YEAR;
//                int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0 so month+1
                return year + context.getResources().getString(R.string.expressreader_year_before);
//                        + month + context.getResources().getString(R.string.expressreader_month)
//                        + calendar.get(Calendar.DATE) + context.getResources().getString(R.string.expressreader_day);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateStr;

    }

    /**
     * 距离今天的绝对时间
     *
     * @param date
     * @return
     */
    public static String toToday(Date date) {
        long time = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟";
        else if (ago <= ONE_DAY * 2)
            return "昨天" + (ago - ONE_DAY) / ONE_HOUR + "点" + (ago - ONE_DAY)
                    % ONE_HOUR / ONE_MINUTE + "分";
        else if (ago <= ONE_DAY * 3) {
            long hour = ago - ONE_DAY * 2;
            return "前天" + hour / ONE_HOUR + "点" + hour % ONE_HOUR / ONE_MINUTE
                    + "分";
        } else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            long hour = ago % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return day + "天前" + hour + "点" + minute + "分";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            long hour = ago % ONE_MONTH % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_MONTH % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return month + "个月" + day + "天" + hour + "点" + minute + "分前";
        } else {
            long year = ago / ONE_YEAR;
            long month = ago % ONE_YEAR / ONE_MONTH;
            long day = ago % ONE_YEAR % ONE_MONTH / ONE_DAY;
            return year + "年前" + month + "月" + day + "天";
        }

    }

    private static String getYear() {
        return calendar.get(Calendar.YEAR) + "";
    }

    private static String getMonth() {
        int month = calendar.get(Calendar.MONTH) + 1;
        return month + "";
    }

    private static String getDay() {
        return calendar.get(Calendar.DATE) + "";
    }

    public static String get24Hour() {
        return calendar.get(Calendar.HOUR_OF_DAY) + "";
    }

    public static String getMinute() {
        return calendar.get(Calendar.MINUTE) + "";
    }

    public static String getSecond() {
        return calendar.get(Calendar.SECOND) + "";
    }

}
