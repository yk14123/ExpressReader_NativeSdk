package com.cnpeak.expressreader.view.news.news_detail;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.cnpeak.expressreader.tts.Synthesizer;
import com.cnpeak.expressreader.tts.Voice;
import com.cnpeak.expressreader.utils.GlideCircleTransform;
import com.cnpeak.expressreader.utils.LogUtils;
import com.cnpeak.expressreader.utils.SpUtil;
import com.cnpeak.expressreader.utils.ToastUtils;
import com.cnpeak.expressreader.utils.UIHelper;
import com.cnpeak.expressreader.view.dialog.FontSettingsDialog;
import com.cnpeak.expressreader.view.widget.web.ActionWebCallback;
import com.cnpeak.expressreader.view.widget.web.ActionWebView;
import com.cnpeak.expressreader.view.widget.web.JsToAndroid;

import java.io.Serializable;
import java.util.List;

/**
 * @author builder by HUANG JIN on 18/11/12
 * @version 1.0
 * 热点资讯详情加载页面
 */
public class NewsDetailActivity extends BaseActivity<NewsDetailView, NewsDetailPresenter>
        implements NewsDetailView, ActionWebCallback {
    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    //控制器
    private NewsDetailPresenter mDetailPresenter;
    //滑动组件
    private NestedScrollView mNestedScrollView;
    private TextView mTvNewsTitle;
    private Toolbar mToolBar;
    //WebView
    private FrameLayout mFlVideoView;
    private ActionWebView mWebView;
    //HotSpot实体Bean对象
    private HotSpot mHotSpot;
    //底部字号选择框
    private FontSettingsDialog mFontOptionDialog;
    //是否被收藏
    private boolean mFavorFlag;

    // 语音合成对象
    private Synthesizer mSynthesizer;
    //文字转语音播报
    private String mHttpContext;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        //页面暂停状态，合成暂停
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //销毁当前合成对象
        if (mSynthesizer != null) {
            mSynthesizer.stopSound();
        }
        //解决WebView带来的内存泄漏问题
        if (mWebView != null) {
            mWebView.releaseAction();
            mWebView.setWebViewClient(null);
            mWebView.setWebChromeClient(null);
            mWebView.loadDataWithBaseURL(null, "",
                    "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

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
        //初始化TTS客户端
//        initTtsClient();
    }

    private void initTtsClient() {
        // 初始化合成对象
        Voice voice = new Voice.Builder()
                .setLang("zh-CN")
                .setGender("Female")
                .setRate("-10.00%")
                .setVoiceName("Microsoft Server Speech Text to Speech Voice (zh-CN, XiaoxiaoNeural)")
                .build();
        mSynthesizer = Synthesizer.getDefault(voice);
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

            mHttpContext = mHotSpot.getHttpContext();
            if (!TextUtils.isEmpty(mHttpContext)) {
                mWebView.loadData(mHttpContext, "text/html; charset=UTF-8", null);
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
        //是否显示自定义菜单
        mWebView.isCustomMenu(false);
        //Js调用对象
        mWebView.addJsInterface(new JsToAndroid(this), "JsToAndroid");//设置js接口

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
    public void onFavorStateChanged(boolean result, boolean favorFlag, boolean showToast) {
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
//        else if (i == R.id.menu_action_tts) {
//            if (mSynthesizer != null) {
//                mSynthesizer.speakToAudio(mHttpContext);
//            }
//        }
        return true;
    }

    @Override
    public void onZoomPicture(List<String> imgUrls, int defaultIndex) {
        UIHelper.startImageActivity(mContext, imgUrls, defaultIndex);
    }

    @Override
    public void onTranslate(int menuId, String content) {
        Log.d(TAG, "onTranslate: menuId >>> " + menuId);
        Log.d(TAG, "onTranslate: content >>> " + content);
        switch (menuId) {
//            case 0: //实现内容翻译功能
//                mDetailPresenter.translateText(content, this);
//                break;
            case 1: //复制到剪贴板中
                ClipboardManager clipboardManager = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                //setText()已经过时，采用新的API
                clipboardManager.setPrimaryClip(ClipData.newPlainText("", content));
                ToastUtils.showS(mContext, "复制成功");
                break;
        }
    }

    @Override
    public void onPlayVideo(boolean fullScreen, View customView) {
        if (fullScreen) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mNestedScrollView.setVisibility(View.GONE);
            mToolBar.setVisibility(View.GONE);
            mFlVideoView.setVisibility(View.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            mNestedScrollView.setVisibility(View.VISIBLE);
            mToolBar.setVisibility(View.VISIBLE);
            mFlVideoView.setVisibility(View.GONE);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (fullScreen) {
            if (customView != null) {
                mFlVideoView.addView(customView);
            }
        } else {
            if (customView != null) {
                mFlVideoView.removeView(customView);
            }
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


}
