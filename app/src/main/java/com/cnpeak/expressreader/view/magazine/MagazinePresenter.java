package com.cnpeak.expressreader.view.magazine;

import android.util.Log;

import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.model.bean.MagazineList;
import com.cnpeak.expressreader.model.dao.MagazineListDaoImpl;
import com.cnpeak.expressreader.model.remote.MagazineListModuleFactory;
import com.cnpeak.expressreader.view.news.NewsView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * @author builder by HUANG JIN on ${date}
 * @version 1.0
 * @description
 */
public class MagazinePresenter extends BasePresenter<NewsView> {
    public static final String TAG = MagazinePresenter.class.getSimpleName();
    private MagazineView mView;

    MagazinePresenter(MagazineView magazineView) {
        this.mView = magazineView;
    }

    /**
     * 获取首页热点资讯
     *
     * @param LCID 国家码
     */
    void getMagazineList(final String LCID) {
        MagazineListDaoImpl.queryByOrderIndex()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<MagazineList>>() {
                    @Override
                    public void onNext(List<MagazineList> paperCovers) {
                        if (paperCovers != null && paperCovers.size() != 0) {
                            Log.i(TAG, "getMagazineList from local,the size >>>" + paperCovers.size());
                            mView.getMagazineList(paperCovers);
                        } else {
                            getMagazineListFromRemote(LCID);
                        }
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

    private void getMagazineListFromRemote(String LCID) {
        MagazineListModuleFactory.getMagazineList(LCID)
                //在通过网络请求完成之后，将当前原始数据排序存入到数据库中
                .doAfterNext(new Consumer<List<MagazineList>>() {
                    @Override
                    public void accept(List<MagazineList> magazineList) {
                        if (magazineList != null && magazineList.size() != 0) {
                            insertAll(magazineList);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<MagazineList>>() {
                    @Override
                    public void onNext(List<MagazineList> paperCovers) {
                        mView.getMagazineList(paperCovers);
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

    private void insertAll(List<MagazineList> paperCovers) {
        MagazineListDaoImpl.insertAll(paperCovers)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<long[]>() {
                    @Override
                    public void onNext(long[] aLong) {
                        Log.d(TAG, "onNext: insertAll aLong >>> " + aLong.length);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: insertAll >>> " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
