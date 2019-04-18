package com.cnpeak.expressreader.view.newspaper;

import android.util.Log;

import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.model.bean.PaperCover;
import com.cnpeak.expressreader.model.dao.PaperCoverDaoImpl;
import com.cnpeak.expressreader.model.remote.PaperCoverModuleFactory;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * @author builder by HUANG JIN on ${date}
 * @version 1.0
 * @description
 */
public class NewspaperPresenter extends BasePresenter<NewspaperView> {
    public static final String TAG = NewspaperPresenter.class.getSimpleName();
    private NewspaperView mView;

    NewspaperPresenter(NewspaperView newspaperView) {
        this.mView = newspaperView;
    }

    /**
     * 获取报纸cover列表
     *
     * @param LCID 国家码
     */
    void getPaperCovers(final String LCID) {
        PaperCoverDaoImpl.queryByOrderIndex()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<PaperCover>>() {
                    @Override
                    public void onNext(List<PaperCover> paperCovers) {
                        if (paperCovers != null && paperCovers.size() != 0) {
                            mView.getPaperCover(paperCovers);
                        } else {
                            getPaperCoversFromRemote(LCID);
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

    private void getPaperCoversFromRemote(String LCID) {
        PaperCoverModuleFactory.getPagerCover(LCID)
                //在通过网络请求完成之后，将当前原始数据排序存入到数据库中
                .doAfterNext(new Consumer<List<PaperCover>>() {
                    @Override
                    public void accept(List<PaperCover> paperCovers) {
                        if (paperCovers != null && paperCovers.size() != 0) {
                            insertAll(paperCovers);
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<PaperCover>>() {
                    @Override
                    public void onNext(List<PaperCover> paperCovers) {
                        mView.getPaperCover(paperCovers);
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

    private void insertAll(List<PaperCover> paperCovers) {
        PaperCoverDaoImpl.insertAll(paperCovers)
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
