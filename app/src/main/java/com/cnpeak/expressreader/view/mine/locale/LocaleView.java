package com.cnpeak.expressreader.view.mine.locale;

import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.model.bean.LCID;
import java.util.List;

public interface LocaleView extends IView {

    void getLCID(List<LCID> lcidList);

    void onLocaleConfigChanged(boolean result);
}
