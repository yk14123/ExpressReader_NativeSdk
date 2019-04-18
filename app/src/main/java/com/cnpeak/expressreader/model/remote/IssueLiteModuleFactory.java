package com.cnpeak.expressreader.model.remote;

import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.model.bean.IssueLite;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author builder by HUANG JIN on 18/11/8
 * @version 1.0
 * 获取IssueLite单本杂志数据实体
 */
public class IssueLiteModuleFactory {
    /**
     * 获取热点资讯
     *
     * @param issueId issueId
     * @return 根据指定的LCID国家码获取杂志数据列表
     */
    public static Observable<IssueLite> getIssueLite(String issueId) {
        //从网络中获取热点资讯数据更新
        return ApiManager.builder().getService().getIssueLite(issueId).subscribeOn(Schedulers.io());
    }

}
