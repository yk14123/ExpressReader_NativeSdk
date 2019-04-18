package com.cnpeak.expressreader.model.bean;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.io.Serializable;


/**
 * 报纸封面部分
 */
@Entity
public class PaperCover implements Serializable {

    /**
     * PaperID : 901
     * NewsID : 602
     * PaperDescription : 新华通讯社是中国国家通讯社和世界性通讯社。它及时、准确、权威地报道党和国家的方针政策等各个领域的重要新闻。全天24小时不间断用中文、英文、法文、俄文、西班牙文、阿拉伯文、葡萄牙文和日文8种文字发稿。
     * PaperLogoImgBinary : null
     * PaperLogoImgUrl : http://cnpiecbucket101.oss-cn-beijing.aliyuncs.com/logos/express_reader/XinHua.png
     * Bewrite : 新华社
     * Enabled : true
     * SourcePubDate : 2018-12-26
     */
    @NonNull
    @PrimaryKey
    private String NewsID;
    private String PaperID;
    private String PaperDescription;
    @Ignore
    private Object PaperLogoImgBinary;
    private String PaperLogoImgUrl;
    private String Bewrite;
    private boolean Enabled;
    /**
     * 新增字段，当前报社收藏置顶的时间
     * 当前值为0时表示当前项未被收藏
     */
    private long checkedTimestamp = 0;
    /**
     * 当前报纸排序的初始索引
     */
    private int originalIndex;
    private String SourcePubDate;

    public String getPaperID() {
        return PaperID;
    }

    public void setPaperID(String PaperID) {
        this.PaperID = PaperID;
    }

    public String getNewsID() {
        return NewsID;
    }

    public void setNewsID(String NewsID) {
        this.NewsID = NewsID;
    }

    public String getPaperDescription() {
        return PaperDescription;
    }

    public void setPaperDescription(String PaperDescription) {
        this.PaperDescription = PaperDescription;
    }

    public Object getPaperLogoImgBinary() {
        return PaperLogoImgBinary;
    }

    public void setPaperLogoImgBinary(Object PaperLogoImgBinary) {
        this.PaperLogoImgBinary = PaperLogoImgBinary;
    }

    public String getPaperLogoImgUrl() {
        return PaperLogoImgUrl;
    }

    public void setPaperLogoImgUrl(String PaperLogoImgUrl) {
        this.PaperLogoImgUrl = PaperLogoImgUrl;
    }

    public String getBewrite() {
        return Bewrite;
    }

    public void setBewrite(String Bewrite) {
        this.Bewrite = Bewrite;
    }

    public boolean isEnabled() {
        return Enabled;
    }

    public long getCheckedTimestamp() {
        return checkedTimestamp;
    }

    public void setCheckedTimestamp(long checkedTimestamp) {
        this.checkedTimestamp = checkedTimestamp;
    }

    public void setEnabled(boolean Enabled) {
        this.Enabled = Enabled;
    }

    public int getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(int originalIndex) {
        this.originalIndex = originalIndex;
    }

    public String getSourcePubDate() {
        return SourcePubDate;
    }

    public void setSourcePubDate(String SourcePubDate) {
        this.SourcePubDate = SourcePubDate;
    }

    @Override
    public String toString() {
        return "PaperCover{" +
                "NewsID='" + NewsID + '\'' +
                ", PaperID='" + PaperID + '\'' +
                ", PaperDescription='" + PaperDescription + '\'' +
                ", PaperLogoImgBinary=" + PaperLogoImgBinary +
                ", PaperLogoImgUrl='" + PaperLogoImgUrl + '\'' +
                ", Bewrite='" + Bewrite + '\'' +
                ", Enabled=" + Enabled +
                ", checkedTimestamp=" + checkedTimestamp +
                ", originalIndex=" + originalIndex +
                ", SourcePubDate='" + SourcePubDate + '\'' +
                '}';
    }
}
