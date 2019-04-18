package com.cnpeak.expressreader.model.remote;

import com.cnpeak.expressreader.model.bean.ResultBean;
import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.net.ApiService;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * The type ResultBean module factory.
 */
public class ResultBeanModuleFactory {

    /**
     * @param requestBody 请求体
     * @return the observable
     */
    public static Observable<ResultBean> registerByEmail(JsonObject requestBody) {
        return ApiManager.builder().createServiceFrom(ApiService.class)
                .registerByEmail(requestBody).subscribeOn(Schedulers.io());
    }

}
