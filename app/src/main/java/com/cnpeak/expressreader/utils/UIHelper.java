package com.cnpeak.expressreader.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cnpeak.expressreader.analytics.AnalyticsAgent;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.bean.MagazineList;
import com.cnpeak.expressreader.model.bean.PaperCover;
import com.cnpeak.expressreader.model.db.DbType;
import com.cnpeak.expressreader.view.home.ErHomeActivity;
import com.cnpeak.expressreader.view.magazine.magazine_detail.MagazineDetailActivity;
import com.cnpeak.expressreader.view.magazine.magazine_review.MagazineReviewActivity;
import com.cnpeak.expressreader.view.magazine.magazine_units.MagazineUnitActivity;
import com.cnpeak.expressreader.view.mine.local.LocalHistoryActivity;
import com.cnpeak.expressreader.view.mine.locale.LocaleSettingActivity;
import com.cnpeak.expressreader.view.news.news_detail.NewsDetailActivity;
import com.cnpeak.expressreader.view.news.news_image.ImageDetailActivity;
import com.cnpeak.expressreader.view.newspaper.paper_news.PaperNewsActivity;
import com.cnpeak.expressreader.view.splash.ErSplashActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Intent跳转管理类
 */
public class UIHelper {

    /**
     * 跳转闪屏界面
     */
    public static void startSplashActivity(Context context) {
        Intent intent = new Intent(context, ErSplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转首页
     */
    public static void startHomeActivity(Context context) {
        Intent intent = new Intent(context, ErHomeActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转报纸新闻列表展示页面
     */
    public static void startPageListActivity(Context context, PaperCover paperCover) {
        Intent intent = new Intent(context, PaperNewsActivity.class);
        intent.putExtra(ErConstant.PAPER_COVER, paperCover);
        context.startActivity(intent);
    }

    /**
     * 跳转杂志内容详情页面
     *
     * @param itemData 热点资讯数据bean
     */
    public static void startNewsDetailActivity(Context context, HotSpot itemData) {
        if (itemData != null) {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra(ErConstant.HOTSPOT_DATA_BEAN, itemData);
            context.startActivity(intent);

        }
    }

    /**
     * 跳转往期杂志列表页面
     *
     * @param magazineList 杂志单元bean
     */
    public static void startMagazineReviewActivity(Activity context, MagazineList magazineList) {
        Intent intent = new Intent(context, MagazineReviewActivity.class);
        intent.putExtra(ErConstant.ISSUE_LIST, magazineList);
        context.startActivityForResult(intent, ErConstant.MAGAZINE_UNIT_REQUEST_CODE);
    }

    /**
     * 跳转杂志分类条目页
     *
     * @param magazineList 数据bean
     */
    public static void startMagazineUnitActivity(Context context, MagazineList magazineList) {
        Intent intent = new Intent(context, MagazineUnitActivity.class);
        intent.putExtra(ErConstant.ISSUE_LIST, magazineList);
        context.startActivity(intent);
    }


    /**
     * 跳转杂志内容详情页面
     *
     * @param MnId 杂志id see{@link com.cnpeak.expressreader.model.bean.IssueLite}
     */
    public static void startMagazineDetailActivity(Context context, String MnId) {
        Intent intent = new Intent(context, MagazineDetailActivity.class);
        intent.putExtra(ErConstant.MN_ID, MnId);
        context.startActivity(intent);
    }

    /**
     * 跳转大图浏览界面
     *
     * @param imgUrls      图片地址数组
     * @param defaultIndex 当前默认索引
     * @param showIndex    是否需要显示索引
     */
    public static void startImageActivity(Context context, List<String> imgUrls, int defaultIndex, boolean showIndex) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putStringArrayListExtra(ErConstant.IMAGE_DETAIL_URLS, new ArrayList<>(imgUrls));
        intent.putExtra(ErConstant.IMAGE_CURRENT_INDEX, defaultIndex);
        intent.putExtra(ErConstant.IMAGE_SHOW_FLAG, showIndex);
        context.startActivity(intent);
    }

    /**
     * 跳转语言环境设置界面
     */
    public static void startLocaleActivity(Context context) {
        Intent intent = new Intent(context, LocaleSettingActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转阅读记录页面
     *
     * @param dbType 当前数据库的类型 see{@link DbType}
     */
    public static void startHistoryActivity(Context context, @DbType int dbType) {
        Intent intent = new Intent(context, LocalHistoryActivity.class);
        intent.putExtra(ErConstant.FAVOR_FLAG, dbType);
        context.startActivity(intent);
    }

}
