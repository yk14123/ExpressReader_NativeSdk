package com.cnpeak.expressreader.view.magazine.magazine_detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseActivity;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.OnFontOptionListener;
import com.cnpeak.expressreader.model.bean.IssueUnit;
import com.cnpeak.expressreader.model.db.SQLConstant;
import com.cnpeak.expressreader.permission.PermissionHelper;
import com.cnpeak.expressreader.utils.LocaleHelper;
import com.cnpeak.expressreader.utils.LogUtils;
import com.cnpeak.expressreader.utils.SpUtil;
import com.cnpeak.expressreader.utils.ToastUtils;
import com.cnpeak.expressreader.utils.UIHelper;
import com.cnpeak.expressreader.view.dialog.FontSettingsDialog;
import com.cnpeak.expressreader.view.news.news_image.ImageDetailAdapter;
import com.cnpeak.expressreader.view.widget.ZoomViewPaper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnTwoLevelListener;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import java.util.List;

/**
 * IssueUnit单元详情页面
 *
 * @author HUANG JIN on 2018/11/15
 * @version 1.0
 */
public class MagazineDetailActivity extends BaseActivity<MagazineDetailView, MagazineDetailPresenter>
        implements MagazineDetailView {
    private static final String TAG = MagazineDetailActivity.class.getSimpleName();
    //控制器
    private MagazineDetailPresenter mMagazineDetailPresenter;
    //页面跳转参数
    private String mMnId;

    //一楼刷新视图
    private SmartRefreshLayout mSmartRefreshLayout;

    //二楼pages视图
    private TwoLevelHeader mTwoLevelHeader;
    private LinearLayout mRlTwoLevelHeader;
    private ZoomViewPaper mViewPages;
    private RelativeLayout mRlTwoLevelTitle;
    private ImageView mIvSlideUp;
    private TextView mTvHeaderTitle;
    private TextView mTvHeaderNo;
    private TextView mTvHeaderName;
    private TextView mTvHeaderPublishDate;
    /**
     * 操作action
     */
    private LinearLayout mLlTitleBar;
    private ImageView mIvBackPress;
    private ImageView mIvFonSetting;
    private ImageView mIvFavorite;
    private ImageView mIvReviewUnits;
    //图片滑动浏览
    private ViewPager mIssueImagePaper;
    /**
     * 新闻标题部分
     */
    private ImageView mIvSlideDown;//下拉提示控件
    private TextView mTvIssueTitle; //杂志标题
    private TextView mTvIssueNo;//期号
    private TextView mTvMagazineName;//杂志名称
    private TextView mTvIssuePublishDate;//发表时间
    //内容
    private WebView mWvContent;
    //杂志数据bean
    private IssueUnit mIssueUnit;
    //是否保存在数据库中
    private boolean mFavorFlag;
    //是否保存在历史记录数据库中
    private boolean mHistoryFlag;
    //当前二楼pages视图是否打开
    private boolean mTwoLevelOpen = false;
    //当前是否向服务器提交过统计事件
    private boolean hasSendEvent = false;
    //调节字体选择框
    private FontSettingsDialog mFontDialog;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.expressreader_activity_magazine_detail;
    }

    @Override
    protected void getExtraFromBundle(Intent intent) {
        super.getExtraFromBundle(intent);
        if (intent != null) {
            mMnId = intent.getStringExtra(ErConstant.MN_ID);
            LogUtils.i(TAG, "MnId >>> " + mMnId);
        }
    }

    @Override
    protected MagazineDetailPresenter initPresenter() {
        mMagazineDetailPresenter = new MagazineDetailPresenter(this);
        mMagazineDetailPresenter.queryTypeByMnId(mMnId);
        return mMagazineDetailPresenter;
    }


    @Override
    public void initView() {
        //初始化标题栏
        initTitleBar();
        //初始化刷新容器
        initSmartRefreshLayout();
        //初始化二楼数据
        initSecondFloor();
        //刷新杂志详情数据
        initMagazineDetail();
        //刷新内容页面
        initWebView();
        //获取IssueUnit视图数据
        initIssueUnit();
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
        mMagazineDetailPresenter.sentIssueUnitClickEvent(mMnId, locationInfo[0],
                locationInfo[1], LocaleHelper.getLCID(mContext));
        hasSendEvent = true;
    }

    private void initTitleBar() {
        mLlTitleBar = findViewById(R.id.ll_magazine_detail_toolbar);
        mIvBackPress = findViewById(R.id.iv_action_back_press);
        mIvBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvFonSetting = findViewById(R.id.iv_action_font_setting);
        mIvFonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFontDialog == null) {
                    mFontDialog = new FontSettingsDialog(mContext);
                    mFontDialog.setOnFontOptionListener(new OnFontOptionListener() {
                        @Override
                        public void onFontSelected(int fontOption) {
                            setFontSize(fontOption);
                        }
                    });
                }
                mFontDialog.show();
            }
        });
        mIvFavorite = findViewById(R.id.iv_action_favorite_unit);
        mIvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFavorFlag) {
                    mMagazineDetailPresenter.deleteNIDByType(mMnId, SQLConstant.DB_TYPE_FAVORITE);
                } else {
                    mMagazineDetailPresenter.insert(mIssueUnit, SQLConstant.DB_TYPE_FAVORITE);
                }
            }
        });
        mIvReviewUnits = findViewById(R.id.iv_action_review_units);
        mIvReviewUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoLevelOpen) {
                    mTwoLevelHeader.finishTwoLevel();
                } else {
                    mTwoLevelHeader.onStateChanged(mSmartRefreshLayout,
                            RefreshState.TwoLevelFinish,
                            RefreshState.TwoLevelReleased);
                }
            }
        });
    }

    private void initMagazineDetail() {
        mIssueImagePaper = findViewById(R.id.vp_magazine_content_images);
        mIvSlideDown = findViewById(R.id.iv_issue_detail_slide_down);
        mTvIssueTitle = findViewById(R.id.tv_magazine_content_title);
        mTvIssueNo = findViewById(R.id.tv_magazine_content_issueNo);
        mTvMagazineName = findViewById(R.id.tv_magazine_content_newsName);
        mTvIssuePublishDate = findViewById(R.id.tv_magazine_content_publishDate);
    }

    /**
     * 初始化WebView控件
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mWvContent = findViewById(R.id.wv_magazine_content);
        mWvContent.setFocusable(false);
        mWvContent.setVerticalScrollBarEnabled(false);//不能垂直滑动
        mWvContent.setHorizontalScrollBarEnabled(false);//不能水平滑动
        WebSettings settings = mWvContent.getSettings();
        //settings.setUseWideViewPort(true);//调整到适合webView的大小，不过尽量不要用，有些手机有问题
        settings.setLoadWithOverviewMode(true);//设置WebView是否使用预览模式加载界面。
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);//设置js可用
        //初始化默认值
        int fontOption = SpUtil.getInt(mContext, ErConstant.FONT_OPTION, FontSettingsDialog.FONT_NORMAL);
        setFontSize(fontOption);
    }

    private void initSecondFloor() {
        mTwoLevelHeader = findViewById(R.id.tll_magazine_detail_header);
        mTwoLevelHeader.setOnTwoLevelListener(new OnTwoLevelListener() {
            @Override
            public boolean onTwoLevel(@NonNull RefreshLayout refreshLayout) {
                ToastUtils.showS(mContext, "开启预览杂志模式 ");
                mRlTwoLevelHeader.setVisibility(View.VISIBLE);
                mTwoLevelOpen = true;
                mIvReviewUnits.setImageResource(R.drawable.expressreader_icon_close_shadow);
                mRlTwoLevelTitle.setAlpha(1);
                return true;//true 将会展开二楼状态 false 关闭刷新
            }
        });
        mViewPages = findViewById(R.id.vp_magazine_detail_pages);
        mRlTwoLevelHeader = findViewById(R.id.ll_magazine_pages_root);
        //当前杂志标题所占位置
        mRlTwoLevelTitle = findViewById(R.id.rl_magazine_pages_title);
        mIvSlideUp = findViewById(R.id.iv_magazine_pages_slide_down);
        mTvHeaderTitle = findViewById(R.id.tv_magazine_pages_title);
        mTvHeaderNo = findViewById(R.id.tv_magazine_pages_issueNo);
        mTvHeaderName = findViewById(R.id.tv_magazine_pages_newsName);
        mTvHeaderPublishDate = findViewById(R.id.tv_magazine_pages_publishDate);
    }

    private void initSmartRefreshLayout() {
        mSmartRefreshLayout = findViewById(R.id.srl_magazine_detail);
        mSmartRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Log.d(TAG, "onRefresh: >>> ");
                refreshLayout.finishRefresh();
            }

            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent,
                                       int offset, int headerHeight, int maxDragHeight) {
                Log.d(TAG, "onHeaderMoving: alpha >>> " + (1 - Math.min(percent, 1)));
                mIvBackPress.setAlpha(1 - Math.min(percent, 1));
                mIvFonSetting.setAlpha(1 - Math.min(percent, 1));
                mIvFavorite.setAlpha(1 - Math.min(percent, 1));
                mIvReviewUnits.setAlpha(1 - Math.min(percent, 1));
                mRlTwoLevelHeader.setTranslationY(Math.min(offset - mRlTwoLevelHeader.getHeight() + mLlTitleBar.getHeight(),
                        mSmartRefreshLayout.getLayout().getHeight() - mRlTwoLevelHeader.getHeight()));
            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                Log.d(TAG, "onStateChanged: newState >>>  " + newState + "   oldState >>>" + oldState);
                if (oldState == RefreshState.TwoLevelFinish) {
                    mTwoLevelOpen = false;
                    mRlTwoLevelTitle.setAlpha(0);
                    mIvReviewUnits.setImageResource(R.drawable.expressreader_magazine_mark);
                }
                mIvReviewUnits.setAlpha(1f);//重置图标资源之后再次出现
                super.onStateChanged(refreshLayout, oldState, newState);
            }
        });
    }

    private void initIssueUnit() {
        mMagazineDetailPresenter.getIssueUnit(mMnId);
    }

    @Override
    public void getIssueUnit(IssueUnit issueUnit) {
        LogUtils.i(TAG, "getIssueUnit success issueUnit>>> " + issueUnit);
        if (issueUnit != null) {
            this.mIssueUnit = issueUnit;
            if (!mHistoryFlag) {
                mMagazineDetailPresenter.insert(mIssueUnit, SQLConstant.DB_TYPE_HISTORY);
            }
            //发表时间
            String publishDate = mIssueUnit.getPublishDate();
            if (!TextUtils.isEmpty(publishDate)) {
                mTvIssuePublishDate.setText(publishDate);
                mTvHeaderPublishDate.setText(publishDate);
            }
            //新闻标题
            String title = mIssueUnit.getTitle();
            if (!TextUtils.isEmpty(title)) {
                mTvIssueTitle.setText(title);
                mTvHeaderTitle.setText(title);
            }
            //新闻源
            String mgName = mIssueUnit.getMgName();
            if (!TextUtils.isEmpty(mgName)) {
                mTvMagazineName.setText(mgName);
                mTvHeaderName.setText(mgName);
            }
            //期数
            String issueNo = mIssueUnit.getIssueNo();
            if (!TextUtils.isEmpty(issueNo)) {
                mTvIssueNo.setText(issueNo);
                mTvHeaderNo.setText(issueNo);
            }
            //HttpContext新闻内容
            String htmlContent = mIssueUnit.getHtmlContent();
            if (!TextUtils.isEmpty(htmlContent)) {
                mWvContent.loadData(htmlContent, "text/html;utf-8", null);
            }
            //图片数据适配
            List<String> images = mIssueUnit.getImages();
            initViewPaper(images);

            List<String> pages = mIssueUnit.getPages();
            if (pages != null && pages.size() != 0) {
                MagazinePagesAdapter adapter = new MagazinePagesAdapter(mContext, pages);
                adapter.setOnPageMatrixChangedListener(new MagazinePagesAdapter.OnPageMatrixChangedListener() {

                    @Override
                    public void onMatrixChanged(RectF rect) {
                        Log.d(TAG, "onMatrixChanged: rect.bottom >>> " + rect.bottom);
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                                mRlTwoLevelTitle.getLayoutParams();
                        layoutParams.height = (int) (rect.bottom);
                        layoutParams.bottomMargin = 40;
                        mViewPages.setLayoutParams(layoutParams);
                    }
                });

                mViewPages.setAdapter(adapter);
            }
        }
    }

    @Override
    public void setFavorFlag(boolean favorFlag, boolean result, boolean showToast) {
        if (result) {
            mFavorFlag = favorFlag;
            if (mFavorFlag) {
                mIvFavorite.setImageResource(R.drawable.expressreader_icon_bookmark_active);
            } else {
                mIvFavorite.setImageResource(R.drawable.expressreader_icon_bookmark_normal);
            }
            if (showToast) {
                int resId = mFavorFlag ? R.string.favorite_success : R.string.favorite_cancel;
                ToastUtils.showS(mContext, resId);
            }
        }
    }

    @Override
    public void setHistoryFlag(boolean historyFlag) {
        mHistoryFlag = historyFlag;
    }

    private void initViewPaper(final List<String> images) {
        if (images != null) {
            ImageDetailAdapter mIssueImgAdapter = new ImageDetailAdapter(mContext, images);
            //设置ViewPager的监听事件
            mIssueImgAdapter.setOnImageClickListener(new ImageDetailAdapter.OnImageClickListener() {
                @Override
                public void onImageClick(int position) {
                    UIHelper.startImageActivity(mContext, images, position, true);
                }
            });
            mIssueImagePaper.setAdapter(mIssueImgAdapter);
        }
    }

    /**
     * 设置页面内容的字体大小
     *
     * @param fontSize 字体大小标识
     */
    private void setFontSize(int fontSize) {
        switch (fontSize) {
            case FontSettingsDialog.FONT_LARGEST:
                mTvIssueTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        getResources().getDimensionPixelOffset(R.dimen.dp_10));
                break;
            case FontSettingsDialog.FONT_LARGE:
                mTvIssueTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        getResources().getDimensionPixelOffset(R.dimen.dp_9));
                break;
            case FontSettingsDialog.FONT_NORMAL://正常
                mTvIssueTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        getResources().getDimensionPixelOffset(R.dimen.dp_8));
                break;
            case FontSettingsDialog.FONT_SMALL://小
                mTvIssueTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        getResources().getDimensionPixelOffset(R.dimen.dp_7));
                break;
        }
        mWvContent.getSettings().setTextZoom(fontSize);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendEvent();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: 位置权限缺失...");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        hasSendEvent = false;
        super.onDestroy();
    }
}