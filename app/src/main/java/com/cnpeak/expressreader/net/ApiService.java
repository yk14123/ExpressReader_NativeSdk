package com.cnpeak.expressreader.net;

import com.cnpeak.expressreader.model.bean.Category;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.bean.IssueLite;
import com.cnpeak.expressreader.model.bean.IssueUnit;
import com.cnpeak.expressreader.model.bean.LCID;
import com.cnpeak.expressreader.model.bean.MagazineList;
import com.cnpeak.expressreader.model.bean.PaperCover;
import com.cnpeak.expressreader.model.bean.PayStatus;
import com.cnpeak.expressreader.model.bean.ResultBean;
import com.cnpeak.expressreader.model.bean.UserBean;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author builder by HUANG JIN on 18/11/6
 * @version 1.0
 * 项目API service相关接口定义管理类
 */
public interface ApiService {

    /**
     * 获取语系代码
     */
    @POST("/api/Lcid")
    Observable<List<LCID>> getLCID();

    /**
     * 取得热点新闻资讯
     */
    @POST("/api/HotSpot/hotspot")
    Observable<List<HotSpot>> getHotSpot(@Query("LCID") String LCID,
                                         @Query("pageIndex") int pageIndex,
                                         @Query("pageSize") int pageSize);

    /**
     * 取得热点新闻资讯分类
     */
    @POST("/api/Category")
    Observable<List<Category>> getCategory(@Query("LCID") String LCID);

    /**
     * 取得热点新闻资讯分类下的新闻列表
     */
    @GET("/api/News{LCID}")
    Observable<List<HotSpot>> getCategoryNews(@Path("LCID") String LCID, @Query("NewsID") String newsId,
                                              @Query("ChannelID") String channelId, @Query("keyword") String keyword,
                                              @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 取得热点新闻banner数据
     */
    @GET("/api/StickTop")
    Observable<List<HotSpot>> getStickTop(@Query("LCID") String LCID);


    /**
     * 获取报纸封面列表数据
     */
    @GET("/api/PaperCover")
    Observable<List<PaperCover>> getPagerCover(@Query("LCID") String LCID);

    /**
     * 获取报纸封面列表数据
     */
    @GET("/api/NewsPaper")
    Observable<List<HotSpot>> getNewsPaper(@Query("LCID") String LCID, @Query("NewsID") String newsId,
                                           @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);


    /**
     * 获取指定国家码所有的2018年后的杂志
     */
    @POST("/api/Magazine/GetMagazineList")
    Observable<List<MagazineList>> getMagazineList(@Query("LCID") String LCID);

    /**
     * 获取单本杂志内容
     */
    @POST("/api/Magazine/GetIssueLite")
    Observable<IssueLite> getIssueLite(@Query("IssueId") String IssueId);


    /**
     * 获取杂志章节内容
     */
    @POST("/api/Magazine/GetIssueUnit")
    Observable<IssueUnit> getIssueUnit(@Query("MnID") String MnID);

    /**
     * 用户Email注册
     */
    @Headers({"Content-Type: application/json; charset=utf-8"})
    @POST("/api/Member/EmailRegister")
    Observable<ResultBean> registerByEmail(@Body JsonObject requestBody);

    /**
     * 用户Email登入
     */
    @Headers({"Content-Type: application/json; charset=utf-8"})
    @POST("/api/Member/EmailSignIn")
    Observable<UserBean> signInByEmail(@Body JsonObject requestBody);

    /**
     * 记录点击杂志单元化时发送经纬度给后台，分析使用者在哪些地区点击观看
     */
    @Headers({"Content-Type: application/json; charset=utf-8"})
    @POST("/api/Log/MagZCounter")
    Call<Void> sendMagClickEvent(@Body JsonObject requestBody);

    /**
     * 记录点击报纸单元化时发送经纬度给后台，分析使用者在哪些地区点击观看
     */
    @Headers({"Content-Type: application/json; charset=utf-8"})
    @POST("/api/Log/NewsCounter")
    Call<Void> sendNewsClickEvent(@Body JsonObject requestBody);

    /**
     * 点击杂志品牌，分析会员看哪些杂志品牌
     */
    @Headers({"Content-Type: application/json; charset=utf-8"})
    @POST("/api/Log/MagStatistics")
    Call<Void> sendMagBrandClickEvent(@Body JsonObject requestBody);

    /**
     * 点击杂志品牌，分析会员看哪些杂志品牌
     */
    @Headers({"Content-Type: application/json; charset=utf-8"})
    @POST("/api/Receipt/GoogleReceipt")
    Observable<PayStatus> sendPayReceipt(@Body JsonObject requestBody);

    /**
     * 获取Microsoft授权信息
     * https://westus.api.cognitive.microsoft.com/sts/v1.0/issueToken
     */
    @Headers("Authorization:Bearer;Content-Type:application/ssml+xml;X-Microsoft-OutputFormat:audio-16khz-64kbitrate-mono-mp3;User-Agent:ExpressReader")
    @POST("/cognitiveservices/v1")
    Observable getAccessToken(@Query("fetchUri") String fetchUri, @Query("subscriptionKey") String subscriptionKey);


}
