package com.cnpeak.expressreader.view.magazine.magazine_units;

import android.annotation.SuppressLint;

import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.model.bean.IssueList;
import com.cnpeak.expressreader.model.bean.IssueLite;
import com.cnpeak.expressreader.model.bean.MagazineList;
import com.cnpeak.expressreader.model.remote.IssueLiteModuleFactory;
import com.cnpeak.expressreader.utils.LogUtils;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author builder by HUANG JIN on 2018/11/13
 * @version 1.0
 * @description
 */
public class MagazineUnitPresenter extends BasePresenter<MagazineUnitView> {
    public static final String TAG = MagazineUnitPresenter.class.getSimpleName();
    private MagazineUnitView mView;

    MagazineUnitPresenter(MagazineUnitView magazineIssueView) {
        this.mView = magazineIssueView;
    }

    /**
     * 进入杂志详情首页，默认选择当前杂志最新一期杂志数据
     *
     * @param issueList MagazineList下的IssueListBean数据bean
     * @param index     当前IssueLite的位置索引
     */
    @SuppressLint("CheckResult")
    void getIssueLiteById(List<IssueList> issueList, int index) {
        Observable.fromIterable(issueList)
                .elementAt(index)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<IssueList>() {
                    @Override
                    public void accept(IssueList issueListBean) {
                        if (issueListBean != null) {
                            getIssueLite(issueListBean.getIssueId());
                        }
                    }
                });
    }

    /**
     * 获取单本杂志具体某一期的数据
     *
     * @param issueId 单期杂志的issueId
     */
    void getIssueLite(String issueId) {
        IssueLiteModuleFactory.getIssueLite(issueId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<IssueLite>() {
                    @Override
                    public void onNext(IssueLite issueLite) {
                        LogUtils.i(TAG, "getIssueLite onNext,the size >>>" + issueLite);
                        mView.getIssueLite(issueLite);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i(TAG, "getIssueLite onError,message>>>" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.i(TAG, "getIssueLite onComplete >>>");
                    }
                });

    }

}
