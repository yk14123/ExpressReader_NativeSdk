package com.cnpeak.expressreader.analytics;

/**
 * The interface Analytics event.
 */
public interface IAnalyticsEvent {

    /**
     * 记录点击杂志单元化时发送经纬度给后台，分析使用者在哪些地区点击观看
     *
     * @param MnId      杂志唯一标示
     * @param Latitude  纬度
     * @param Longitude 经度
     */
    void onIssueUnitClickEvent(String MnId, String Latitude, String Longitude, String LCID);

    /**
     * On news click event.
     *
     * @param NID          新闻唯一标示
     * @param Latitude     纬度
     * @param Longitude    经度
     * @param RealLocation 真实位置（暂未使用）
     * @param LCID         语系ID
     */
    void onNewsClickEvent(String NID, String Latitude, String Longitude, String RealLocation, String LCID);

    /**
     * 点击杂志品牌，分析会员看哪些杂志品牌
     *
     * @param Id        the id
     * @param brandCode the brand code
     * @param userName  the user name
     */
    void onMagazineBrandClickEvent(String Id, String brandCode, String userName);

}
