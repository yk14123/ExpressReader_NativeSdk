package com.cnpeak.expressreader.model.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 报纸分类下的新闻详情
 */
public class NewsPaper implements Serializable {

    private String NID;
    private String LCID;
    private String NewsID;
    private String NewsName;
    private String ChannelID;
    private String ChannelName;
    private String SourceName;
    private String SourcePubDate;
    private String SourceTitle;
    private String SourceAuthor;
    private boolean OrigianSource;
    private String ExecuteDatetime;
    private String Imgs;
    private String Videos;
    private String Audios;
    private String HttpContext;
    private boolean CanTop;
    private boolean CanMarquee;

    public String getNID() {
        return NID;
    }

    public void setNID(String NID) {
        this.NID = NID;
    }

    public String getLCID() {
        return LCID;
    }

    public void setLCID(String LCID) {
        this.LCID = LCID;
    }

    public String getNewsID() {
        return NewsID;
    }

    public void setNewsID(String NewsID) {
        this.NewsID = NewsID;
    }

    public String getNewsName() {
        return NewsName;
    }

    public void setNewsName(String NewsName) {
        this.NewsName = NewsName;
    }

    public String getChannelID() {
        return ChannelID;
    }

    public void setChannelID(String ChannelID) {
        this.ChannelID = ChannelID;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String ChannelName) {
        this.ChannelName = ChannelName;
    }

    public String getSourceName() {
        return SourceName;
    }

    public void setSourceName(String SourceName) {
        this.SourceName = SourceName;
    }

    public String getSourcePubDate() {
        return SourcePubDate;
    }

    public void setSourcePubDate(String SourcePubDate) {
        this.SourcePubDate = SourcePubDate;
    }

    public String getSourceTitle() {
        return SourceTitle;
    }

    public void setSourceTitle(String SourceTitle) {
        this.SourceTitle = SourceTitle;
    }

    public String getSourceAuthor() {
        return SourceAuthor;
    }

    public void setSourceAuthor(String SourceAuthor) {
        this.SourceAuthor = SourceAuthor;
    }

    public boolean isOrigianSource() {
        return OrigianSource;
    }

    public void setOrigianSource(boolean OrigianSource) {
        this.OrigianSource = OrigianSource;
    }

    public String getExecuteDatetime() {
        return ExecuteDatetime;
    }

    public void setExecuteDatetime(String ExecuteDatetime) {
        this.ExecuteDatetime = ExecuteDatetime;
    }

//    public String getImgs() {
//        return Imgs;
//    }

    public ArrayList<String> getImgs() {
        ArrayList<String> imgUrls = new ArrayList<>();
        if (!TextUtils.isEmpty(Imgs) && !TextUtils.equals("[]", Imgs)) {
            //当前Imgs标签不为空，并且不为[]，对其进行解析
            //1、去掉首尾
            if (Imgs.startsWith("[") && Imgs.endsWith("]")) {
                String subStr = Imgs.substring(1, Imgs.length() - 1);
                //2、正则切割图片路径
                String[] split = subStr.split(",");
                if (split.length != 0) {
                    for (String imgUrl : split) {
                        //3、去除首尾符号
                        if (!TextUtils.isEmpty(imgUrl) & imgUrl.startsWith("\"") && imgUrl.endsWith("\"")) {
                            imgUrl = imgUrl.substring(1, imgUrl.length() - 1);
                        }
                        imgUrls.add(imgUrl);
                    }
                    return imgUrls;
                }
            }
        }
        return imgUrls;
    }

    public void setImgs(String Imgs) {
        this.Imgs = Imgs;
    }

    public String getVideos() {
        return Videos;
    }

    public void setVideos(String Videos) {
        this.Videos = Videos;
    }

    public String getAudios() {
        return Audios;
    }

    public void setAudios(String Audios) {
        this.Audios = Audios;
    }

    public String getHttpContext() {
        return HttpContext;
    }

    public void setHttpContext(String HttpContext) {
        this.HttpContext = HttpContext;
    }

    public boolean isCanTop() {
        return CanTop;
    }

    public void setCanTop(boolean CanTop) {
        this.CanTop = CanTop;
    }

    public boolean isCanMarquee() {
        return CanMarquee;
    }

    public void setCanMarquee(boolean CanMarquee) {
        this.CanMarquee = CanMarquee;
    }
}
