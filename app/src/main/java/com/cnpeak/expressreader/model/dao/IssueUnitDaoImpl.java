package com.cnpeak.expressreader.model.dao;


import com.cnpeak.expressreader.ErApplication;
import com.cnpeak.expressreader.model.bean.IssueUnit;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * The type Issue unit dao.
 */
public class IssueUnitDaoImpl {

    /**
     * 根据指定的MnID进行查询
     *
     * @param MnID the mn id
     * @return the observable
     */
    public static Observable<List<IssueUnit>> queryIssueByMnId(final String MnID) {
        return Observable.create(new ObservableOnSubscribe<List<IssueUnit>>() {
            @Override
            public void subscribe(ObservableEmitter<List<IssueUnit>> emitter) {
                List<IssueUnit> issueUnits = ErApplication.getErDatabase().issueUnitDao().queryIssueByMnId(MnID);
                emitter.onNext(issueUnits);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 插入一条杂志数据
     *
     * @param bean the bean
     * @return the observable
     */
    public static Observable<Long> insert(final IssueUnit bean) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) {
                long rowId = ErApplication.getErDatabase().issueUnitDao().insert(bean);
                emitter.onNext(rowId);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 根据指定的MnID进行删除
     *
     * @param MnId   the mn id
     * @param dbType the db type
     * @return the observable
     */
    public static Observable<Integer> deleteIssueByMnId(final String MnId, final int dbType) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                int rowId = ErApplication.getErDatabase().issueUnitDao().deleteIssueByMnId(MnId, dbType);
                emitter.onNext(rowId);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 根删除指定的杂志列表集合进行删除
     *
     * @param issueUnits the issue units
     * @param dbType     the db type
     * @return the observable
     */
    public static Observable<Boolean> deleteList(final List<IssueUnit> issueUnits, final int dbType) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                for (IssueUnit bean : issueUnits) {
                    ErApplication.getErDatabase().issueUnitDao().deleteIssueByMnId(bean.getMnId(), dbType);
                }
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 根据数据库的存储类型进行删除
     *
     * @param dbType 是否为收藏数据
     * @return the observable
     */
    public static Observable<Boolean> deleteAll(final int dbType) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                int rowId = ErApplication.getErDatabase().issueUnitDao().deleteIssueUnitByType(dbType);
                emitter.onNext(rowId >= 0);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 清除本地所有的杂志数据
     *
     * @return the observable
     */
    public static Observable<Boolean> deleteAll() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                int rowId = ErApplication.getErDatabase().issueUnitDao().deleteAll();
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 根据指定的数据存储类型进行查询
     *
     * @param dbType the db type
     * @return the observable
     */
    public static Observable<List<IssueUnit>> queryIssueUnitByType(final int dbType) {
        return Observable.create(new ObservableOnSubscribe<List<IssueUnit>>() {
            @Override
            public void subscribe(ObservableEmitter<List<IssueUnit>> emitter) {
                List<IssueUnit> hotSpots = ErApplication.getErDatabase().issueUnitDao().queryIssueUnitByType(dbType);
                emitter.onNext(hotSpots);
            }
        }).subscribeOn(Schedulers.io());
    }

}
