package com.cnpeak.expressreader.model.remote;


import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.model.bean.HotSpot;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author builder by HUANG JIN on 18/11/8
 * @version 1.0
 * @description HotSpot热点新闻工厂类处理方法
 */
public class HotSpotModuleFactory {
    /**
     * 获取热点资讯
     *
     * @param LCID      国家码
     * @param pageIndex 页码索引
     * @param pageSize  每页的索引个数
     * @return HotSpot热点快讯信息bean数据
     */
    public static Observable<List<HotSpot>> getHotSpotList(String LCID, int pageIndex, int pageSize) {
        //从网络中获取热点资讯数据更新
        return ApiManager.builder().getService().getHotSpot(LCID, pageIndex, pageSize).subscribeOn(Schedulers.io());
    }

}
