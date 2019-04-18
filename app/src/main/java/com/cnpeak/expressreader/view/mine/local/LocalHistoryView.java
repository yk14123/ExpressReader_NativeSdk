package com.cnpeak.expressreader.view.mine.local;

import com.cnpeak.expressreader.base.ILoadingView;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.bean.IssueUnit;

import java.util.List;

public interface LocalHistoryView extends ILoadingView {

    // 从本地数据库中获取收藏的热点新闻资讯列表
    void getHotspotFromLocal(List<HotSpot> localList);

    // 从本地数据库中获取收藏的热点新闻资讯列表
    void getIssueListFromLocal(List<IssueUnit> localList);

    //更新UI状态
    void setState(boolean result);

}
