package com.cnpeak.expressreader.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

import java.text.NumberFormat;

import androidx.core.content.ContextCompat;

/**
 * 权限申请帮助类
 */
public class PermissionHelper {

    /**
     * 检查指定权限
     *
     * @param context    上下文
     * @param permission 权限标示 see{@link android.Manifest.permission}
     * @return boolean 是否拥有此权限
     */
    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * 请求权限列表
     *
     * @param activity    Activity实例对象
     * @param permissions 未申请的权限列表
     * @param requestCode 请求码
     */
    public static void requestRuntimePermission(Activity activity, String[] permissions, int requestCode) {
        if (checkPermissionVersion()) {
            activity.requestPermissions(permissions, requestCode);
        }

    }

    /**
     * 检查权限版本兼容
     *
     * @return the boolean
     */
    public static boolean checkPermissionVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    @SuppressLint("MissingPermission")
    public static String[] getKnownLocation(Context context) {
        if (context != null) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    String[] locationInfo = new String[2];
                    NumberFormat format = NumberFormat.getNumberInstance();
                    format.setMaximumFractionDigits(3);
                    locationInfo[0] = format.format(location.getLatitude());
                    locationInfo[1] = format.format(location.getLongitude());
                    return locationInfo;
                }
            }
        }
        return null;
    }

}
