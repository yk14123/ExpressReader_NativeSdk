package com.cnpeak.expressreader.model.remote;

import com.cnpeak.expressreader.model.local.UserInfoLocalFactory;
import com.cnpeak.expressreader.model.bean.UserBean;
import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.net.ApiService;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UserBeanModuleFactory {

    public static Observable<UserBean.UserInfoBean> signInByEmail(JsonObject requestBody) {
        return ApiManager.builder()
                .createServiceFrom(ApiService.class)
                .signInByEmail(requestBody)
                .map(new Function<UserBean, UserBean.UserInfoBean>() {
                    @Override
                    public UserBean.UserInfoBean apply(UserBean userBean) {
                        if (userBean != null) {
                            return userBean.getUserInfo();
                        }
                        return null;
                    }
                })
                .doAfterNext(new Consumer<UserBean.UserInfoBean>() {
                    @Override
                    public void accept(UserBean.UserInfoBean userInfo) {

                        UserInfoLocalFactory.builder().setUserInfo(userInfo);

                    }
                })
                .subscribeOn(Schedulers.io());

    }

}
