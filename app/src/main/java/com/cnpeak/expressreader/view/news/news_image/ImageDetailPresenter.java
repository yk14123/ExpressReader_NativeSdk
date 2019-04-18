package com.cnpeak.expressreader.view.news.news_image;

import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.base.IView;

/**
 * @author builder by HUANG JIN on ${date}
 * @version 1.0
 * @description
 */
public class ImageDetailPresenter extends BasePresenter<IView> {
    public static final String TAG = ImageDetailPresenter.class.getSimpleName();
    private IView mIView;

    public ImageDetailPresenter(IView iView) {
        this.mIView = iView;
    }


}
