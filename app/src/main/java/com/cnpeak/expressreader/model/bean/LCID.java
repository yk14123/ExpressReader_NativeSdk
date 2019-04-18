package com.cnpeak.expressreader.model.bean;

/**
 * 语系bean
 */
public class LCID {
    /**
     * LCID : 2052
     * DisplayName : 中文(简体)
     * ShortName : zh-cn
     * Sequence : 1
     */
    private String LCID;
    private String DisplayName;
    private String ShortName;
    private String Sequence;

    public String getLCID() {
        return LCID;
    }

    public void setLCID(String LCID) {
        this.LCID = LCID;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String DisplayName) {
        this.DisplayName = DisplayName;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String ShortName) {
        this.ShortName = ShortName;
    }

    public String getSequence() {
        return Sequence;
    }

    public void setSequence(String Sequence) {
        this.Sequence = Sequence;
    }

}
