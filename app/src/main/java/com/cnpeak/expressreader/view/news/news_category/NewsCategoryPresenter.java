package com.cnpeak.expressreader.view.news.news_category;

import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.remote.HotSpotCategoryNewsFactory;
import com.cnpeak.expressreader.model.remote.HotSpotModuleFactory;
import com.cnpeak.expressreader.utils.LogUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

/**
 * @author builder by HUANG JIN on ${date}
 * @version 1.0
 * @description
 */
public class NewsCategoryPresenter extends BasePresenter<NewsCategoryView> {
    public static final String TAG = NewsCategoryPresenter.class.getSimpleName();
    private NewsCategoryView mView;

    NewsCategoryPresenter(NewsCategoryView hotSpotView) {
        this.mView = hotSpotView;
    }

    /**
     * 获取首页资讯标签新闻数据
     *
     * @param LCID      国家码
     * @param channelId 渠道id
     * @param newsId    新闻id
     * @param keyword   关键词
     * @param pageIndex 当前页码
     */
    void getCategoryNews(String LCID, String newsId, String channelId,
                         String keyword, int pageIndex) {
        HotSpotCategoryNewsFactory.getCategoryNews(LCID, newsId, channelId, keyword, pageIndex, 10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HotSpot>>() {
                    @Override
                    public void onNext(List<HotSpot> categoryNews) {
                        mView.getCategoryNews(categoryNews);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    /**
     * 获取首页热点资讯
     *
     * @param LCID      国家码
     * @param pageIndex 页面索引
     */
    void getNewsList(String LCID, int pageIndex) {
        HotSpotModuleFactory.getHotSpotList(LCID, pageIndex, 10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HotSpot>>() {
                    @Override
                    public void onNext(List<HotSpot> hotSpot) {
                        mView.getNewsList(hotSpot);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }


}
