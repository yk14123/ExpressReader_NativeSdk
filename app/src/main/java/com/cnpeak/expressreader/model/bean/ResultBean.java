package com.cnpeak.expressreader.model.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResultBean {

    /**
     * Result : 0x401
     */
    private String Result;

    public static List<ResultBean> arrayResultBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<ResultBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

}
