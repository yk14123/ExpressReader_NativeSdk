package com.cnpeak.expressreader.base;

public interface ILoadingView extends IView {
    /**
     * 显示loading视图
     */
    void showLoading();


    //关闭loading视图
    void hideLoading();

    //显示吐司信息
//    void showToast();

    //显示错误信息、或者错误界面视图
//    void showErrorView();

}
