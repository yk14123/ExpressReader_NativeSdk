package com.cnpeak.expressreader.model.remote;

import android.util.Log;

import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.net.ApiManager;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsClickEventFactory {

    private static final String TAG = "AnalyticsAgent";

    public static void sendEvent(String NID, String Latitude, String Longitude,
                                 String RealLocation, String LCID) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ErConstant.PLATFORM, "Android");
        jsonObject.addProperty(ErConstant.NID, NID);
        jsonObject.addProperty(ErConstant.LATITUDE, Latitude);
        jsonObject.addProperty(ErConstant.LONGITUDE, Longitude);
        jsonObject.addProperty(ErConstant.REAL_LOCATION, RealLocation);
        jsonObject.addProperty(ErConstant.LCID, LCID);

        ApiManager.builder()
                .getService()
                .sendNewsClickEvent(jsonObject)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(TAG, "onResponse: code >>> " + response.code());
                        Log.d(TAG, "onResponse: message >>> " + response.message());
                        Log.d(TAG, "onResponse: errorBody >>> " + response.errorBody());
                        Log.d(TAG, "onResponse: string >>> " + response.toString());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG, "onFailure: message >>> " + t.getLocalizedMessage());
                    }


                });

    }


}
