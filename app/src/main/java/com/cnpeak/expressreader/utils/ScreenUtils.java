package com.cnpeak.expressreader.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * @author builder by HUANG JIN on 2018/11/5
 * @version 1.0
 * @description Android设备屏幕相关信息获取类（屏幕宽高、dp与px单位转换、状态栏高度）
 */
public class ScreenUtils {
    /**
     * 根据手机分辨率将dp转为px单位
     */
    public static int dip2Px(Context mContext, float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2Dip(Context mContext, float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 屏幕宽高
     *
     * @param mContext 上下文
     * @return 屏幕宽高信息数组
     */
    private static int[] getScreenSize(Context mContext) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return new int[]{screenWidth, screenHeight};
    }

    /**
     * 获取状态栏高度
     *
     * @param mContext 上下文
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(Context mContext) {
        Class<?> c;
        Object obj;
        Field field;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取手机屏幕的宽度
     *
     * @param mContext 上下文
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context mContext) {
        int screen[] = getScreenSize(mContext);
        return screen[0];
    }

    /**
     * 获取手机屏幕的高度
     *
     * @param mContext 上下文
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context mContext) {
        int screen[] = getScreenSize(mContext);
        return screen[1];
    }
}


