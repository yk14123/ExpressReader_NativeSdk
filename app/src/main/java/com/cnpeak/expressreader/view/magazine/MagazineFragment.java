package com.cnpeak.expressreader.view.magazine;

import android.graphics.Color;
import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseFragment;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.interf.LoadingType;
import com.cnpeak.expressreader.model.bean.MagazineList;
import com.cnpeak.expressreader.utils.ToastUtils;
import com.cnpeak.expressreader.utils.UIHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Magazine fragment.
 *
 * @author HUANG JIN
 * @version 1.0
 * 杂志封面Grid网格视图
 */
public class MagazineFragment extends BaseFragment implements MagazineView {
    /**
     * The constant TAG.
     */
    public static final String TAG = MagazineFragment.class.getSimpleName();
    /**
     * 加载视图
     */
    private LinearLayout mLoadingLayout;//loading
    private LinearLayout mLlProgressBar;
    private TextView mTvLoadingLogo;//图片
    //杂志数据控制器
    private MagazinePresenter mMagazinePresenter;
    //加载视图
    private SmartRefreshLayout mSmartRefreshLayout;
    //数据展示容器
    private RecyclerView mMagazineRecycler;
    //适配器
    private MagazineListAdapter mMagazineListAdapter;

    /**
     * 采用静态方法封装相关必备参数，并提供该Fragment实例
     */
    public MagazineFragment() {
    }

    /**
     * New instance magazine fragment.
     *
     * @return the magazine fragment
     */
    public static MagazineFragment newInstance() {
        return new MagazineFragment();
    }

    @Override
    protected int getFragmentLayoutID() {
        return R.layout.expressreader_fragment_magazine;
    }

    @Override
    protected BasePresenter createFragmentPresenter() {
        return mMagazinePresenter = new MagazinePresenter(this);
    }

    @Override
    public void findView() {
        //初始化加载视图
        initSmartRefreshLayout();
        initLoadingLayout();
        //初始化RecyclerView
        initRecyclerView();
    }

    @Override
    protected void lazyFetchData() {
        //加载杂志数据
        initMagazineList();
    }

    private void initSmartRefreshLayout() {
        mSmartRefreshLayout = mContentView.findViewById(R.id.srv_magazine_list);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initMagazineList();
            }
        });
    }

    private void initLoadingLayout() {
        mLoadingLayout = mContentView.findViewById(R.id.layout_loading_magazine_list);
        mLlProgressBar = mContentView.findViewById(R.id.ll_loading_view_root);
        mTvLoadingLogo = mContentView.findViewById(R.id.tv_recycler_empty_message);
        mTvLoadingLogo.setTextColor(Color.parseColor("#FFFFFF"));
        mLoadingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(LoadingType.LOADING_REFRESH);
                initMagazineList();
            }
        });
    }

    private void initRecyclerView() {
        mMagazineRecycler = mContentView.findViewById(R.id.rv_magazine_list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mMagazineRecycler.setLayoutManager(gridLayoutManager);
        //设置GirdLayoutManager平分当前屏幕
        mMagazineRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                //下边距
                outRect.top = 30;
                //右边距
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                if (childAdapterPosition % 2 == 0) {
                    outRect.right = 30;
                }
            }
        });
        List<MagazineList> mMagazineLists = new ArrayList<>();
        mMagazineListAdapter = new MagazineListAdapter(mContext, mMagazineLists);
        mMagazineListAdapter.setOnMagazineItemClick(new MagazineListAdapter.OnMagazineListItemListener() {

            @Override
            public void onBackTop() {
                mMagazineRecycler.scrollToPosition(0);
            }

            @Override
            public void onItemClick(MagazineList magazineList) {
                if (magazineList != null) {
                    //跳转到issueList界面
                    UIHelper.startMagazineUnitActivity(mContext, magazineList);
                }
            }

            @Override
            public void refresh() {

            }
        });

        mMagazineRecycler.setAdapter(mMagazineListAdapter);
    }

    private void initMagazineList() {
        showLoading(LoadingType.LOADING_REFRESH);
//        mMagazineListAdapter.resetLoadingType(LoadingType.LOADING_REFRESH);
        //获取杂志数据
        mMagazinePresenter.getMagazineList(LCID);
    }

    /**
     * Can scroll vertical boolean.
     *
     * @return the boolean
     */
    public boolean canScrollVertical() {
        if (mMagazineRecycler != null) {
            mMagazineRecycler.smoothScrollToPosition(0);
            return mMagazineRecycler.canScrollVertically(-1);
        }
        return false;
    }

    @Override
    public void getMagazineList(List<MagazineList> magazineList) {
        finishLoading(true);
        if (magazineList != null && magazineList.size() != 0) {
            showLoading(LoadingType.LOADING_CONTENT);
            mMagazineListAdapter.updateData(magazineList);
        } else {
            showLoading(LoadingType.LOADING_ERROR);
        }
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showS(mContext, errorMsg);
        finishLoading(false);
        showLoading(LoadingType.LOADING_ERROR);
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

    private void showLoading(int loadingType) {
        switch (loadingType) {
            case LoadingType.LOADING_ERROR:
                mSmartRefreshLayout.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.VISIBLE);
                mLlProgressBar.setVisibility(View.GONE);
                mTvLoadingLogo.setVisibility(View.VISIBLE);
                break;
            case LoadingType.LOADING_REFRESH:
                mSmartRefreshLayout.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.VISIBLE);
                mLlProgressBar.setVisibility(View.VISIBLE);
                mTvLoadingLogo.setVisibility(View.GONE);
                break;
            default:
                mSmartRefreshLayout.setVisibility(View.VISIBLE);
                mLoadingLayout.setVisibility(View.GONE);
                break;
        }
    }

}
