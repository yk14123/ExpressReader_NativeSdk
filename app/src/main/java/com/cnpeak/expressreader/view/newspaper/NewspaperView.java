package com.cnpeak.expressreader.view.newspaper;

import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.model.bean.PaperCover;

import java.util.List;

/**
 * @author builder by HUANG JIN on 18/11/9
 * @version 1.0
 * @description
 */
public interface NewspaperView extends IView {

    void getPaperCover(List<PaperCover> paperCovers);

    void onError(String errorMsg);

}
