package com.cnpeak.expressreader.model.remote;


import android.util.Log;

import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.net.ApiService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author builder by HUANG JIN on 18/12/26
 * @version 1.0
 * 获取指定报纸ID下的详情数据
 */
public class NewsPaperModuleFactory {
    private static final String TAG = "NewsPaperModuleFactory";

    public static Observable<List<HotSpot>> getNewsPaper(String LCID, String newsId, int pageIndex, int pageSize) {

        Log.d(TAG, "getNewsPaper:newsId>>>  " + newsId);
        return ApiManager.builder()
                .createServiceFrom(ApiService.class)
                .getNewsPaper(LCID, newsId, pageIndex, pageSize)
                .subscribeOn(Schedulers.io());
    }
}
