package com.cnpeak.expressreader.model.bean;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.io.Serializable;

@Entity
@ForeignKey(entity = MagazineList.class, parentColumns = {"BookCase"}, childColumns = {"IssueId"})
public class IssueList implements Serializable {
    /**
     * IssueId : C0120180901N6709
     * IssueNo : 6709
     * PublishDate : 2018-08-31
     * Cover : https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180901N6709/jpeg/001.jpg
     */
    @PrimaryKey
    @NonNull
    private String IssueId;
    private String IssueNo;
    private String PublishDate;
    private String Cover;

    public String getIssueId() {
        return IssueId;
    }

    public void setIssueId(String IssueId) {
        this.IssueId = IssueId;
    }

    public String getIssueNo() {
        return IssueNo;
    }

    public void setIssueNo(String IssueNo) {
        this.IssueNo = IssueNo;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String PublishDate) {
        this.PublishDate = PublishDate;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String Cover) {
        this.Cover = Cover;
    }
}
