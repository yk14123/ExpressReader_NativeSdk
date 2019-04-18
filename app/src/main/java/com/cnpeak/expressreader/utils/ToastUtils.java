package com.cnpeak.expressreader.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author builder by PAYNE on 2018/11/1
 * @version 1.0
 * Toast相关方法
 */
public class ToastUtils {

    private static Toast mToast = null;
    /**
     * 测试用的话，就改为true给人用的话就为false
     */
    private final static boolean isShow = true;

    /**
     * 显示toast，默认为不显示，只给调试人员看，根据isShow来控制
     *
     * @param mContext     上下文
     * @param charSequence 显示文字内容
     */
    public static void showS(Context mContext, CharSequence charSequence) {
        if (isShow) {
            makeText(mContext, charSequence, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 根据传进来的flag来判断是否显示Toast内容
     *
     * @param mContext     上下文
     * @param charSequence 显示的文字内容
     * @param flag         判断是否显示Toast消息
     */
    public static void showS(Context mContext, CharSequence charSequence, boolean flag) {
        if (flag) {
            makeText(mContext, charSequence, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 显示toast，默认为不显示，只给调试人员看，根据isShow来控制
     *
     * @param mContext 上下文
     * @param resource 字符串资源ID
     */
    public static void showS(Context mContext, int resource) {
        if (isShow) {
            makeText(mContext, resource, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 根据传进来的flag来判断是否显示Toast内容
     *
     * @param mContext 上下文
     * @param resource 字符串资源ID
     * @param flag     判断是否显示Toast消息
     */
    public static void showS(Context mContext, int resource, boolean flag) {
        if (flag) {
            makeText(mContext, resource, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 显示toast，默认为不显示，只给调试人员看，根据isShow来控制
     *
     * @param mContext     上下文
     * @param charSequence 显示的文字内容
     */
    public static void showL(Context mContext, CharSequence charSequence) {
        if (isShow) {
            makeText(mContext, charSequence, Toast.LENGTH_LONG);
        }
    }

    /**
     * 显示toast，默认为不显示，只给调试人员看，根据isShow来控制
     *
     * @param mContext 上下文
     * @param resource 字符串资源ID
     */
    public static void showL(Context mContext, int resource) {
        if (isShow) {
            makeText(mContext, resource, Toast.LENGTH_LONG);
        }
    }

    /**
     * 根据传进来的flag来判断是否显示Toast内容
     *
     * @param mContext     上下文
     * @param charSequence 文字内容
     * @param flag         是否显示标志
     */
    public static void showL(Context mContext, CharSequence charSequence,
                             boolean flag) {
        if (flag) {
            makeText(mContext, charSequence, Toast.LENGTH_LONG);
        }
    }

    /**
     * 根据传进来的flag来判断是否显示Toast内容
     *
     * @param mContext 上下文
     * @param resource 字符串资源ID
     * @param flag     标志
     */
    public static void showL(Context mContext, int resource, boolean flag) {
        if (flag) {
            makeText(mContext, resource, Toast.LENGTH_LONG);
        }
    }

    /**
     * 本人根据源代码研究出来的可以马上显示下一个Toast内容的方法
     *
     * @param mContext     上下文
     * @param charSequence 文字显示内容
     * @param time         自定义显示的时长
     */
    private static void makeText(Context mContext, CharSequence charSequence,
                                 int time) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, charSequence, time);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        } else {
            mToast.cancel();
            mToast = Toast.makeText(mContext, charSequence, time);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }
    }

    /**
     * 本人根据源代码研究出来的可以马上显示下一个Toast内容的方法
     *
     * @param mContext 上下文
     * @param time     自定义显示的时长
     */
    private static void makeText(Context mContext, int resource, int time) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, resource, time);
            mToast.show();
        } else {
            mToast.cancel();
            mToast = Toast.makeText(mContext, resource, time);
            mToast.show();
        }
    }
}
