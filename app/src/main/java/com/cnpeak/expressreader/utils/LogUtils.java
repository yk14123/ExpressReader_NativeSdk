package com.cnpeak.expressreader.utils;

import android.util.Log;

import com.cnpeak.expressreader.BuildConfig;

/**
 * @author builder by PAYNE on 2018/11/1
 * @version 1.0
 * @description 设LOG工具类 默认tag-LOG
 */
public class LogUtils {

    private static final String TAG = "LOGGER";
    private static boolean LOG_ENABLE = BuildConfig.DEBUG;

    private static String buildMsg(String msg) {
        StringBuilder buffer = new StringBuilder();

//        if (DETAIL_ENABLE) {
//            final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
//
//            buffer.append("[ ");
//            buffer.append(Thread.currentThread().getName());
//            buffer.append(": ");
//            buffer.append(stackTraceElement.getFileName());
//            buffer.append(": ");
//            buffer.append(stackTraceElement.getLineNumber());
//            buffer.append(": ");
//            buffer.append(stackTraceElement.getMethodName());
//        }

//        buffer.append("() ] _____ ");

        buffer.append(msg);

        return buffer.toString();
    }

    /**
     * 设置是否显示Log
     *
     * @param enable true-显示 false-不显示
     */
    public static void setLogEnable(boolean enable) {
        LOG_ENABLE = enable;
    }

    /**
     * verbose log
     *
     * @param msg log msg
     */
    public static void v(String msg) {
        if (LOG_ENABLE) {
            Log.v(TAG, buildMsg(msg));
        }
    }

    /**
     * verbose log
     *
     * @param tag tag
     * @param msg log msg
     */
    public static void v(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.v(tag, buildMsg(msg));
        }
    }

    /**
     * debug log
     *
     * @param msg log msg
     */
    public static void d(String msg) {
        if (LOG_ENABLE) {
            Log.d(TAG, buildMsg(msg));
        }
    }

    /**
     * debug log
     *
     * @param tag tag
     * @param msg log msg
     */
    public static void d(String tag, String msg) {
        if (LOG_ENABLE && Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, buildMsg(msg));
        }
    }

    /**
     * info log
     *
     * @param msg log msg
     */
    public static void i(String msg) {
        if (LOG_ENABLE) {
            Log.i(TAG, buildMsg(msg));
        }
    }

    /**
     * info log
     *
     * @param tag tag
     * @param msg log msg
     */
    public static void i(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.i(tag, buildMsg(msg));
        }
    }

    /**
     * warning log
     *
     * @param msg log msg
     */
    public static void w(String msg) {
        if (LOG_ENABLE) {
            Log.w(TAG, buildMsg(msg));
        }
    }

    /**
     * warning log
     *
     * @param msg log msg
     * @param e   exception
     */
    public static void w(String msg, Exception e) {
        if (LOG_ENABLE) {
            Log.w(TAG, buildMsg(msg), e);
        }
    }

    /**
     * warning log
     *
     * @param tag tag
     * @param msg log msg
     */
    public static void w(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.w(tag, buildMsg(msg));
        }
    }

    /**
     * warning log
     *
     * @param tag tag
     * @param msg log msg
     * @param e   exception
     */
    public static void w(String tag, String msg, Exception e) {
        if (LOG_ENABLE) {
            Log.w(tag, buildMsg(msg), e);
        }
    }

    /**
     * error log
     *
     * @param msg log msg
     */
    public static void e(String msg) {
        if (LOG_ENABLE) {
            Log.e(TAG, buildMsg(msg));
        }
    }

    /**
     * error log
     *
     * @param msg log msg
     * @param e   exception
     */
    public static void e(String msg, Exception e) {
        if (LOG_ENABLE) {
            Log.e(TAG, buildMsg(msg), e);
        }
    }

    /**
     * error log
     *
     * @param tag tag
     * @param msg msg
     */
    public static void e(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.e(tag, buildMsg(msg));
        }
    }

    /**
     * error log
     *
     * @param tag tag
     * @param msg log msg
     * @param e   exception
     */
    public static void e(String tag, String msg, Exception e) {
        if (LOG_ENABLE) {
            Log.e(tag, buildMsg(msg), e);
        }
    }
}
