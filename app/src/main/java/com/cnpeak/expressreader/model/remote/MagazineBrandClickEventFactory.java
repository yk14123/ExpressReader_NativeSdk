package com.cnpeak.expressreader.model.remote;

import android.util.Log;

import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.net.ApiService;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MagazineBrandClickEventFactory {

    private static final String TAG = "AnalyticsAgent";

    public static void sendEvent(String Id, String brandCode, String userName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ErConstant.ID, Id);
        jsonObject.addProperty(ErConstant.BRAND_CODE, brandCode);
        jsonObject.addProperty(ErConstant.USER_NAME, userName);
        Date date = new Date(System.currentTimeMillis());
        DateFormat instance = SimpleDateFormat.getDateInstance();
        String clickTime = instance.format(date);
        Log.d(TAG, "sendEvent: clickTime >>> " + clickTime);
        jsonObject.addProperty(ErConstant.CLICK_TIME, clickTime);

        ApiManager.builder()
                .createServiceFrom(ApiService.class)
                .sendMagBrandClickEvent(jsonObject)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(TAG, "onResponse: code >>> " + response.code());
//                        Log.d(TAG, "onResponse: message >>> " + response.message());
//                        Log.d(TAG, "onResponse: errorBody >>> " + response.errorBody());
//                        Log.d(TAG, "onResponse: string >>> " + response.toString());

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG, "onFailure: message >>> " + t.getLocalizedMessage());
                    }

                });


    }


}
