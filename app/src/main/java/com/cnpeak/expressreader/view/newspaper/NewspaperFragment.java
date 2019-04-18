package com.cnpeak.expressreader.view.newspaper;

import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseFragment;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.interf.LoadingType;
import com.cnpeak.expressreader.model.bean.PaperCover;
import com.cnpeak.expressreader.utils.ToastUtils;
import com.cnpeak.expressreader.utils.UIHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * * @author HUANG JIN
 * * @version 1.0
 * * @description 报纸数据视图
 */
public class NewspaperFragment extends BaseFragment implements NewspaperView {
    private NewspaperPresenter mNewspaperPresenter;
    //刷新组件
    private SmartRefreshLayout mSmartRefreshLayout;
    //列表
    private RecyclerView mNewspaperRecycler;
    //适配器
    private PaperCoverAdapter mPaperCoverAdapter;

    /**
     * 采用静态方法封装相关必备参数，并提供该Fragment实例
     */
    public NewspaperFragment() {
    }

    /**
     * New instance newspaper fragment.
     *
     * @return the newspaper fragment
     */
    public static NewspaperFragment newInstance() {
        return new NewspaperFragment();
    }

    @Override
    protected int getFragmentLayoutID() {
        return R.layout.expressreader_fragment_newspaper;
    }

    @Override
    protected BasePresenter createFragmentPresenter() {
        return mNewspaperPresenter = new NewspaperPresenter(this);
    }

    @Override
    public void findView() {
        //初始化标题
        initTitleBar();
        //初始化刷新组件
        initSmartRefreshLayout();
        //初始化RecyclerView
        initRecyclerView();
    }

    @Override
    protected void lazyFetchData() {
        //请求报纸封面的数据
        initPaperList();
    }

    private void initTitleBar() {
        ImageView ivBack = mContentView.findViewById(R.id.iv_appbar_back);
        ivBack.setVisibility(View.GONE);
        TextView tvTitle = mContentView.findViewById(R.id.tv_appbar_title);
        tvTitle.setText(getResources().getString(R.string.expressreader_newspaper));
    }

    private void initSmartRefreshLayout() {
        mSmartRefreshLayout = mContentView.findViewById(R.id.srl_fragment_newspaper);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initPaperList();
            }
        });
    }

    private void initRecyclerView() {
        mNewspaperRecycler = mContentView.findViewById(R.id.rv_fragment_newspaper_list);
        mNewspaperRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mNewspaperRecycler.setHasFixedSize(true);
        mNewspaperRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 20;
            }
        });
        //数据
        List<PaperCover> paperCovers = new ArrayList<>();
        mPaperCoverAdapter = new PaperCoverAdapter(mContext, paperCovers);
        mPaperCoverAdapter.setOnPaperItemClickListener(new PaperCoverAdapter.OnPaperItemClickListener() {
            @Override
            public void onItemClick(PaperCover paperCover) {
                UIHelper.startPageListActivity(mContext, paperCover);
            }

            @Override
            public void refresh() {

            }

            @Override
            public void onBackTop() {
                mNewspaperRecycler.scrollToPosition(0);
            }
        });
        mNewspaperRecycler.setAdapter(mPaperCoverAdapter);
    }

    private void initPaperList() {
        mPaperCoverAdapter.resetLoadingType(LoadingType.LOADING_REFRESH);
        mNewspaperPresenter.getPaperCovers(LCID);
    }

    /**
     * Can scroll vertical boolean.
     *
     * @return the boolean
     */
    public boolean canScrollVertical() {
        if (mNewspaperRecycler != null) {
            mNewspaperRecycler.smoothScrollToPosition(0);
            return mNewspaperRecycler.canScrollVertically(-1);
        } else
            return false;
    }

    @Override
    public void getPaperCover(List<PaperCover> paperCovers) {
        finishLoading(true);
        if (mPaperCoverAdapter != null) {
            mPaperCoverAdapter.updateData(paperCovers);
        }
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showS(mContext, errorMsg);
        finishLoading(false);
        mPaperCoverAdapter.resetLoadingType(LoadingType.LOADING_ERROR);
    }

    /**
     * 停止加载
     *
     * @param success 是否加载成功
     *                true表示成功；false表示失败
     */
    private void finishLoading(boolean success) {
        mSmartRefreshLayout.finishRefresh(success);
    }

}
