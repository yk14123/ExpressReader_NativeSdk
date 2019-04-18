package com.cnpeak.expressreader.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnpeak.expressreader.utils.LocaleHelper;

/**
 * @author builder by HUANG JIN on 18/11/8
 * @version 1.0
 * Fragment基类，负责处理相关调用逻辑
 */
public abstract class BaseFragment<V extends IView, P extends BasePresenter<V>> extends Fragment {
    private static final String TAG = "BaseFragment";
    //activity上下文环境
    protected Context mContext;
    //当前Fragment视图是否已经加载标志
    private boolean isViewPrepared;
    //当前Fragment是否已经完成过数据的加载加载工作
    private boolean hasFetchData;
    //界面内容布局
    protected View mContentView;
    //逻辑控制器
    private P mPresenter;
    //当前国家码
    protected String LCID;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //当前配置有数据时进行赋值,为空则为当前默认值（2052）
        LCID = LocaleHelper.getLCID(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (mContentView == null) {
            mContentView = inflater.inflate(getFragmentLayoutID(), container, false);
        }
        ViewGroup parent = (ViewGroup) mContentView.getParent();
        if (parent != null) {
            parent.removeView(mContentView);
        }

        initPresenter();

        findView();

        return mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //在视图被创建之后，标记当前视图已经完成初始化操作
        isViewPrepared = true;
        lazyFetchDataIfPrepared();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initPresenter() {
        mPresenter = createFragmentPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //触发数据加载的时机
        if (isVisibleToUser) {
            lazyFetchDataIfPrepared();
        }
    }

    private void lazyFetchDataIfPrepared() {
        // 用户可见fragment && 没有加载过数据 && 视图已经准备完毕
        if (getUserVisibleHint() && !hasFetchData && isViewPrepared) {
            hasFetchData = true; //已加载过数据
            lazyFetchData();
        }
    }

    /**
     * 数据懒加载形式
     */
    protected abstract void lazyFetchData();

    /**
     * Fragment内部的视图组件初始化
     */
    protected abstract void findView();

    /**
     * 获取布局id完成contentView初始化的工作
     */
    protected abstract int getFragmentLayoutID();

    /**
     * 初始化逻辑控制器工作
     */
    protected abstract P createFragmentPresenter();

    @Override
    public void onDestroyView() {
        //当前视图被销毁之后，标记当前视图可以重新被加载
        isViewPrepared = false;
        hasFetchData = false;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        //解绑
        if (mPresenter != null && mPresenter.isViewAttach()) {
            mPresenter.detachView();
        }
        mPresenter = null;
        super.onDestroy();
    }

}
