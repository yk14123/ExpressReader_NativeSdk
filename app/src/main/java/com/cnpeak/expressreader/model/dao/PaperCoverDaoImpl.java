package com.cnpeak.expressreader.model.dao;

import com.cnpeak.expressreader.ErApplication;
import com.cnpeak.expressreader.model.bean.PaperCover;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * PaperCover数据库操作业务类
 */
public class PaperCoverDaoImpl {

    /**
     * Query by order index observable.
     *
     * @return the observable
     */
    public static Observable<List<PaperCover>> queryByOrderIndex() {
        return Observable.create(new ObservableOnSubscribe<List<PaperCover>>() {
            @Override
            public void subscribe(ObservableEmitter<List<PaperCover>> emitter) {
                List<PaperCover> paperCovers = ErApplication.getErDatabase()
                        .paperCoverDao().queryByOrderIndex();
                emitter.onNext(paperCovers);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Insert 插入单条数据.
     *
     * @param bean the bean
     * @return the observable
     */
    public static Observable<Long> insert(final PaperCover bean) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) {
                long rowId = ErApplication.getErDatabase().paperCoverDao().insert(bean);
                emitter.onNext(rowId);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Insert 插入单条数据.
     *
     * @param bean the bean
     * @return the observable
     */
    public static Observable<Long> update(final PaperCover bean) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) {
                long rowId = ErApplication.getErDatabase().paperCoverDao().update(bean);
                emitter.onNext(rowId);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Insert 插入单条数据.
     *
     * @param bean the bean
     * @return the observable
     */
    public static Observable<List<PaperCover>> updateFavorState(final PaperCover bean) {
        return Observable.create(new ObservableOnSubscribe<List<PaperCover>>() {
            @Override
            public void subscribe(ObservableEmitter<List<PaperCover>> emitter) {
                int rowId = ErApplication.getErDatabase().paperCoverDao().update(bean);
                if (rowId > 0) {
                    List<PaperCover> paperCovers = ErApplication.getErDatabase()
                            .paperCoverDao().queryByOrderIndex();
                    emitter.onNext(paperCovers);
                }
            }
        }).subscribeOn(Schedulers.io());
    }


    /**
     * Insert all 插入数据列表.
     *
     * @param paperCovers the paper covers
     * @return the observable
     */
    public static Observable<long[]> insertAll(final List<PaperCover> paperCovers) {
        return Observable.create(new ObservableOnSubscribe<long[]>() {
            @Override
            public void subscribe(ObservableEmitter<long[]> emitter) {
                long[] rowId = ErApplication.getErDatabase().paperCoverDao()
                        .insertAll(paperCovers);
                emitter.onNext(rowId);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Insert all 插入数据列表.
     *
     * @return the observable
     */
    public static Observable<Boolean> deleteAll() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                int rowId = ErApplication.getErDatabase()
                        .paperCoverDao().deleteAll();
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.io());
    }

//    /**
//     * Query all observable.
//     *
//     * @return the observable
//     */
//    public static Observable<List<PaperCover>> queryAll() {
//        return Observable.builder(new ObservableOnSubscribe<List<PaperCover>>() {
//            @Override
//            public void subscribe(ObservableEmitter<List<PaperCover>> emitter) {
//                List<PaperCover> paperCovers = ErApplication.getErDatabase()
//                        .paperCoverDao().queryAll();
//                emitter.onNext(paperCovers);
//            }
//        }).subscribeOn(Schedulers.io());
//    }
}
