package com.cnpeak.expressreader.view.magazine.magazine_detail;


import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.model.bean.IssueUnit;

public interface MagazineDetailView extends IView {

    void getIssueUnit(IssueUnit issueUnit);

    void setFavorFlag(boolean favorFlag, boolean result, boolean showToast);

    void setHistoryFlag(boolean historyFlag);

}
