package com.cnpeak.expressreader.model.remote;

import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.model.bean.MagazineList;
import com.cnpeak.expressreader.net.ApiService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author builder by HUANG JIN on 18/11/8
 * @version 1.0
 * @description MagazineList杂志数据列表网络获取类
 */
public class MagazineListModuleFactory {
    /**
     * 获取热点资讯
     *
     * @param LCID 国家码
     * @return 根据指定的LCID国家码获取杂志数据列表
     */
    public static Observable<List<MagazineList>> getMagazineList(String LCID) {
        //从网络中获取热点资讯数据更新
        return ApiManager.builder().createServiceFrom(ApiService.class)
                .getMagazineList(LCID).subscribeOn(Schedulers.io());
    }

}
