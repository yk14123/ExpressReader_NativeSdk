package com.cnpeak.expressreader.model.dao;

import com.cnpeak.expressreader.ErApplication;
import com.cnpeak.expressreader.model.bean.HotSpot;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * The type Hot spot dao.
 */
public class HotSpotDaoImpl {

    /**
     * 当前指定新闻id数据
     *
     * @param NID the nid
     * @return the observable
     */
    public static Observable<List<HotSpot>> queryNewsByNID(final String NID) {
        return Observable.create(new ObservableOnSubscribe<List<HotSpot>>() {
            @Override
            public void subscribe(ObservableEmitter<List<HotSpot>> emitter) {
                List<HotSpot> hotSpots = ErApplication.getErDatabase().hotSpotDao().queryNewsByNID(NID);
                emitter.onNext(hotSpots);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 插入操作
     *
     * @param bean the bean
     * @return the observable
     */
    public static Observable<Long> insert(final HotSpot bean) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) {
                long rowId = ErApplication.getErDatabase().hotSpotDao().insert(bean);
                emitter.onNext(rowId);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 根据指定的NID删除
     *
     * @param NID    the nid
     * @param dbType the db type
     * @return the observable
     */
    public static Observable<Integer> deleteNewsByNID(final String NID, final int dbType) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                int rowId = ErApplication.getErDatabase().hotSpotDao().deleteNewsByNID(NID, dbType);
                emitter.onNext(rowId);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 删除指定的本地新闻列表集合
     *
     * @param hotSpots the hot spots
     * @param dbType   the db type
     * @return the observable
     */
    public static Observable<Boolean> deleteNewsListByNID(final List<HotSpot> hotSpots, final int dbType) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                for (HotSpot bean : hotSpots) {
                    ErApplication.getErDatabase().hotSpotDao().deleteNewsByNID(bean.getNID(), dbType);
                }
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 根据数据存储类型进行删除
     *
     * @param dbType 更新标志，是否为更新收藏列表数据
     * @return the observable
     */
    public static Observable<Boolean> deleteNewsByType(final int dbType) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                int rowId = ErApplication.getErDatabase().hotSpotDao().deleteNewsByType(dbType);
                emitter.onNext(rowId >= 0);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 清除所有本地新闻数据
     *
     * @return the observable
     */
    public static Observable<Boolean> deleteAll() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                ErApplication.getErDatabase().hotSpotDao().deleteAll();
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 查询所有
     *
     * @param dbType the db type
     * @return the observable
     */
    public static Observable<List<HotSpot>> queryNewsByType(final int dbType) {
        return Observable.create(new ObservableOnSubscribe<List<HotSpot>>() {
            @Override
            public void subscribe(ObservableEmitter<List<HotSpot>> emitter) {
                List<HotSpot> hotSpots = ErApplication.getErDatabase().hotSpotDao().queryNewsByType(dbType);
                emitter.onNext(hotSpots);
            }
        }).subscribeOn(Schedulers.io());
    }

}
