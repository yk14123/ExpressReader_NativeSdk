package com.cnpeak.expressreader.view.mine.local;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.cnpeak.expressreader.utils.LogUtils;
import com.google.android.material.tabs.TabLayout;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseActivity;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.model.bean.IssueUnit;
import com.cnpeak.expressreader.model.db.DbType;
import com.cnpeak.expressreader.model.db.SQLConstant;
import com.cnpeak.expressreader.utils.StatusBarUtil;
import com.cnpeak.expressreader.utils.UIHelper;
import com.cnpeak.expressreader.view.dialog.ErLoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 阅读历史页面
 */
public class LocalHistoryActivity extends BaseActivity implements LocalHistoryView {
    private static final String TAG = "LocalHistoryActivity";
    private LocalHistoryPresenter mLocalHistoryPresenter;
    /**
     * 标题、操作按钮
     */
    private TextView mTvTitle;//页面标题
    private TextView mTvEditAction;//编辑按钮
    private FrameLayout mFlAction;//底部操作actions
    private TextView mTvDeleteAction;//删除选中项
    /**
     * 列表显示
     */
    private RecyclerView mHistoryRecycler;//列表显示控件
    private List<HotSpot> mLocalNewsList = new ArrayList<>();//本地新闻数据
    private NewsLocalAdapter mNewsLocalAdapter;//本地新闻适配器
    private List<IssueUnit> mLocalIssueList = new ArrayList<>();//本地杂志单元数据
    private IssueUnitLocalAdapter mIssueUnitLocalAdapter;//本地杂志单元适配器
    /**
     * 前数据库的类型see{@link DbType }
     */
    private int mDbType = SQLConstant.DB_TYPE_HISTORY;
    //当前数据库数据类型{0:报纸；1：杂志}
    private int mDataType = 0;

    //网络加载框
    private ErLoadingDialog mLoadingDialog;
    //一键清除确认框
    private AlertDialog mConfirmDialog;

    //回调事件
    private OnLocalItemClickListener onLocalItemClickListener = new OnLocalItemClickListener() {

        @Override
        public void onItemClick(Object bean) {
            if (bean != null) {
                if (bean instanceof HotSpot) {
                    HotSpot hotSpot = (HotSpot) bean;
                    UIHelper.startNewsDetailActivity(mContext, hotSpot);
                } else if (bean instanceof IssueUnit) {
                    IssueUnit issueUnit = (IssueUnit) bean;
                    UIHelper.startMagazineDetailActivity(mContext, issueUnit.getMnId());
                }
            }
        }

        @Override
        public void refresh() {

        }

        @Override
        public void onCheckable(boolean checkable) {
            setCheckable(checkable);
        }

        @Override
        public void onCheckedChanged(int count) {
            if (count > 0) {
                mTvDeleteAction.setText(String.format(mContext.getResources()
                        .getString(R.string.expressreader_delete_count), count));
                mTvDeleteAction.setTextColor(getResources().getColor(R.color.expressreader_channel_text_selected));
            } else {
                mTvDeleteAction.setText(mContext.getResources().getString(R.string.expressreader_delete));
                mTvDeleteAction.setTextColor(getResources().getColor(R.color.expressreader_summaryTextColor));
            }
        }
    };


    @Override
    protected int getActivityLayoutId() {
        return R.layout.expressreader_activity_local_history;
    }

    @Override
    protected BasePresenter initPresenter() {
        mLocalHistoryPresenter = new LocalHistoryPresenter(this);
        return mLocalHistoryPresenter;
    }

    @Override
    protected void getExtraFromBundle(Intent intent) {
        super.getExtraFromBundle(intent);
        mDbType = intent.getIntExtra(ErConstant.FAVOR_FLAG, 1);
    }

    @Override
    public void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.expressreader_white), 55);
        //初始化AppBar
        initAppbar();
        //初始化Tab标签
        initTabs();
        //初始化列表显示控件
        initRecyclerView();
        //初始化底部的action操作按钮
        initActions();
        //记载第一个tab
        switchTab(ErConstant.DATA_TYPE_HOTSPOT);
    }

    private void initAppbar() {
        CardView cvWrapper = findViewById(R.id.cv_appbar_wrapper);
        cvWrapper.setCardElevation(0);

        ImageView ivBack = findViewById(R.id.iv_appbar_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvTitle = findViewById(R.id.tv_appbar_title);
        String title = mDbType == 1
                ? getString(R.string.expressreader_mine_favorite_label)
                : getString(R.string.expressreader_mine_history);
        mTvTitle.setText(title);

        ImageView ivAction = findViewById(R.id.iv_appbar_action);
        ivAction.setVisibility(View.GONE);

        mTvEditAction = findViewById(R.id.tv_appbar_action);
        mTvEditAction.setText(R.string.expressreader_edit);
        mTvEditAction.setVisibility(View.VISIBLE);
        mTvEditAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView.Adapter adapter = mHistoryRecycler.getAdapter();
                if (adapter instanceof NewsLocalAdapter) {
                    mNewsLocalAdapter.setCheckable();
                } else if (adapter instanceof IssueUnitLocalAdapter) {
                    mIssueUnitLocalAdapter.setCheckable();
                }
            }
        });
    }

    private void initTabs() {
        TabLayout tabLayout = findViewById(R.id.tl_history_local);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.expressreader_newspaper)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.expressreader_magazine)));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initRecyclerView() {
        mHistoryRecycler = findViewById(R.id.rv_history_local);
        mHistoryRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mHistoryRecycler.setHasFixedSize(true);
        mHistoryRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //第一条位置插入上边距91
                if (parent.getChildAdapterPosition(view) == 0 && parent.getChildCount() != 0) {
                    outRect.top = 20;
                }
                outRect.bottom = 20;
            }
        });
    }

    private void initActions() {
        mFlAction = findViewById(R.id.fl_history_action);
        //删除所有
        TextView tvDeleteAllAction = findViewById(R.id.tv_history_delete_all);
        tvDeleteAllAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearAllDialog();
            }
        });

        //删除选中项
        mTvDeleteAction = findViewById(R.id.tv_history_delete_check);
        mTvDeleteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView.Adapter adapter = mHistoryRecycler.getAdapter();
                if (adapter instanceof NewsLocalAdapter) {
                    List<HotSpot> newsList = mNewsLocalAdapter.getCheckedList();
                    mLocalHistoryPresenter.deleteNewsListByType(mDbType, newsList);
                } else if (adapter instanceof IssueUnitLocalAdapter) {
                    List<IssueUnit> issueUnits = mIssueUnitLocalAdapter.getCheckedList();
                    mLocalHistoryPresenter.deleteIssueListByType(mDbType, issueUnits);
                }
            }
        });
    }

    /**
     * 显示确认框
     */
    private void showClearAllDialog() {
        if (mConfirmDialog == null) {
            mConfirmDialog = new AlertDialog
                    .Builder(this)
                    .setTitle("")
                    .setMessage(R.string.expressreader_clear_all_confirm)
                    .setPositiveButton(R.string.expressreader_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mLocalHistoryPresenter.deleteAllByType(mDataType, mDbType);
                        }
                    })
                    .setNegativeButton(R.string.expressreader_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (dialog != null)
                                dialog.dismiss();
                        }
                    }).create();

        }
        //点击外部区域可以消失
        mConfirmDialog.setCanceledOnTouchOutside(true);
        //当前弹窗可以消失
        mConfirmDialog.setCancelable(true);
        //消失
        if (!mConfirmDialog.isShowing()) {
            mConfirmDialog.show();
        }
    }

    private void switchTab(int dataType) {
        mDataType = dataType;
        switch (dataType) {
            case ErConstant.DATA_TYPE_HOTSPOT:
                if (mNewsLocalAdapter == null) {
                    mNewsLocalAdapter = new NewsLocalAdapter(mContext, mLocalNewsList);
                    mNewsLocalAdapter.setOnItemClickListener(onLocalItemClickListener);
                }
                mHistoryRecycler.setAdapter(mNewsLocalAdapter);
                break;
            case ErConstant.DATA_TYPE_ISSUE:
                if (mIssueUnitLocalAdapter == null) {
                    mIssueUnitLocalAdapter = new IssueUnitLocalAdapter(mContext, mLocalIssueList);
                    mIssueUnitLocalAdapter.setOnLocalItemClickListener(onLocalItemClickListener);
                }
                mHistoryRecycler.setAdapter(mIssueUnitLocalAdapter);
                break;
        }
        mTvEditAction.setEnabled(mLocalIssueList.size() != 0);
        mLocalHistoryPresenter.getLocalListByType(mDbType, mDataType);
        setCheckable(false);
    }

    /**
     * 设置当前界面的可编辑UI状态
     *
     * @param checkable true:当前界面可以编辑{适配器出现单选框，标题文字、右侧的action按钮文字}
     */
    private void setCheckable(boolean checkable) {
        mFlAction.setVisibility(checkable ? View.VISIBLE : View.GONE);
        mTvEditAction.setText(checkable ? R.string.expressreader_cancel : R.string.expressreader_edit);
        if (mDbType == 1) {
            mTvTitle.setText(checkable ? R.string.expressreader_favorite_edit
                    : R.string.expressreader_mine_favorite_label);
        } else {
            mTvTitle.setText(checkable ? R.string.expressreader_history_edit
                    : R.string.expressreader_mine_history);
        }
    }

    @Override
    public void getHotspotFromLocal(List<HotSpot> localList) {
        if (localList != null) {
            LogUtils.d(TAG, "getHotspotFromLocal: 新闻本地缓存记录数>>> " + localList.size());
            mLocalNewsList.addAll(localList);
            RecyclerView.Adapter adapter = mHistoryRecycler.getAdapter();
            if (adapter instanceof NewsLocalAdapter) {
                mNewsLocalAdapter.updateData(localList, false);
                mTvEditAction.setEnabled(localList.size() != 0);
            }
        } else {
            LogUtils.d(TAG, "getHotspotFromLocal: 新闻本地缓存记录为空>>> ");
        }
    }

    @Override
    public void getIssueListFromLocal(List<IssueUnit> localList) {
        if (localList != null) {
            LogUtils.d(TAG, "getIssueListFromLocal: 杂志本地缓存记录数>>> " + localList.size());
            mLocalIssueList.addAll(localList);
            RecyclerView.Adapter adapter = mHistoryRecycler.getAdapter();
            if (adapter instanceof IssueUnitLocalAdapter) {
                mIssueUnitLocalAdapter.updateData(localList, false);
                mTvEditAction.setEnabled(localList.size() != 0);
            }
        } else {
            LogUtils.d(TAG, "getIssueListFromLocal: 杂志本地缓存记录为空>>> ");
        }
    }

    @Override
    public void setState(boolean result) {
        LogUtils.d(TAG, "setState: result >>> " + result);
        if (result) {
            setCheckable(false);
            RecyclerView.Adapter adapter = mHistoryRecycler.getAdapter();
            if (adapter instanceof NewsLocalAdapter) {
                mLocalHistoryPresenter.getLocalListByType(mDbType, ErConstant.DATA_TYPE_HOTSPOT);
            } else if (adapter instanceof IssueUnitLocalAdapter) {
                mLocalHistoryPresenter.getLocalListByType(mDbType, ErConstant.DATA_TYPE_ISSUE);
            }
        }
    }

    @Override
    public void showLoading() {
        //在UI初始化完成之后开始请求网络数据
        if (mLoadingDialog == null) {
            mLoadingDialog = new ErLoadingDialog(mContext);
            mLoadingDialog.setMessage(mContext.getResources()
                    .getString(R.string.expressreader_data_loading));
        }
        if (!mLoadingDialog.isShowing() && !isFinishing()) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

}
