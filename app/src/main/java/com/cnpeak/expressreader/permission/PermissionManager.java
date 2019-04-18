package com.cnpeak.expressreader.permission;

import android.app.Activity;
import android.content.pm.PackageManager;

import java.util.ArrayList;

/**
 * 权限申请管理类
 */
public class PermissionManager implements IPermission {

    /**
     * 权限申请请求码
     */
    public static final int PERMISSION_REQUEST_CODE = 0x104;

    /**
     * 请求结果回调对象
     */
    private IPermissionResult onPermissionResult;

    /**
     * Instantiates a new Permission manager.
     *
     * @param onPermissionResult 请求结果回调
     */
    public PermissionManager(IPermissionResult onPermissionResult) {
        this.onPermissionResult = onPermissionResult;
    }

    @Override
    public void requestPermission(Activity activity, String[] permissions, int requestCode) {
        if (onPermissionResult == null) {
            return;
        }
        if (activity == null) {
            onPermissionResult.onError(IPermissionResult.REQUEST_PERMISSION_ACTIVITY_NULL);
            return;
        }
        if (permissions == null || permissions.length <= 0) {
            onPermissionResult.onError(IPermissionResult.REQUEST_PERMISSION_GROUP_LIST_NULL);
            return;
        }

        //开始检查权限,将未获得权限的列表放入申请权限列表中；
        ArrayList<String> deniedList = new ArrayList<>();
        for (String permission : permissions) {
            boolean granted = PermissionHelper.checkPermission(activity, permission);
            if (!granted) {
                deniedList.add(permission);
            }
        }

        //当前未申请权限的列表不为空，进行权限申请
        if (deniedList.size() > 0) {
            PermissionHelper.requestRuntimePermission(activity, deniedList.toArray(new String[0]), requestCode);
        }

    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //处理结果回调
                ArrayList<String> deniedPermissions = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        deniedPermissions.add(permissions[i]);
                    }
                }
                //检查是否有未申请成功的权限列表
                if (deniedPermissions.size() == 0) {
                    onPermissionResult.onGrand();
                } else {
                    onPermissionResult.onDenied(deniedPermissions.toArray(new String[0]));
                }
                break;
        }

    }

}
