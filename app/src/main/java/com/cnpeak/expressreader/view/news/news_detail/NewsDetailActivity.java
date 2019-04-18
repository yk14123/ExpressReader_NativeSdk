package com.cnpeak.expressreader.view.news.news_detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseActivity;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.OnFontOptionListener;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.db.SQLConstant;
import com.cnpeak.expressreader.permission.PermissionHelper;
import com.cnpeak.expressreader.utils.GlideCircleTransform;
import com.cnpeak.expressreader.utils.LocaleHelper;
import com.cnpeak.expressreader.utils.LogUtils;
import com.cnpeak.expressreader.utils.SpUtil;
import com.cnpeak.expressreader.utils.ToastUtils;
import com.cnpeak.expressreader.view.dialog.FontSettingsDialog;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author builder by HUANG JIN on 18/11/12
 * @version 1.0
 * 热点资讯详情加载页面
 */
public class NewsDetailActivity extends BaseActivity<NewsDetailView, NewsDetailPresenter> implements NewsDetailView {
    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    //控制器
    private NewsDetailPresenter mDetailPresenter;
    //滑动组件
    private NestedScrollView mNestedScrollView;
    private TextView mTvNewsTitle;
    private Toolbar mToolBar;
    //WebView
    private FrameLayout mFlVideoView;
    private WebView mWebView;
    //Js调用对象
    private JsToAndroid mJsToAndroid;
    //HotSpot实体Bean对象
    private HotSpot mHotSpot;
    //底部字号选择框
    private FontSettingsDialog mFontOptionDialog;
    //是否被收藏
    private boolean mFavorFlag;
    //是否向服务器发送过统计事件
    private boolean hasSendEvent = false;

    //文字转语音播报
    private TextToSpeech mTtsClient;
    private String httpContext;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.expressreader_activity_news_detail;
    }

    @Override
    protected NewsDetailPresenter initPresenter() {
        mDetailPresenter = new NewsDetailPresenter(this);
        //查询当前数据bean在数据库中的收藏状态
        mDetailPresenter.queryTypeByNID(mHotSpot);
        return mDetailPresenter;
    }

    @Override
    protected void getExtraFromBundle(Intent intent) {
        super.getExtraFromBundle(intent);
        if (intent != null) {
            //获取HotSpot数据实体类对象
            Serializable extra = intent.getSerializableExtra(ErConstant.HOTSPOT_DATA_BEAN);
            if (extra instanceof HotSpot) {
                mHotSpot = (HotSpot) extra;
            }
        }
    }

    @Override
    public void initView() {
        mNestedScrollView = findViewById(R.id.nsv_news_detail);
        mFlVideoView = findViewById(R.id.fl_news_detail_video);
        //初始化ToolBar
        initToolbar();
        //初始化WebView
        initWebView();
        //初始化页面数据
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!hasSendEvent) {
            checkLocationPermission();
        }
    }

    private void checkLocationPermission() {
        if (PermissionHelper.checkPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
            sendEvent();
        } else {
            PermissionHelper.requestRuntimePermission(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        }
    }

    private void sendEvent() {
        String[] locationInfo = PermissionHelper.getKnownLocation(mContext);
        mDetailPresenter.sentNewsClickEvent(mHotSpot.getNID(), locationInfo[0],
                locationInfo[1], LocaleHelper.getLCID(mContext));
        Log.d(TAG, "sendEvent: >>>" + locationInfo[0]);
        hasSendEvent = true;
    }

    private void initToolbar() {
        //ToolBar
        mToolBar = findViewById(R.id.toolbar);
        mToolBar.setTitle("");//不显示标题
        setSupportActionBar(mToolBar);
        //设置左侧NavigationIcon按钮的点击事件
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void initData() {
        //新聞標題
        TextView tvTitle = findViewById(R.id.tv_news_detail_name);
        //新聞icon图
        ImageView ivNewsIcon = findViewById(R.id.iv_news_detail_icon);
        //发布时间
        TextView tvPublishDate = findViewById(R.id.tv_news_detail_time);
        if (mHotSpot != null) {
            //sourceTitle
            String sourceTitle = mHotSpot.getSourceTitle();
            if (!TextUtils.isEmpty(sourceTitle)) {
                mTvNewsTitle.setText(sourceTitle);
            }
            //newsName
            String newsName = mHotSpot.getNewsName();
            if (!TextUtils.isEmpty(newsName)) {
                tvTitle.setText(newsName);
            }
            //publishDate
            String sourcePubDate = mHotSpot.getSourcePubDate();
            if (!TextUtils.isEmpty(sourcePubDate)) {
                tvPublishDate.setText(sourcePubDate);
            }
            //newsIcon
            String newsIconUrl = mHotSpot.getNewsIconUrl();
            Glide.with(mContext)
                    .load(newsIconUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.expressreader_news_default_head)
                            .placeholder(R.drawable.expressreader_news_default_head)
                            .transform(new GlideCircleTransform(mContext)))
                    .into(ivNewsIcon);

//            String httpContext = mHotSpot.getHttpContext();
            httpContext = mHotSpot.getHttpContext();
            if (!TextUtils.isEmpty(httpContext)) {
                mWebView.loadData(httpContext, "text/html; charset=UTF-8", null);
            } else {
                LogUtils.i(TAG, "当前内容详情参数为空，请检查数据 >>> ");
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        //页面标题
        mTvNewsTitle = findViewById(R.id.tv_news_detail_title);
        mWebView = findViewById(R.id.wv_news_detail);
        mWebView.setVerticalScrollBarEnabled(false);//不能垂直滑动
        mWebView.setHorizontalScrollBarEnabled(false);//不能水平滑动
        WebSettings settings = mWebView.getSettings();
        //settings.setUseWideViewPort(true);//调整到适合webView的大小，不过尽量不要用，有些手机有问题
        settings.setLoadWithOverviewMode(true);//设置WebView是否使用预览模式加载界面。
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);//设置js可用
        mJsToAndroid = new JsToAndroid(mContext);
        mWebView.addJavascriptInterface(mJsToAndroid, "_expressReaderJsConnect");//设置js接口
        mWebView.setWebViewClient(new ErWebViewClient());
        mWebView.setWebChromeClient(new ErWebChromeClient());
        //初始化内容的字体大小
        int fontOption = SpUtil.getInt(mContext, ErConstant.FONT_OPTION, FontSettingsDialog.FONT_NORMAL);
        setFontSize(fontOption);
    }

    /**
     * 设置页面内容的字体大小
     *
     * @param fontSize 字体大小标识
     */
    private void setFontSize(int fontSize) {
        switch (fontSize) {
            case FontSettingsDialog.FONT_LARGEST:
                mTvNewsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelOffset(R.dimen.dp_10));
                break;
            case FontSettingsDialog.FONT_LARGE:
                mTvNewsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelOffset(R.dimen.dp_9));
                break;
            case FontSettingsDialog.FONT_NORMAL://正常
                mTvNewsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelOffset(R.dimen.dp_8));
                break;
            case FontSettingsDialog.FONT_SMALL://小
                mTvNewsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelOffset(R.dimen.dp_7));
                break;
        }
        mWebView.getSettings().setTextZoom(fontSize);
    }

    /**
     * 设置页面内容的字体大小
     *
     * @param result    数据库操作是否成功
     * @param favorFlag 收藏标示
     * @param showToast 是否需要吐司提示
     */
    @Override
    public void setFavorFlag(boolean result, boolean favorFlag, boolean showToast) {
        if (result) {
            mFavorFlag = favorFlag;
            invalidateOptionsMenu();
            if (showToast) {
                int resId = mFavorFlag ? R.string.favorite_success : R.string.favorite_cancel;
                ToastUtils.showS(mContext, resId);
            }
        }
    }

    /**
     * 初始化选项菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expressreader_menu_hotspot_detail, menu);
        return true;
    }

    //该方法在显示OptionMenu前会被调用，所以我们可以这样实现
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        LogUtils.d(TAG, "onPrepareOptionsMenu: >>> queryByNewsId >>> " + mFavorFlag);
        MenuItem bookmarkAction = menu.findItem(R.id.menu_action_bookmark);
        if (mFavorFlag) {
            bookmarkAction.setIcon(R.drawable.expressreader_icon_bookmark_active);
        } else {
            bookmarkAction.setIcon(R.drawable.expressreader_icon_bookmark_normal);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.menu_action_font) {
            //调整显示字号,弹出底部对话框，选择字号选项并进行设置
            if (mFontOptionDialog == null) {
                mFontOptionDialog = new FontSettingsDialog(mContext);
                mFontOptionDialog.setOnFontOptionListener(new OnFontOptionListener() {
                    @Override
                    public void onFontSelected(int fontOption) {
                        setFontSize(fontOption);
                    }
                });
            }
            if (!mFontOptionDialog.isShowing()) {
                mFontOptionDialog.show();
            }
        } else if (i == R.id.menu_action_bookmark) {
            //点击收藏 or 取消
            if (mHotSpot != null) {
                if (mFavorFlag) {
                    mDetailPresenter.deleteNIDByType(mHotSpot.getNID(), SQLConstant.DB_TYPE_FAVORITE);
                } else {
                    mDetailPresenter.insert(mHotSpot, SQLConstant.DB_TYPE_FAVORITE);
                }
            }
        } else if (i == R.id.menu_action_share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, mHotSpot.getNewsIconUrl());
            intent.setType("image/png");
            startActivity(intent);
        }
        return true;
    }

    private void initTextToSpeech() {
        mTtsClient = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    LogUtils.d(TAG, "onInit: TextToSpeech success...");
                    //设置语言模式
                    Locale locale = LocaleHelper.getLocale(mContext);
                    String language = locale.getLanguage();
                    String country = locale.getCountry();

                    LogUtils.d(TAG, "onInit: language >>> " + language + "  country >>> " + country);

                    int languageAvailable = mTtsClient.isLanguageAvailable(locale);
                    LogUtils.d(TAG, "onInit: languageAvailable >>> " + languageAvailable);
                    if (languageAvailable == TextToSpeech.LANG_NOT_SUPPORTED || languageAvailable == TextToSpeech.LANG_MISSING_DATA) {
                        ToastUtils.showS(mContext, "当前引擎不支持中文语系，请检查...");
                        mTtsClient.setLanguage(Locale.ENGLISH);
                    } else {
                        LogUtils.d(TAG, "onInit: 支持当前语系 >>>");
                        mTtsClient.setLanguage(locale);
                    }

                    if (!TextUtils.isEmpty(httpContext)) {
                        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                        mTtsClient.setPitch(1.0f);
//                        // 设置语速
//                        mTtsClient.setSpeechRate(1.0f);
                        //开始播放
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mTtsClient.speak(httpContext, TextToSpeech.QUEUE_ADD, null, null);
                        } else {
                            mTtsClient.speak(httpContext, TextToSpeech.QUEUE_ADD, null);
                        }
                    }
                } else {
                    ToastUtils.showS(mContext, "初始化语音引擎失败..");
                }
            }
        });

    }

    /**
     * WebView客户端对象
     */
    private class ErWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtils.i(TAG, "onPageStarted >>> ");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mJsToAndroid.setHtmlElementStyle(mWebView);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //防止当前应用采用系统自带的浏览器打开，需要复写此方法；
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }

    //全屏监听对象
    WebChromeClient.CustomViewCallback mCallback;
    View mCustomView;

    private class ErWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            LogUtils.d(TAG, "onProgressChanged: newProgress >>> " + newProgress);
        }

        //H5请求全屏时会调用此方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            LogUtils.d(TAG, "onShowCustomView: >>>");
            requestFullScreen(true);
            mCustomView = view;
            mCallback = callback;
            if (mCustomView != null) {
                mFlVideoView.addView(mCustomView);
            }
        }

        //H5推出全屏时会调用
        @Override
        public void onHideCustomView() {
            LogUtils.d(TAG, "onHideCustomView: >>>");
            requestFullScreen(false);
            if (mCustomView != null) {
                mFlVideoView.removeView(mCustomView);
                mCustomView = null;
            }
            if (mCallback != null) {
                mCallback.onCustomViewHidden();
                mCallback = null;
            }
        }
    }

    private void requestFullScreen(boolean fullScreen) {
        if (fullScreen) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mNestedScrollView.setVisibility(View.GONE);
            mToolBar.setVisibility(View.GONE);
            mFlVideoView.setVisibility(View.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            mNestedScrollView.setVisibility(View.VISIBLE);
            mToolBar.setVisibility(View.VISIBLE);
            mFlVideoView.setVisibility(View.GONE);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendEvent();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: 地理位置权限缺失 ...");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //当前NestedScrollView是否可以置顶
        if (mNestedScrollView.canScrollVertically(-1)) {
            mNestedScrollView.fullScroll(View.FOCUS_UP);
        } else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        hasSendEvent = false;

        if (mJsToAndroid != null) {
            mJsToAndroid = null;
        }
        //解决WebView带来的内存泄漏问题
        if (mWebView != null) {
            mWebView.setWebViewClient(null);
            mWebView.setWebChromeClient(null);
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }

        if (mTtsClient != null) {
            mTtsClient.stop();
            mTtsClient.shutdown();
        }
        super.onDestroy();
    }

}
