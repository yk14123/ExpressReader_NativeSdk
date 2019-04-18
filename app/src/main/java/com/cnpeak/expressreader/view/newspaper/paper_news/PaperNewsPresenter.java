package com.cnpeak.expressreader.view.newspaper.paper_news;

import android.util.Log;

import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.remote.NewsPaperModuleFactory;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

/**
 * @author builder by HUANG JIN on 18/05
 * @version 1.0
 * @description 报纸数据列表控制器
 */
class PaperNewsPresenter extends BasePresenter<IView> {
    private static final String TAG = "PaperNewsPresenter";
    private PaperNewsView mView;

    //构造绑定View
    PaperNewsPresenter(PaperNewsView view) {
        mView = view;
    }

    void getNewsPaper(String LCID, String newsId, int pageIndex) {
        NewsPaperModuleFactory.getNewsPaper(LCID, newsId, pageIndex, 10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HotSpot>>() {
                    @Override
                    public void onNext(List<HotSpot> newsPapers) {
                        mView.getNewsPaper(newsPapers);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: >>>");
                    }
                });
    }


}
