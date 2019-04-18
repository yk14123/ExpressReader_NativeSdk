package com.cnpeak.expressreader.view.news;


import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.model.bean.Category;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.remote.CategoryModuleFactory;
import com.cnpeak.expressreader.model.remote.StickTopModuleFactory;
import com.cnpeak.expressreader.utils.LogUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

/**
 * @author builder by HUANG JIN
 * @version 1.0
 */
public class NewsPresenter extends BasePresenter<NewsView> {
    public static final String TAG = NewsPresenter.class.getSimpleName();
    private NewsView mNewsView;

    NewsPresenter(NewsView newsView) {
        this.mNewsView = newsView;
    }

    /**
     * 获取首页资讯标签数据
     *
     * @param LCID 国家码
     */
    public void getCategory(String LCID) {
        CategoryModuleFactory.getCategory(LCID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Category>>() {
                    @Override
                    public void onNext(List<Category> categories) {
                        mNewsView.getCategory(categories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i(TAG, "onError message >>>" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    /**
     * 获取首页banner数据
     *
     * @param LCID 国家码
     */
    void getStickTop(String LCID) {
        StickTopModuleFactory.getStickTop(LCID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HotSpot>>() {
                    @Override
                    public void onNext(List<HotSpot> banners) {
                        mNewsView.getStickTop(banners);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i(TAG, "onError message >>>" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

}
