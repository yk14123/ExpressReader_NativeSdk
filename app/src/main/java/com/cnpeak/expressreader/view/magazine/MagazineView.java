package com.cnpeak.expressreader.view.magazine;

import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.model.bean.MagazineList;

import java.util.List;

/**
 * @author builder by HUANG JIN on 18/11/9
 * @version 1.0
 * @description
 */
public interface MagazineView extends IView {

    void getMagazineList(List<MagazineList> magazineList);

    void onError(String errorMsg);

}
