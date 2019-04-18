package com.cnpeak.expressreader.view.mine.locale;


import android.util.Log;

import com.cnpeak.expressreader.ErApplication;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.model.bean.LCID;
import com.cnpeak.expressreader.model.dao.HotSpotDaoImpl;
import com.cnpeak.expressreader.model.dao.IssueUnitDaoImpl;
import com.cnpeak.expressreader.model.dao.MagazineListDaoImpl;
import com.cnpeak.expressreader.model.dao.PaperCoverDaoImpl;
import com.cnpeak.expressreader.model.db.ErDatabase;
import com.cnpeak.expressreader.model.remote.LCIDModuleFactory;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

class LocalePresenter extends BasePresenter {
    private static final String TAG = "LocalePresenter";
    private LocaleView mView;

    LocalePresenter(LocaleView view) {
        this.mView = view;
    }

    /**
     * 获取语系
     */
    void getLCID() {
        LCIDModuleFactory.getLCID().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<LCID>>() {
                    @Override
                    public void onNext(List<LCID> lcidList) {
                        mView.getLCID(lcidList);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 切换语系之前，需要清除当前数据库中缓存的数据
     */
    void clearCache() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                ErDatabase erDatabase = ErApplication.getErDatabase();
                //清除本地新闻数据库数据
                erDatabase.hotSpotDao().deleteAll();
                //清除本地杂志单元数据库数据
                erDatabase.issueUnitDao().deleteAll();
                //清除本地杂志数据库数据
                erDatabase.magazineListDao().deleteAll();
                //清除本地报纸缓存数据
                erDatabase.paperCoverDao().deleteAll();
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.onLocaleConfigChanged(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "clearCache onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

}
