package com.cnpeak.expressreader.model.bean;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author builder by HUANG JIN on 18/11/6
 * @version 1.0
 */
@Entity
public class HotSpot implements Serializable {

    /**
     * NID : E77ECD80-3037-4705-AEFD-D6E9329B32D8
     * LCID : 2052
     * NewsID : 602
     * NewsName : 新华社
     * ChannelID : 999
     * ChannelName :
     * SourceName : Bndbur
     * SourcePubDate : 2018-11-08 13:49:48
     * SourceTitle : （经济）印度１０月失业率创两年来新高
     * SourceAuthor : 胡晓明
     * OrigianSource : true
     * ExecuteDatetime : 2018-11-08 13:51:52
     * Imgs : [\"https://express-reader-images.oss-cn-beijing.aliyuncs.com/2f55c863db3866ac2cf6f93990cffad11fa0fc12.jpg\", \"https://express-reader-images.oss-cn-beijing.aliyuncs.com/772e8ff8020d0dd1613e4af797e46f2cb6859d56.jpg\", \"https://express-reader-images.oss-cn-beijing.aliyuncs.com/5a22fdea9b047edaae7dab672a8aa25e95f66734.jpg\", \"https://express-reader-images.oss-cn-beijing.aliyuncs.com/a590c8aa8d929fe5fc5cfc04326792c5e47eda1b.jpg\", \"https://express-reader-images.oss-cn-beijing.aliyuncs.com/0d814c34ca5d9d68d18a59488f267f78f4544c69.gif\", \"https://express-reader-images.oss-cn-beijing.aliyuncs.com/1021564d4ba7e6566c377ae7c252b6b2861085a5.jpg\"]
     * Videos : []
     * Audios : []
     * HttpContext : <p>新华社新德里１１月８日电（记者胡晓明）根据印度经济监测中心公布的最新数据，印度１０月失业率升至６．９％，创两年来新高。同时，劳动参与率降至４２．４％，创自２０１６年１月有该项统计以来最低。</p><p>该中心日前发布的一份研究报告指出，自２０１６年废钞令颁布以来，印度的劳动参与率从４７％至４８％急剧下降，至今尚未恢复。１０月的数据表明劳动力市场情况没有好转。</p><p>报告显示，印度２０１８年１０月的就业人口为３．９７亿，比去年同期减少２．４％；失业人口从去年７月的１４００万增加到了今年１０月的２９５０万。</p><p>分析人士指出，１０月至１２月通常是印度的就业旺季，当前就业不足的状况令人担忧，其背后的重要原因包括该国电力和基础设施领域表现不佳、非银行金融机构贷款不足等。（完）</p>
     * CanTop : false
     * CanMarquee : false
     * NewsIconUrl : http://cnpiecbucket101.oss-cn-beijing.aliyuncs.com/logos/express_reader/XinHua.png
     * TagInfo : null
     */
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
    private String NewsIconUrl;
    @Ignore
    private Object TagInfo;
    /**
     * 新增标示，当前的存储类型{0:历史记录 1、收藏记录}
     */
    private int dbType;
    /**
     * 新增标示,当前加入数据库的时间戳
     */
    @PrimaryKey
    private long createDate;

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

    public int getDbType() {
        return dbType;
    }

    public void setDbType(int dbType) {
        this.dbType = dbType;
    }

    public String getImgs() {
        return Imgs;
    }

    public void setImgs(String Imgs) {
        this.Imgs = Imgs;
    }

    /**
     * "[]"
     * <p>
     * "["https://v.xinhua-news.com/store_attachment/xcbattachfile/dspdata/2018/12/24/XxjnmcC007024_20181224_NDMFN0A001.jpg",
     * "https://v.xinhua-news.com/store_attachment/xcbattachfile/dspdata/2018/12/24/XxjnmcC007024_20181224_NDMFN0A002.jpg",
     * "https://v.xinhua-news.com/store_attachment/xcbattachfile/dspdata/2018/12/24/XxjnmcC007024_20181224_NDMFN0A003.jpg",
     * "https://v.xinhua-news.com/store_attachment/xcbattachfile/dspdata/2018/12/24/XxjnmcC007024_20181224_NDMFN0A004.jpg",
     * "https://v.xinhua-news.com/store_attachment/xcbattachfile/dspdata/2018/12/24/XxjnmcC007024_20181224_NDMFN0A005.jpg"]"
     */
    public List<String> getImgUrls() {
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

    public String getNewsIconUrl() {
        return NewsIconUrl;
    }

    public void setNewsIconUrl(String NewsIconUrl) {
        this.NewsIconUrl = NewsIconUrl;
    }

    public Object getTagInfo() {
        return TagInfo;
    }

    public void setTagInfo(Object TagInfo) {
        this.TagInfo = TagInfo;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
