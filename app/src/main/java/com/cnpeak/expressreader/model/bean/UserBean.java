package com.cnpeak.expressreader.model.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserBean {

    /**
     * Result : 0x200
     * UserInfo : {"MemberName":"873178086@qq.com","PurchaseRecord":null,"Lcid":"2052","HiddenCode":"e79e2eed-fe14-4b5e-84ff-0a31b25ecb76"}
     */

    private String Result;
    private UserInfoBean UserInfo;

    public static List<UserBean> arrayUserBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<UserBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public UserInfoBean getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(UserInfoBean UserInfo) {
        this.UserInfo = UserInfo;
    }

    public static class UserInfoBean {
        /**
         * MemberName : 873178086@qq.com
         * PurchaseRecord : null
         * Lcid : 2052
         * HiddenCode : e79e2eed-fe14-4b5e-84ff-0a31b25ecb76
         */

        private String MemberName;
        private Object PurchaseRecord;
        private String Lcid;
        private String HiddenCode;

        public static List<UserInfoBean> arrayUserInfoBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<UserInfoBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getMemberName() {
            return MemberName;
        }

        public void setMemberName(String MemberName) {
            this.MemberName = MemberName;
        }

        public Object getPurchaseRecord() {
            return PurchaseRecord;
        }

        public void setPurchaseRecord(Object PurchaseRecord) {
            this.PurchaseRecord = PurchaseRecord;
        }

        public String getLcid() {
            return Lcid;
        }

        public void setLcid(String Lcid) {
            this.Lcid = Lcid;
        }

        public String getHiddenCode() {
            return HiddenCode;
        }

        public void setHiddenCode(String HiddenCode) {
            this.HiddenCode = HiddenCode;
        }
    }
}
