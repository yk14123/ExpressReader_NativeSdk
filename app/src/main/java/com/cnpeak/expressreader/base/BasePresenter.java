package com.cnpeak.expressreader.base;

import java.lang.ref.WeakReference;

/**
 * @author builder by PAYNE on 2018/11/1
 * @version 1.0
 * PRESENTER基类, 当前项目业务量部大的情况下，可以将model业务逻辑放在presenter子类内部去实现
 */
public class BasePresenter<V extends IView> implements IPresenter<V> {
    private WeakReference<V> mViewRefer;


    /**
     * 检查view对象是否为空
     */
    public boolean isViewAttach() {
        return mViewRefer != null && mViewRefer.get() != null;
    }

    @Override
    public void attachView(V view) {
        this.mViewRefer = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        if (mViewRefer != null) {
            mViewRefer.clear();
            mViewRefer = null;
        }
    }
}