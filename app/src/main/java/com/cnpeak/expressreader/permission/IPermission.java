package com.cnpeak.expressreader.permission;

import android.app.Activity;

/**
 * 权限申请回调
 */
public interface IPermission {

    /**
     * 请求权限列表
     *
     * @param activity    Activity实例对象
     * @param permissions 权限列表
     * @param requestCode 请求码
     */
    void requestPermission(Activity activity, String[] permissions, int requestCode);

    /**
     * 权限请求结果回调
     *
     * @param requestCode  请求码
     * @param permissions  权限列表
     * @param grantResults 权限申请结果数组
     */
    void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);

}
