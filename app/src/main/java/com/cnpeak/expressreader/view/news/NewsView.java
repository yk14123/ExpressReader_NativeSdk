package com.cnpeak.expressreader.view.news;

import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.model.bean.Category;
import com.cnpeak.expressreader.model.bean.HotSpot;
import java.util.List;

/**
 * @author builder by HUANG JIN on 18/11/9
 * @version 1.0
 * @description
 */
public interface NewsView extends IView {

    //回传列表分类数据
    void getCategory(List<Category> categories);

    //回传banner列表数据
    void getStickTop(List<HotSpot> banners);

}
