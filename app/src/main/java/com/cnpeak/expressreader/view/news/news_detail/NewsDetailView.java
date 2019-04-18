package com.cnpeak.expressreader.view.news.news_detail;

import com.cnpeak.expressreader.base.IView;

public interface NewsDetailView extends IView {

    void onFavorStateChanged(boolean result, boolean favorFlag, boolean showToast);

}
