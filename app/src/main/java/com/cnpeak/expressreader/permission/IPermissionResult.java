package com.cnpeak.expressreader.permission;

public interface IPermissionResult {
    /**
     * Activity对象为空错误
     */
    int REQUEST_PERMISSION_ACTIVITY_NULL = 0x100;

    /**
     * 请求权限列表为空
     */
    int REQUEST_PERMISSION_GROUP_LIST_NULL = 0x101;

    /**
     * 结果回调对象为空
     */
    int REQUEST_PERMISSION_CALLBACK_NULL = 0x102;

    void onGrand();

    void onDenied(String[] deniedPermissions);

    void onError(int errorCode);

}
