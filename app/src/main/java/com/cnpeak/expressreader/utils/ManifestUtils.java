package com.cnpeak.expressreader.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * @author builder by PAYNE on 2018/11/1
 * @version 1.0
 * @description manifest工具类
 */
public class ManifestUtils {

    private static final String TAG = "ManifestUtils";

    /**
     * 返回Manifest指定meta-data值
     *
     * @param context 全局context
     * @param key     meta-data key
     * @return 成功-value
     * 失败-""
     */
    public static String getMetaData(Context context, String key) {
        ApplicationInfo app_info = null;
        try {
            app_info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (app_info == null || app_info.metaData == null) {
                return "";
            } else {
                Object obj = app_info.metaData.get(key);
                if (obj != null) {
                    return obj.toString();
                } else {
                    return "";
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "getMetaData error", e);
            return "";
        }
    }

    /**
     * 获取版本名
     *
     * @param context context
     * @return version name 版本名
     */
    public static String getVersionName(Context context) {
        String version = "0.0";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
            LogUtils.e(TAG, "getVersionName error", e);
        }

        return version;
    }


    /**
     * 获取应用程序版本名称信息
     *
     * @param context 环境
     * @return 当前应用的版本名称
     */
    public static String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
