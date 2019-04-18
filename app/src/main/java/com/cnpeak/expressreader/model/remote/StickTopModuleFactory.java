package com.cnpeak.expressreader.model.remote;

import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.net.ApiService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author builder by HUANG JIN on 18/12/25
 * @version 1.0
 * @description 获取首页热点资讯banner数据
 */
public class StickTopModuleFactory {
    /**
     * 获取热点资讯
     *
     * @param LCID 国家码
     * @return 获取首页热点资讯banner数据
     */
    public static Observable<List<HotSpot>> getStickTop(String LCID) {
        //从网络中获取热点资讯数据更新
        return ApiManager.builder().createServiceFrom(ApiService.class)
                .getStickTop(LCID).subscribeOn(Schedulers.io());
    }

}
