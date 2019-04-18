package com.cnpeak.expressreader.view.news.news_detail;

import android.util.Log;

import com.cnpeak.expressreader.analytics.AnalyticsAgent;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.dao.HotSpotDaoImpl;
import com.cnpeak.expressreader.model.db.SQLConstant;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * @author builder by HUANG JIN on ${date}
 * @version 1.0
 */
public class NewsDetailPresenter extends BasePresenter<NewsDetailView> {

    public static final String TAG = NewsDetailPresenter.class.getSimpleName();

    private NewsDetailView mView;

    NewsDetailPresenter(NewsDetailView iView) {
        this.mView = iView;
    }

    /**
     * 根据当前NID匹配数据库数据；
     * <p>
     * doAfterNext：返回列表为空，数据库不存在当前项，则插入一条阅读历史类型的数据（dbType = 0）
     * 返回列表不为空，则判断当前列表条目是否存在dbType = 0 的数据，不存在需要插入一条阅读历史类型的数据
     * <p>
     * 结果：返回列表为空，则回到favorFlag = 0;反之返回favorFlag = 1;
     */
    void queryTypeByNID(final HotSpot hotSpot) {
        HotSpotDaoImpl.queryNewsByNID(hotSpot.getNID())
                .doAfterNext(new Consumer<List<HotSpot>>() {
                    @Override
                    public void accept(List<HotSpot> hotSpots) {
                        if (hotSpots == null || hotSpots.size() == 0) {
                            Log.d(TAG, "doAfterNext: hotSpots.size ==null >>> ");
                            insert(hotSpot, SQLConstant.DB_TYPE_HISTORY);
                        } else {
                            Log.d(TAG, "doAfterNext: hotSpots.size >>> " + hotSpots.size());
                            if (!exists(SQLConstant.DB_TYPE_HISTORY, hotSpots)) {
                                insert(hotSpot, SQLConstant.DB_TYPE_HISTORY);
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HotSpot>>() {
                    @Override
                    public void onNext(List<HotSpot> hotSpots) {
                        if (hotSpots == null || hotSpots.size() == 0) {
                            Log.d(TAG, "onNext: 收藏数据不存在 >>> ");
                            mView.setFavorFlag(true, false, false);
                        } else {
                            if (exists(SQLConstant.DB_TYPE_FAVORITE, hotSpots)) {
                                Log.d(TAG, "onNext: 收藏数据存在 >>> ");
                                mView.setFavorFlag(true, true, false);
                            } else {
                                Log.d(TAG, "onNext: 收藏数据不存在 >>> ");
                                mView.setFavorFlag(true, false, false);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "queryTypeByMnId onError: >>> " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private boolean exists(int dbType, List<HotSpot> hotSpots) {
        for (HotSpot bean : hotSpots) {
            if (bean.getDbType() == dbType) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数据库映射插入操作
     *
     * @param hotSpot 数据bean
     * @param dbType  当前需要插入的数据库类型{0:历史记录，1：收藏记录}
     */
    public void insert(HotSpot hotSpot, final int dbType) {
        hotSpot.setDbType(dbType);//设置当前的插入类型为历史记录类型
        hotSpot.setCreateDate(System.currentTimeMillis());
        HotSpotDaoImpl.insert(hotSpot)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long rowId) {
                        if (dbType == SQLConstant.DB_TYPE_FAVORITE) {
                            Log.d(TAG, "当前收藏记录插入完毕 rowId >>>" + rowId);
                            mView.setFavorFlag(rowId > 0, true, true);
                        } else {
                            Log.d(TAG, "当前历史记录插入完毕 rowId >>>" + rowId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "insert onError: >>> " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 根据指定的数据类型和NID字段进行匹配删除
     *
     * @param NID    当前新闻数据的NID
     * @param dbType 当前数据的数据库类型
     */
    public void deleteNIDByType(String NID, final int dbType) {
        HotSpotDaoImpl.deleteNewsByNID(NID, dbType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer rowId) {
                        Log.d(TAG, "deleteNIDByType: rowId >>> " + rowId);
                        mView.setFavorFlag(rowId > 0, false, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "deleteNIDByType onError: >>> " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    void sentNewsClickEvent(String NID, String latitude, String longitude, String LCID) {
        //当前页面完全展示之后，向服务器提交一次统计
        AnalyticsAgent.builder().onNewsClickEvent(NID,
                latitude, longitude, "", LCID);
    }

}
