package com.cnpeak.expressreader.view.newspaper.paper_news;

import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.model.bean.HotSpot;

import java.util.List;

/**
 * @author builder by HUANG JIN on 18/11/9
 * @version 1.0
 * @description
 */
public interface PaperNewsView extends IView {

    void getNewsPaper(List<HotSpot> newsPapers);

    void onError(String errorMsg);

}
