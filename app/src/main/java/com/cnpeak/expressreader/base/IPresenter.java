package com.cnpeak.expressreader.base;

/**
 * @author builder by HUANG JIN on 2018/11/1
 * @version 1.0
 * @description P层接口
 */
public interface IPresenter<V extends IView> {

    /**
     * 绑定视图View对象
     *
     * @param view 绑定的视图对象
     */
    void attachView(V view);

    /**
     * 解绑View视图对象
     */
    void detachView();
}
