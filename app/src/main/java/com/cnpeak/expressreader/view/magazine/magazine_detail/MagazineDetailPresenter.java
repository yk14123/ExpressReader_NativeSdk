package com.cnpeak.expressreader.view.magazine.magazine_detail;

import android.util.Log;

import com.cnpeak.expressreader.analytics.AnalyticsAgent;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.model.bean.IssueUnit;
import com.cnpeak.expressreader.model.dao.IssueUnitDaoImpl;
import com.cnpeak.expressreader.model.db.SQLConstant;
import com.cnpeak.expressreader.model.remote.IssueUnitModuleFactory;
import com.cnpeak.expressreader.utils.LocaleHelper;
import com.cnpeak.expressreader.utils.LogUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MagazineDetailPresenter extends BasePresenter<MagazineDetailView> {
    public static final String TAG = MagazineDetailPresenter.class.getSimpleName();
    private MagazineDetailView mView;

    MagazineDetailPresenter(MagazineDetailView mView) {
        this.mView = mView;
    }

    /**
     * 获取单本杂志数据
     *
     * @param MnId 当前杂志的MnId
     */
    void getIssueUnit(String MnId) {
        IssueUnitModuleFactory.getIssueUnit(MnId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<IssueUnit>() {
                    @Override
                    public void onNext(IssueUnit issueUnit) {
                        mView.getIssueUnit(issueUnit);
                        LogUtils.i(TAG, "getIssueUnit onNext,issueUnit >>>" + issueUnit.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i(TAG, "getIssueUnit onError,message>>>" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    /**
     * 根据当前NID匹配数据库数据；
     * <p>
     * doAfterNext：返回列表为空，数据库不存在当前项，则插入一条阅读历史类型的数据（dbType = 0）
     * 返回列表不为空，则判断当前列表条目是否存在dbType = 0 的数据，不存在需要插入一条阅读历史类型的数据
     * <p>
     * 结果：返回列表为空，则回到favorFlag = 0;反之返回favorFlag = 1;
     */
    public void queryTypeByMnId(final String MnId) {
        IssueUnitDaoImpl.queryIssueByMnId(MnId)
                .doAfterNext(new Consumer<List<IssueUnit>>() {
                    @Override
                    public void accept(List<IssueUnit> list) {
                        if (list == null || list.size() == 0) {
                            Log.d(TAG, "doAfterNext: list.size ==null >>> ");
//                            insert(issueUnit, SQLConstant.DB_TYPE_HISTORY);
                            mView.setHistoryFlag(false);
                        } else {
                            Log.d(TAG, "doAfterNext: list.size >>> " + list.size());
//                            if (!exists(SQLConstant.DB_TYPE_HISTORY, list)) {
//                                mView.setHistoryFlag(false);
//                            }else
                            mView.setHistoryFlag(exists(SQLConstant.DB_TYPE_HISTORY, list));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<IssueUnit>>() {
                    @Override
                    public void onNext(List<IssueUnit> hotSpots) {
                        if (hotSpots == null || hotSpots.size() == 0) {
                            Log.d(TAG, "onNext: 收藏数据不存在 >>> ");
                            mView.setFavorFlag(false, true, false);
                        } else {
                            if (exists(SQLConstant.DB_TYPE_FAVORITE, hotSpots)) {
                                Log.d(TAG, "onNext: 收藏数据存在 >>> ");
                                mView.setFavorFlag(true, true, false);
                            } else {
                                Log.d(TAG, "onNext: 收藏数据不存在 >>> ");
                                mView.setFavorFlag(false, true, false);
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

    private boolean exists(int dbType, List<IssueUnit> issueUnits) {
        for (IssueUnit bean : issueUnits) {
            if (bean.getDbType() == dbType) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数据库映射插入操作
     *
     * @param issueUnit 数据bean
     * @param dbType    当前需要插入的数据库类型{0:历史记录，1：收藏记录}
     */
    public void insert(IssueUnit issueUnit, final int dbType) {
        issueUnit.setDbType(dbType);//设置当前的插入类型为历史记录类型
        issueUnit.setCreateDate(System.currentTimeMillis());
        IssueUnitDaoImpl.insert(issueUnit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long rowId) {
                        if (dbType == SQLConstant.DB_TYPE_FAVORITE) {
                            Log.d(TAG, "当前收藏记录插入完毕 rowId >>>" + rowId);
                            mView.setFavorFlag(true, rowId > 0, true);
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
     * @param MnId   当前新闻数据的NID
     * @param dbType 当前数据的数据库类型
     */
    public void deleteNIDByType(String MnId, final int dbType) {
        IssueUnitDaoImpl.deleteIssueByMnId(MnId, dbType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer rowId) {
                        Log.d(TAG, "deleteNIDByType: rowId >>> " + rowId);
                        mView.setFavorFlag(false, rowId > 0, true);
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

    void sentIssueUnitClickEvent(String MnId, String latitude, String longitude, String LCID) {
        //当前页面完全展示之后，向服务器提交一次统计
        AnalyticsAgent.builder().onIssueUnitClickEvent(MnId,
                latitude, longitude, LCID);
    }

}
