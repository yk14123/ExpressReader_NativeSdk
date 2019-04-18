package com.cnpeak.expressreader.view.mine.local;

import android.util.Log;

import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.bean.IssueUnit;
import com.cnpeak.expressreader.model.dao.HotSpotDaoImpl;
import com.cnpeak.expressreader.model.dao.IssueUnitDaoImpl;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;


class LocalHistoryPresenter extends BasePresenter<LocalHistoryView> {
    private static final String TAG = "LocalHistoryPresenter";
    private LocalHistoryView mView;

    LocalHistoryPresenter(LocalHistoryView mView) {
        this.mView = mView;
    }

    /**
     * 从数据库中获取资讯列表数据
     */
    void getLocalListByType(int dbType, int dataType) {
        switch (dataType) {
            case ErConstant.DATA_TYPE_HOTSPOT:
                queryNewsByType(dbType);
                break;
            case ErConstant.DATA_TYPE_ISSUE:
                queryIssueUnitByType(dbType);
                break;
        }
    }


    private void queryNewsByType(int dbType) {
        Log.d(TAG, "queryNewsByType: dbType >>> " + dbType);
        HotSpotDaoImpl.queryNewsByType(dbType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HotSpot>>() {
                    @Override
                    public void onNext(List<HotSpot> hotSpots) {
                        mView.getHotspotFromLocal(hotSpots);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "queryNewsByType onError: message >>> " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void queryIssueUnitByType(int dbType) {
        Log.d(TAG, "queryIssueUnitByType: dbType >>> " + dbType);
        IssueUnitDaoImpl.queryIssueUnitByType(dbType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<IssueUnit>>() {
                    @Override
                    public void onNext(List<IssueUnit> issueUnits) {
                        mView.getIssueListFromLocal(issueUnits);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "queryIssueUnitByType onError: message >>> " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    void deleteAllByType(final int dataType, final int dbType) {
        switch (dataType) {
            case ErConstant.DATA_TYPE_HOTSPOT:
                deleteNewsListByType(dbType);
                break;
            case ErConstant.DATA_TYPE_ISSUE:
                deleteIssueListByType(dbType);
                break;

        }
    }

    private void deleteNewsListByType(int dbType) {
        HotSpotDaoImpl.deleteNewsByType(dbType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean result) {
                        Log.d(TAG, "deleteNewsListByType onNext: result >>> " + result);
                        mView.setState(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "deleteNewsListByType onError: message >>> " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void deleteIssueListByType(int dbType) {
        IssueUnitDaoImpl.deleteAll(dbType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean result) {
                        Log.d(TAG, "deleteIssueListByType onNext: result >>> " + result);
                        mView.setState(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "deleteIssueListByType onError: message >>> " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void deleteNewsListByType(final int dbType, final List<HotSpot> newsList) {
        HotSpotDaoImpl.deleteNewsListByNID(newsList, dbType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean result) {
                        Log.d(TAG, "deleteNewsListByType onNext: result >>> " + result);
                        mView.setState(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: message >>> " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    void deleteIssueListByType(final int dbType, final List<IssueUnit> issueList) {
        IssueUnitDaoImpl.deleteList(issueList, dbType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean result) {
                        mView.setState(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: message >>> " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
