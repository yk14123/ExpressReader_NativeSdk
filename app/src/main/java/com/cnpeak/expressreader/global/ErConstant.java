package com.cnpeak.expressreader.global;

/**
 * @author builder by HUANG JIN
 * @version 1.0
 * 常量类
 */
public class ErConstant {

    public static final String BASE_URL = "http://39.106.59.13:9002";

    public static final String BASE_URL_TEST = "http://47.95.229.112:9002";

    public static final String BASE_SERVER_URL = "https://api.expressreader.chinafocus.net";

    //参数常量key
    public static final String HOTSPOT_DATA_BEAN = "hotspot_data_bean";
    public static final String IMAGE_DETAIL_URLS = "image_detail_urls";
    public static final String IMAGE_CURRENT_INDEX = "image_current_index";
    public static final String IMAGE_SHOW_FLAG = "image_show_flag";
    public static final String ISSUE_LIST = "magazine_issue_list";
    public static final String MN_ID = "MnID";
    public static final String LCID = "LCID";
    public static final String NEWS_ID = "newsId";
    public static final String CHANNEL_ID = "channelId";
    //ChannelID
    public static final String HOTSPOT = "301";//热点
    public static final String KEYWORD = "keyword";
    public static final String PAPER_COVER = "paperCover";
    public static final String ISSUE_ID = "issueId";
    public static final String ISSUE_INDEX = "issueIndex";
    public static final String FAVOR_FLAG = "favorFlag";
    public static final int DATA_TYPE_HOTSPOT = 0;
    public static final int DATA_TYPE_ISSUE = 1;

    //用户相关
    public static final String MEMBER_NAME = "MemberName";
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";
    public static final String MEMBER_PASSWORD = "MemberPassword";

    //支付相关
    public static final String PRODUCT_ID = "productId";
    public static final String SUBSCRIPTION_TYPE = "subscriptionType";
    public static final String TRANSITION_ID = "transactionId";
    public static final String RECEIPT = "receipt";
    public static final String SIGNATURE = "signature";

    //统计相关
    public static final String PLATFORM = "PlatForm";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String REAL_LOCATION = "RealLocation";
    public static final String BRAND_CODE = "BrandCode";
    public static final String USER_NAME = "UserName";
    public static final String CLICK_TIME = "ClickTime";
    public static final String ID = "Id";
    public static final String NID = "NID";

    //SharePreference存储常量key
    public static final String FONT_OPTION = "font_option_setting";
    public static final String LOCALE_OPTION = "locale_setting_option";
    public static final String USER_INFO = "user_info";

    //请求码
    public static final int MAGAZINE_UNIT_REQUEST_CODE = 0x100;

    //RecyclerView 常用viewType常量定义
    public static final int ITEM_TYPE_EMPTY = 0;//空视图
    public static final int ITEM_TYPE_HEADER = 1;//头部视图
    public static final int ITEM_TYPE_DATA = 2;//数据视图
}
