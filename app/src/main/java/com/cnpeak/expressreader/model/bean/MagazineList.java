package com.cnpeak.expressreader.model.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * @author builder by HUANG JIN on 2018/11/6
 * @version 1.0
 * 杂志列表bean
 */
@Entity
public class MagazineList implements Serializable {

    /**
     * MgName : 今日中国
     * BookCase : C01
     * IssuerName : 今日中国杂志社
     * BrandOpen : true
     * IssueList : [{"IssueId":"C0120180901N6709","IssueNo":"6709","PublishDate":"2018-08-31","Cover":"https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180901N6709/jpeg/001.jpg"},{"IssueId":"C0120180801N6708","IssueNo":"6708","PublishDate":"2018-07-31","Cover":"https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180801N6708/jpeg/001.jpg"},{"IssueId":"C0120180701N6707","IssueNo":"6707","PublishDate":"2018-06-30","Cover":"https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180701N6707/jpeg/001.jpg"},{"IssueId":"C0120180601N6706","IssueNo":"6706","PublishDate":"2018-05-31","Cover":"https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180601N6706/jpeg/001.jpg"},{"IssueId":"C0120180501N6705","IssueNo":"6705","PublishDate":"2018-04-30","Cover":"https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180501N6705/jpeg/001.jpg"},{"IssueId":"C0120180401N6704","IssueNo":"6704","PublishDate":"2018-03-31","Cover":"https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180401N6704/jpeg/001.jpg"},{"IssueId":"C0120180301N6703","IssueNo":"6703","PublishDate":"2018-02-28","Cover":"https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180301N6703/jpeg/001.jpg"},{"IssueId":"C0120180201N6702","IssueNo":"6702","PublishDate":"2018-01-31","Cover":"https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180201N6702/jpeg/001.jpg"},{"IssueId":"C0120180101N6701","IssueNo":"6701","PublishDate":"2017-12-31","Cover":"https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180101N6701/jpeg/001.jpg"}]
     */

    @NonNull
    @PrimaryKey
    private String BookCase;
    private String MgName;
    private String IssuerName;
    private boolean BrandOpen;
    private List<IssueList> IssueList;
    /**
     * 当前报纸排序的初始索引
     */
    private int originalIndex;

    /**
     * 新增字段，当前报社收藏置顶的时间
     * 当前值为0时表示当前项未被收藏
     */
    private long checkedTimestamp = 0;

    public String getMgName() {
        return MgName;
    }

    public void setMgName(String MgName) {
        this.MgName = MgName;
    }

    public String getBookCase() {
        return BookCase;
    }

    public void setBookCase(String BookCase) {
        this.BookCase = BookCase;
    }

    public String getIssuerName() {
        return IssuerName;
    }

    public void setIssuerName(String IssuerName) {
        this.IssuerName = IssuerName;
    }

    public boolean isBrandOpen() {
        return BrandOpen;
    }

    public void setBrandOpen(boolean BrandOpen) {
        this.BrandOpen = BrandOpen;
    }

    public List<IssueList> getIssueList() {
        return IssueList;
    }

    public void setIssueList(List<IssueList> IssueList) {
        this.IssueList = IssueList;
    }

    public int getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(int originalIndex) {
        this.originalIndex = originalIndex;
    }

    public long getCheckedTimestamp() {
        return checkedTimestamp;
    }

    public void setCheckedTimestamp(long checkedTimestamp) {
        this.checkedTimestamp = checkedTimestamp;
    }
}
