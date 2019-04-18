package com.cnpeak.expressreader.model.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 支付状态结果码
 */
public class PayStatus {

    /**
     * ststus : 0
     */

    private String ststus;

    public static List<PayStatus> arrayPayStatusFromData(String str) {

        Type listType = new TypeToken<ArrayList<PayStatus>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getStstus() {
        return ststus;
    }

    public void setStstus(String ststus) {
        this.ststus = ststus;
    }
}
