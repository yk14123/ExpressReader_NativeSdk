package com.cnpeak.expressreader.view.magazine.magazine_units;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseActivity;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.LoadingType;
import com.cnpeak.expressreader.model.bean.IssueList;
import com.cnpeak.expressreader.model.bean.IssueLite;
import com.cnpeak.expressreader.model.bean.MagazineList;
import com.cnpeak.expressreader.utils.UIHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 杂志单元列表显示界面
 * 杂志单元包括大图、小图、热门、封面故事等类型
 *
 * @author HUANG JIN on 2018/11/13
 * @version 1.0
 */
public class MagazineUnitActivity extends BaseActivity<MagazineUnitView, MagazineUnitPresenter> implements MagazineUnitView {
    private static final String TAG = MagazineUnitActivity.class.getSimpleName();
    //控制器
    private MagazineUnitPresenter mMagazineUnitPresenter;
    //刷新布局
    private SmartRefreshLayout mSmartRefreshLayout;
    /**
     * 数据列表显示
     */
    private RecyclerView mMagazineUnitRecycler;//recyclerView
    private MagazineUnitAdapter mMagazineIssueAdapter;//适配器
    private MagazineList mMagazineList;//IssueListBean实体对象
    private List<IssueList> mIssueList;
    //下一页
    private int mNextPageIndex = 0;
    //上一页
    private int mPrePageIndex = 0;
    //是否为加载更多
    private int mLoadingType = LoadingType.LOADING_REFRESH;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.expressreader_activity_magazine_units;
    }

    @Override
    protected void getExtraFromBundle(Intent intent) {
        super.getExtraFromBundle(intent);
        if (intent != null) {
            Serializable serializableExtra = intent.getSerializableExtra(ErConstant.ISSUE_LIST);
            if (serializableExtra instanceof MagazineList) {
                mMagazineList = (MagazineList) serializableExtra;
                mIssueList = mMagazineList.getIssueList();
            }
        }
    }

    @Override
    protected MagazineUnitPresenter initPresenter() {
        return mMagazineUnitPresenter = new MagazineUnitPresenter(this);
    }

    @Override
    public void initView() {
        //初始化菜单按钮
        initTitleBar();
        //初始化刷新布局
        initSmartRefreshLayout();
        //初始化RecyclerView
        initRecyclerView();
        //初始化IssueUnit数据
        initIssueLite();
    }

    private void initTitleBar() {
        //返回键
        ImageView ivBack = findViewById(R.id.iv_magazine_unit_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                onBackPressed();
            }
        });
        //查看往期杂志
        ImageView ivReview = findViewById(R.id.iv_magazine_unit_review);
        ivReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.startMagazineReviewActivity(MagazineUnitActivity.this, mMagazineList);
            }
        });
    }

    private void initSmartRefreshLayout() {
        mSmartRefreshLayout = findViewById(R.id.srl_magazine_unit);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mPrePageIndex > 0) {
                    //请求上一个数据
                    mLoadingType = LoadingType.LOADING_PRE;
                    mPrePageIndex--;
                    Log.d(TAG, "onRefresh: mPrePageIndex >>> " + mPrePageIndex);
                    mMagazineUnitPresenter.getIssueLiteById(mIssueList, mPrePageIndex);
                } else {
                    mLoadingType = LoadingType.LOADING_REFRESH;
                    Log.d(TAG, "onRefresh: mPrePageIndex >>> " + mPrePageIndex);
//                    mMagazineUnitPresenter.getIssueLiteById(mIssueList, mPrePageIndex);
                    mSmartRefreshLayout.finishRefresh();
                }
            }
        });

        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Log.d(TAG, "onLoadMore: mIssueList.size() >>> " + mIssueList.size());
                if (mNextPageIndex < mIssueList.size() - 1) {
                    mLoadingType = LoadingType.LOADING_NEXT;
                    //请求下一个往期数据
                    mNextPageIndex++;
                    Log.d(TAG, "onLoadMore: mNextPageIndex >>> " + mNextPageIndex);
                    mMagazineUnitPresenter.getIssueLiteById(mIssueList, mNextPageIndex);
                }
            }
        });
    }

    private void initRecyclerView() {
        mMagazineUnitRecycler = findViewById(R.id.rv_magazine_unit);
        //初始化RecyclerView
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mMagazineUnitRecycler.setLayoutManager(mLinearLayoutManager);
        mMagazineUnitRecycler.setHasFixedSize(true);
        LinkedList<IssueLite> mLinkedIssueLite = new LinkedList<>();
        mMagazineIssueAdapter = new MagazineUnitAdapter(mContext, mLinkedIssueLite);
        mMagazineIssueAdapter.setOnIssueItemClickListener(new MagazineUnitTypeAdapter.OnIssueItemClickListener() {
            @Override
            public void onNormalType(String MnId) {
                //点击unit单元回调事件 --->跳转详情展示页面
                UIHelper.startMagazineDetailActivity(mContext, MnId);
            }

            @Override
            public void onCoverType(String coverUrl) {
                //点击封面事件
                ArrayList<String> urls = new ArrayList<>(1);
                urls.add(coverUrl);
                UIHelper.startImageActivity(mContext, urls, -1);
            }
        });
        mMagazineUnitRecycler.setAdapter(mMagazineIssueAdapter);
    }

    private void initIssueLite() {
        mMagazineUnitPresenter.getIssueLiteById(mIssueList, mNextPageIndex);
    }

    @Override
    public void getIssueLite(IssueLite issueLite) {
        if (mLoadingType == LoadingType.LOADING_NEXT) {
            if (mNextPageIndex == mIssueList.size() - 1) {
                mSmartRefreshLayout.finishLoadMoreWithNoMoreData();
            } else
                mSmartRefreshLayout.finishLoadMore();
        } else {
            mSmartRefreshLayout.finishRefresh();
        }
        if (issueLite != null) {
            mMagazineIssueAdapter.addIssueLite(issueLite, mLoadingType);
            if (mLoadingType == LoadingType.LOADING_PRE) {
                mMagazineUnitRecycler.scrollToPosition(0);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ErConstant.MAGAZINE_UNIT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String issueId = data.getStringExtra(ErConstant.ISSUE_ID);
                    int index = data.getIntExtra(ErConstant.ISSUE_INDEX, 0);
                    if (!TextUtils.isEmpty(issueId)) {
                        //重置起始位置
                        mNextPageIndex = mPrePageIndex = index;
                        Log.d(TAG, "onActivityResult: mPrePageIndex >>> " + mPrePageIndex);
                        //重置刷新动作
                        mSmartRefreshLayout.setNoMoreData(mNextPageIndex == mIssueList.size() - 1);
                        mLoadingType = LoadingType.LOADING_REFRESH;
                        mMagazineUnitPresenter.getIssueLite(issueId);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mMagazineUnitRecycler != null && mMagazineUnitRecycler.canScrollVertically(-1)) {
            mMagazineUnitRecycler.scrollToPosition(0);
        } else
            super.onBackPressed();
    }
}
