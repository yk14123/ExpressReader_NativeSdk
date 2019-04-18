package com.cnpeak.expressreader.model.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * @author builder by HUANG JIN on ${date}
 * @version 1.0
 * @description 章节内容bean
 */
@Entity
public class IssueUnit implements Serializable {

    /**
     * MnId : 28B130FD-5755-4F30-8A80-407BFF18E392
     * MgName : 今日中国
     * IssueNo : 6708
     * BType : L
     * UType : 1(1：小图 2：封面 ；3 热门)
     * PublishDate : 2018-07-31
     * Title : 中国与世界：分享与成长
     * HtmlContent : <div ID="p1"><p>2001年12月11日，中国正式加入世界贸易组织（WTO）。这一天对于中国而言，具有里程碑的意义。在此之后的17年，中国用时间和实践回答了入世的时代之问。</p><p>回溯17年，入世翌日，在北京举行的隆重的首届中国总裁日暨中国汽车发展论坛，至今仍具有象征和启示意义。那一天，担心因大雪而堵车的东风神龙公司、日本丰田公司、通用（中国）汽车总公司等多家世界知名厂商的老总们早早来到长城饭店的论坛会场，探讨中国汽车工业将在入世后如何发展、外资车企将如何在中国树立良好的品牌形象等等。众所周知，在入世之初，中国多数产业直接面对国际竞争面临巨大困难，而汽车产业更是首当其冲。“狼来了”，怎么“与狼共舞”尽快融入世界等种种担忧，折射出中国各产业在当时的复杂心态。这天下午的北京晚报发表了《入世不是狼来了而是浪来了》的文章，用东风神龙公司副总刘卫东的话，“入世对中国汽车业来说，不应该是狼来了，应该是浪来了。入世对我们来说只是整合国际资源的一个开始”回应了人们的忧虑。人民日报评论员的文章更是高瞻远瞩：“中国正式成为世界贸易组织成员。这是我国改革开放和现代化建设的历史必然，也是进一步推进全方位、多层次、宽领域对外开放的重要契机，对我国的经济发展将具有深远的影响。”同时认为，这将促进中国自身的改革开放和经济发展，还将鼓舞全球经济增长的信心，有助于多边贸易体制的发展，中国将为世界经济贸易的发展做出积极贡献。这些文章和话语，如今看来还是那般亲切、带劲，富有智慧。</p><p>17年来，中国正是沿着这条道路演绎着成长的故事。这是一个以入世为契机进一步扩大对外开放，逐步融入全球价值链的年代大剧，故事的主人公是中国与世界。</p><p>本期“今日视点”栏目即以《开放的中国与世界共赢》为题，讲述了中国在改革开放40年的宏阔背景下，迎着新世纪的曙光，在面临前所未有的机遇与挑战中的发展轨迹。</p><p>比预想更好的是，中国自信地回答了WTO之问。2018年6月28日，中国国务院新闻办公室首次发表《中国与世界贸易组织》白皮书，系统阐述中国加入世贸组织17年来切实履行入世承诺、坚定支持多边贸易体制、对世界作出的贡献，以及推动更高水平对外开放的愿景与行动。17年来，中国在经济和贸易发展过程中充分享受了入世红利，迅速成长为全球第二大经济体、全球第二大贸易国和第一大出口国。中国因开放而实现经济腾飞，更为拉动世界经济的复苏和增长作出了重要贡献。</p><p>2018年7月13日，世界贸易组织（WTO）通过了对中国的第七次贸易政策审查。这次受到世界各经济体高度关注的政策审查，在当前高度复杂的国际贸易形势下，具有重要的风向标意义。</p><p>特别是在“逆全球化”潮流抬头、贸易保护主义不断升级、全球多边机制不振等背景下，一些成员绕过世界贸易组织（WTO）引发经贸争端的今天，中国一如既往地表明做多边贸易体制的积极参与者、坚定维护者和重要贡献者。同时，通过“一带一路”倡议、自贸协定等多边合作机制等为全球提供公共产品，希望更多成员能与自己共同受益、共同繁荣。</p><p>不难看出，随着中国开放的大门越来越大，中国与世界分享、成长的故事还将继续上演。</p></div><h10>&lt;本文结束&gt;</h10>
     * Images : ["https://express-reader.s3.cn-north-1.amazonaws.com.cn/issueimg/C0120180801N6708/1533777062445.jpg"]
     * Videos : []
     * Pages : ["https://magexp.oss-cn-beijing.aliyuncs.com/2052/C0120180801N6708/jpeg/007.jpg"]
     */

    private String MnId;
    private String MgName;
    private String IssueNo;
    private String BType;
    private int UType;
    private String PublishDate;
    private String Title;
    private String HtmlContent;
    private List<String> Images;
    private List<String> Videos;
    private List<String> Pages;
    /**
     * 新增字段，数据存储类型{0:阅读记录，1：收藏记录}
     */
    private int dbType;

    /**
     * 新增字段，存入数据库的时间
     */
    @PrimaryKey
    private long createDate;

    public String getMnId() {
        return MnId;
    }

    public void setMnId(String MnId) {
        this.MnId = MnId;
    }

    public String getMgName() {
        return MgName;
    }

    public void setMgName(String MgName) {
        this.MgName = MgName;
    }

    public String getIssueNo() {
        return IssueNo;
    }

    public void setIssueNo(String IssueNo) {
        this.IssueNo = IssueNo;
    }

    public String getBType() {
        return BType;
    }

    public void setBType(String BType) {
        this.BType = BType;
    }

    public int getUType() {
        return UType;
    }

    public void setUType(int UType) {
        this.UType = UType;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String PublishDate) {
        this.PublishDate = PublishDate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getHtmlContent() {
        return HtmlContent;
    }

    public void setHtmlContent(String HtmlContent) {
        this.HtmlContent = HtmlContent;
    }

    public List<String> getImages() {
        return Images;
    }

    public void setImages(List<String> Images) {
        this.Images = Images;
    }

    public List<String> getVideos() {
        return Videos;
    }

    public void setVideos(List<String> Videos) {
        this.Videos = Videos;
    }

    public List<String> getPages() {
        return Pages;
    }

    public void setPages(List<String> Pages) {
        this.Pages = Pages;
    }

    public int getDbType() {
        return dbType;
    }

    public void setDbType(int dbType) {
        this.dbType = dbType;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "IssueUnit{" +
                "MnId='" + MnId + '\'' +
                ", MgName='" + MgName + '\'' +
                ", IssueNo='" + IssueNo + '\'' +
                ", BType='" + BType + '\'' +
                ", UType=" + UType +
                ", PublishDate='" + PublishDate + '\'' +
                ", Title='" + Title + '\'' +
                ", HtmlContent='" + HtmlContent + '\'' +
                ", Images=" + Images +
                ", Videos=" + Videos +
                ", Pages=" + Pages +
                '}';
    }
}
