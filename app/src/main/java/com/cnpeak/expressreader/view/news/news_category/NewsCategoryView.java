package com.cnpeak.expressreader.view.news.news_category;

import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.model.bean.HotSpot;

import java.util.List;

/**
 * @author builder by HUANG JIN on 18/11/9
 * @version 1.0
 * @description
 */
public interface NewsCategoryView extends IView {

    //回传列表分类数据
    void getCategoryNews(List<HotSpot> categories);

    //回传新闻资讯数据
    void getNewsList(List<HotSpot> newsList);

    void onError(String errorMsg);

}
