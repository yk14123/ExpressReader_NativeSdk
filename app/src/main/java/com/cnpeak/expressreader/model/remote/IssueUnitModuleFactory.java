package com.cnpeak.expressreader.model.remote;

import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.model.bean.IssueUnit;
import com.cnpeak.expressreader.net.ApiService;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author builder by HUANG JIN on ${date}
 * @version 1.0
 * @description 章节内容bean
 */
public class IssueUnitModuleFactory {
    /**
     * 取得章節內容
     *
     * @param MnID MnID
     * @return 根据指定的MnID获取杂志数据列表
     */
    public static Observable<IssueUnit> getIssueUnit(String MnID) {
        //从网络中获取热点资讯数据更新
        return ApiManager.builder().createServiceFrom(ApiService.class)
                .getIssueUnit(MnID).subscribeOn(Schedulers.io());
    }

}
