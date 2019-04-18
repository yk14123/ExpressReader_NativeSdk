package com.cnpeak.expressreader.model.local;

import android.text.TextUtils;
import android.util.Log;

import com.cnpeak.expressreader.ErApplication;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.model.bean.UserBean;
import com.cnpeak.expressreader.utils.SpUtil;
import com.google.gson.Gson;

/**
 * The type User manager.
 */
public class UserInfoLocalFactory {
    private static final String TAG = "UserInfoLocalFactory";

    private UserBean.UserInfoBean userInfo;

    private UserInfoLocalFactory() {

    }

    private static class Holder {
        private static UserInfoLocalFactory sUserInfoLocalFactory = new UserInfoLocalFactory();

        private Holder() {
        }
    }

    /**
     * New instance user manager.
     *
     * @return the user manager
     */
    public static UserInfoLocalFactory builder() {
        return Holder.sUserInfoLocalFactory;
    }

    /**
     * Sets user info.
     * 设置用户缓存信息
     *
     * @param userInfo the user info
     */
    public void setUserInfo(UserBean.UserInfoBean userInfo) {
        if (userInfo != null) {
            this.userInfo = userInfo;
            Gson gson = new Gson();
            String userJson = gson.toJson(userInfo);
            Log.d(TAG, "setUserInfo: >>> " + userJson);
            SpUtil.setString(ErApplication.getAppContext(), ErConstant.USER_INFO, userJson);
        }

    }

    /**
     * Gets user info.
     * 获取本地缓存信息
     *
     * @return the user info
     */
    public UserBean.UserInfoBean getUserInfo() {
        String userJson = SpUtil.getString(ErApplication.getAppContext(), ErConstant.USER_INFO);
        if (!TextUtils.isEmpty(userJson)) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, UserBean.UserInfoBean.class);
        }
        return userInfo;
    }

    public boolean isLogin() {
        UserBean.UserInfoBean userInfo = getUserInfo();
        return userInfo == null;
    }

    /**
     * Logout.
     * 清空用户信息
     */
    public void logout() {
        userInfo = null;
        SpUtil.setString(ErApplication.getAppContext(), ErConstant.USER_INFO, "");
    }


}
