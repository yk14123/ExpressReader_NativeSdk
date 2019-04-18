package com.cnpeak.expressreader.view.newspaper.paper_news;

import android.content.Intent;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.cnpeak.expressreader.interf.LoadingType;
import com.cnpeak.expressreader.utils.ToastUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseActivity;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.OnItemClickListener;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.bean.PaperCover;
import com.cnpeak.expressreader.utils.GlideCircleTransform;
import com.cnpeak.expressreader.utils.LocaleHelper;
import com.cnpeak.expressreader.utils.UIHelper;
import com.cnpeak.expressreader.view.dialog.ErLoadingDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author builder by HUANG JIN on 18/12/27
 * @version 1.0
 * 报纸简介列表页面
 */
public class PaperNewsActivity extends BaseActivity implements PaperNewsView {
    private PaperNewsPresenter mPaperNewsPresenter;
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;
    private TextView mTvPaperTitle;//报社名称
    /**
     * RecyclerView
     */
    private SmartRefreshLayout mSmartRefreshLayout;//下拉刷新控件
    private PaperNewsAdapter mPaperNewsAdapter;//适配器
    //网络加载框
    private ErLoadingDialog mLoadingDialog;
    //是否为加载更多标志
    private boolean isLoadMore = false;
    //数据bean
    private PaperCover mPageCoverBean;
    //语系参数
    private String mLCID;
    //当前加载的页面索引
    private int mCurrentPageIndex = 1;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.expressreader_activity_paper_news;
    }

    @Override
    protected BasePresenter initPresenter() {
        return mPaperNewsPresenter = new PaperNewsPresenter(this);
    }

    @Override
    protected void getExtraFromBundle(Intent intent) {
        super.getExtraFromBundle(intent);
        Serializable extra = intent.getSerializableExtra(ErConstant.PAPER_COVER);
        if (extra instanceof PaperCover) {
            mPageCoverBean = (PaperCover) extra;
        }
        mLCID = LocaleHelper.getLCID(mContext);
    }

    @Override
    public void initView() {
        //下拉刷新
        initRefreshLayout();
        //初始化标题栏
        initAppBar();
        //新闻数据列表
        initRecyclerView();
        //初始化报社当前的新闻列表
        showLoading();
        initPaperNews();
    }

    private void initRefreshLayout() {
        mSmartRefreshLayout = findViewById(R.id.srl_newspaper_layout);
        //下拉刷新事件
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = false;
                mCurrentPageIndex = 1;
                initPaperNews();
            }
        });
        //上拉加载事件
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = true;
                mCurrentPageIndex++;
                initPaperNews();
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView mPaperNewsRecycler = findViewById(R.id.rv_newspaper_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mPaperNewsRecycler.setLayoutManager(mLinearLayoutManager);
        mPaperNewsRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = 20;
                }
            }
        });

        List<HotSpot> mNewsPapers = new ArrayList<>();
        mPaperNewsAdapter = new PaperNewsAdapter(this, mNewsPapers);
        mPaperNewsAdapter.setOnItemClickListener(new OnItemClickListener<HotSpot>() {
            @Override
            public void onItemClick(HotSpot hotSpot) {
                if (hotSpot != null) {
                    UIHelper.startNewsDetailActivity(mContext, hotSpot);
                }
            }

            @Override
            public void refresh() {
                initPaperNews();
            }
        });
        //初始化报社头部说明信息
        initPaperHeader();
        mPaperNewsRecycler.setAdapter(mPaperNewsAdapter);
    }

    private void initPaperHeader() {
        if (mPageCoverBean != null) {
            //报社名称
            String newsName = mPageCoverBean.getBewrite();
            mTvPaperTitle.setText(newsName);
            String paperDescription = mPageCoverBean.getPaperDescription();
            //加载圆形icon
            String paperLogoImgUrl = mPageCoverBean.getPaperLogoImgUrl();
            Glide.with(this)
                    .load(paperLogoImgUrl)
                    .apply(new RequestOptions()
                            .error(R.drawable.expressreader_news_default_head)
                            .placeholder(R.drawable.expressreader_news_default_head)
                            .transform(new GlideCircleTransform(mContext)))
                    .into(mFloatingActionButton);
            mPaperNewsAdapter.initHeader(paperLogoImgUrl, newsName, paperDescription);
        }
    }

    private void initAppBar() {
        //Toolbar
        mToolbar = findViewById(R.id.tb_newspaper_bar);
        mToolbar.setTitle(" ");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //AppBarLayout
        AppBarLayout mAppBarLayout = findViewById(R.id.abl_newspaper_wrapper);
        //FloatingActionBar
        mFloatingActionButton = findViewById(R.id.fab_newspaper_list);
        //标题
        mTvPaperTitle = findViewById(R.id.tv_newspaper_title);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                switch (state) {
                    case COLLAPSED://折叠
                        mToolbar.setNavigationIcon(R.drawable.expressreader_icon_arrow_left);
                        mFloatingActionButton.hide();
                        mTvPaperTitle.setVisibility(View.VISIBLE);
                        break;
                    case EXPANDED://展开
                        mToolbar.setNavigationIcon(R.drawable.expressreader_icon_back_white);
                        mFloatingActionButton.show();
                        mTvPaperTitle.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    /**
     * 加载报社新闻数据
     */
    private void initPaperNews() {
        mPaperNewsAdapter.resetLoadingType(LoadingType.LOADING_REFRESH);
        mPaperNewsPresenter.getNewsPaper(mLCID, mPageCoverBean.getNewsID(), mCurrentPageIndex);
    }

    @Override
    public void getNewsPaper(List<HotSpot> newsPapers) {
        if (!isLoadMore && newsPapers.size() == 0) {
            finishLoading(false);
            mPaperNewsAdapter.resetLoadingType(LoadingType.LOADING_ERROR);
        } else
            finishLoading(true);
        hideLoading();
        if (newsPapers != null) {
            mPaperNewsAdapter.notifyItemRemoved(mPaperNewsAdapter.getItemCount() + 1);
            mPaperNewsAdapter.updateData(newsPapers, isLoadMore);
        }
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showS(mContext, errorMsg);
        finishLoading(false);
        hideLoading();
        mPaperNewsAdapter.resetLoadingType(LoadingType.LOADING_ERROR);
    }

    /**
     * 停止加载
     *
     * @param success 是否加载成功
     *                true表示成功；false表示失败
     */
    private void finishLoading(boolean success) {
        if (isLoadMore) {
            mSmartRefreshLayout.finishLoadMore(success);
        } else {
            mSmartRefreshLayout.finishRefresh(success);
        }
    }

    private void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ErLoadingDialog(mContext);
            mLoadingDialog.setMessage(mContext.getResources()
                    .getString(R.string.expressreader_data_loading));
        }
        if (!mLoadingDialog.isShowing() && !isFinishing()) {
            mLoadingDialog.show();
        }
    }

    private void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

}

