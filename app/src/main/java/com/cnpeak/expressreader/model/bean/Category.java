package com.cnpeak.expressreader.model.bean;

import java.io.Serializable;

/**
 * 首页热点资讯分类bean
 */
public class Category implements Serializable {
    /**
     * Sequence : 1
     * CategoryText : 热点
     * ChannelID : 301
     * LCID : 2052
     * DisplayType : 0
     */

    private int Sequence;
    private String CategoryText;
    private String ChannelID;
    private String LCID;
    private String DisplayType;

    public int getSequence() {
        return Sequence;
    }

    public void setSequence(int Sequence) {
        this.Sequence = Sequence;
    }

    public String getCategoryText() {
        return CategoryText;
    }

    public void setCategoryText(String CategoryText) {
        this.CategoryText = CategoryText;
    }

    public String getChannelID() {
        return ChannelID;
    }

    public void setChannelID(String ChannelID) {
        this.ChannelID = ChannelID;
    }

    public String getLCID() {
        return LCID;
    }

    public void setLCID(String LCID) {
        this.LCID = LCID;
    }

    public String getDisplayType() {
        return DisplayType;
    }

    public void setDisplayType(String DisplayType) {
        this.DisplayType = DisplayType;
    }

}
