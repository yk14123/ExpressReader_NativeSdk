package com.cnpeak.expressreader.model.remote;

import com.cnpeak.expressreader.model.bean.PayStatus;
import com.cnpeak.expressreader.net.ApiManager;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class PayStatusModuleFactory {

    public static Observable<PayStatus> sendPayReceipt(JsonObject requestBody) {
        return ApiManager.builder().getService()
                .sendPayReceipt(requestBody).subscribeOn(Schedulers.io());
    }

}

