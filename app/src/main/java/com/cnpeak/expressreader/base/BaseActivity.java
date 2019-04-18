package com.cnpeak.expressreader.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cnpeak.expressreader.utils.LocaleHelper;
import com.cnpeak.expressreader.utils.LogUtils;

/**
 * The type Base activity.
 *
 * @param <V> IView接口实现范型
 * @param <P> BasePresenter实现接口范型
 * @author builder by PAYNE on 2018/11/1
 * @version 1.0
 * Activity基类
 */
public abstract class BaseActivity<V extends IView, P extends BasePresenter<V>> extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    /**
     * The M context.
     */
    protected Context mContext;
    /**
     * The M presenter.
     */
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(getActivityLayoutId());

        getExtrasFromIntent();

        getPresenter();

        initView();
    }

    private void getPresenter() {
        mPresenter = initPresenter();
        mPresenter.attachView((V) this);
    }

    private void getExtrasFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            getExtraFromBundle(intent);
        }
    }

    /**
     * Activity内部视图组件初始化
     */
    protected abstract void initView();

    /**
     * 设置Activity内部的Layout资源ID
     *
     * @return the activity layout id
     */
    protected abstract int getActivityLayoutId();

    /**
     * 初始化Presenter对象，创建具体的子类控制单元
     *
     * @return P BasePresenter子类对象
     */
    protected abstract P initPresenter();

    /**
     * 获取Intent Bundles中的传递参数
     * 考虑到一些Activity无需传递相关的参数，所以此方法不必声明为abstract
     *
     * @param intent the intent
     */
    protected void getExtraFromBundle(Intent intent) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        LogUtils.d(TAG, "attachBaseContext: >>>");
        super.attachBaseContext(LocaleHelper.attachBaseContext(newBase));
    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroy();
    }
}
