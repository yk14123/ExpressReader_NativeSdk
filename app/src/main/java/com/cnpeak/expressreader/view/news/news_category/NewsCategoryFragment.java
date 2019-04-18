package com.cnpeak.expressreader.view.news.news_category;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseFragment;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.LoadingType;
import com.cnpeak.expressreader.interf.OnItemClickListener;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.utils.LogUtils;
import com.cnpeak.expressreader.utils.ToastUtils;
import com.cnpeak.expressreader.utils.UIHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的热点资讯分类展现Fragment页
 */
public class NewsCategoryFragment extends BaseFragment implements NewsCategoryView {
    private static final String TAG = "NewsCategoryFragment";
    //控制器
    private NewsCategoryPresenter mCategoryNewsPresenter;
    //下拉刷新控件
    private SmartRefreshLayout mSmartRefreshLayout;
    /**
     * RecyclerView控件
     */
    private RecyclerView mCategoryNewsRecycler;//列表展示控件
    private NewsCategoryAdapter mCategoryNewsAdapter; //适配器
    /**
     * 请求参数
     */
    private int mCurrentPageIndex = 1;//当前page索引
    private String mChannelId;//渠道ID:当前分类的id
    private String mNewsId; //NID字段
    private String mKeyword; //关键字：参数不必须
    //是否为加载更多
    private boolean isLoadMore = false;

    public NewsCategoryFragment() {
    }

    /**
     * 创建一个Fragment实例
     *
     * @param channelId 渠道ID
     * @param newsId    新闻ID
     * @param keyword   关键字
     * @return NewsCategoryFragment
     */
    public static NewsCategoryFragment newInstance(@NonNull String channelId, String newsId, String keyword) {
        NewsCategoryFragment fragment = new NewsCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ErConstant.CHANNEL_ID, channelId);
        args.putString(ErConstant.NEWS_ID, newsId);
        args.putString(ErConstant.KEYWORD, keyword);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getFragmentLayoutID() {
        return R.layout.expressreader_fragment_news_content;
    }

    @Override
    protected BasePresenter createFragmentPresenter() {
        return mCategoryNewsPresenter = new NewsCategoryPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Keyword >>>" + mKeyword);
        //获取相关的参数
        Bundle bundle = getArguments();
        if (bundle != null) {
            mChannelId = bundle.getString(ErConstant.CHANNEL_ID);
            mNewsId = bundle.getString(ErConstant.NEWS_ID);
            mKeyword = bundle.getString(ErConstant.KEYWORD);
        }
    }

    @Override
    public void findView() {
        Log.d(TAG, "findView Keyword >>>" + mKeyword);
        //初始化SwipeRefreshLayout
        initRefreshLayout();
        //初始化RecyclerView
        initRecyclerView();
    }

    @Override
    protected void lazyFetchData() {
        Log.d(TAG, "lazyFetchData: Keyword>>> " + mKeyword);
        //初始化数据
        initCategoryNews();
    }

    private void initRefreshLayout() {
        mSmartRefreshLayout = mContentView.findViewById(R.id.srl_fragment_hotspot);
        //下拉刷新事件
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                LogUtils.i(TAG, "onRefresh 下拉刷新将mCurrentPageIndex置为初始值 >>> ");
                isLoadMore = false;
                mCurrentPageIndex = 1;
                initCategoryNews();
            }
        });

        //上拉加载更多
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = true;
                mCurrentPageIndex++;
                LogUtils.i(TAG, "onScrollStateChanged loadMore >>> " + mCurrentPageIndex);
                //发送网络请求获取更多数据
                initCategoryNews();
            }
        });
    }

    private void initRecyclerView() {
        mCategoryNewsRecycler = mContentView.findViewById(R.id.rv_fragment_hotspot_content);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(
                mContext, RecyclerView.VERTICAL, false);
        mCategoryNewsRecycler.setLayoutManager(mLinearLayoutManager);
        mCategoryNewsRecycler.setHasFixedSize(true);
        mCategoryNewsRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                outRect.top = 20;
            }
        });
        List<HotSpot> newsList = new ArrayList<>();//数据
        mCategoryNewsAdapter = new NewsCategoryAdapter(mContext, newsList);
        //设置Item的点击监听事件
        mCategoryNewsAdapter.setOnItemClickListener(new OnItemClickListener<HotSpot>() {
            @Override
            public void onItemClick(HotSpot hotSpot) {
                UIHelper.startNewsDetailActivity(mContext, hotSpot);
            }

            @Override
            public void refresh() {
                isLoadMore = false;
                initCategoryNews();
            }
        });
        mCategoryNewsRecycler.setAdapter(mCategoryNewsAdapter);
    }

    /**
     * 获取指定渠道的新闻列表数据
     * 热点渠道（301）的新闻接口与其他渠道的接口不是同一个，需要对渠道进行单独判断
     */
    private void initCategoryNews() {
        mCategoryNewsAdapter.resetLoadingType(LoadingType.LOADING_REFRESH);
        if (TextUtils.isEmpty(mChannelId) || mChannelId.equals(ErConstant.HOTSPOT)) {
            mCategoryNewsPresenter.getNewsList(LCID, mCurrentPageIndex);
        } else {
            mCategoryNewsPresenter.getCategoryNews(LCID, mNewsId,
                    mChannelId, mKeyword, mCurrentPageIndex);
        }
    }

    @Override
    public void getCategoryNews(List<HotSpot> categories) {
        initData(categories);
    }

    @Override
    public void getNewsList(List<HotSpot> newsList) {
        initData(newsList);
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showS(mContext, errorMsg);
        finishLoading(false);
        mCategoryNewsAdapter.resetLoadingType(LoadingType.LOADING_ERROR);
    }

    /**
     * 初始化列表数据
     */
    private void initData(List<HotSpot> newsList) {
        finishLoading(true);
        if (newsList != null) {
            if (isLoadMore) {
                //去除加载更多视图
                mCategoryNewsAdapter.notifyItemRemoved(mCategoryNewsAdapter.getItemCount());
            }
            mCategoryNewsAdapter.updateData(newsList, isLoadMore);
        }
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

    /**
     * Can scroll top boolean.
     * 当前的列表是否置顶
     *
     * @return the boolean
     */
    public boolean canScrollTop() {
        if (mCategoryNewsRecycler != null) {
            //置顶
            mCategoryNewsRecycler.scrollToPosition(0);
            return mCategoryNewsRecycler.canScrollVertically(-1);
        } else
            return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: Keyword >>>" + mKeyword);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Keyword >>>" + mKeyword);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Keyword >>>" + mKeyword);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: Keyword >>>" + mKeyword);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: Keyword >>>" + mKeyword);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Keyword >>>" + mKeyword);
    }

}
