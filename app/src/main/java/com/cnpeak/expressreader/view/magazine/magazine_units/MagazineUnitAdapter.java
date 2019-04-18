package com.cnpeak.expressreader.view.magazine.magazine_units;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.LoadingType;
import com.cnpeak.expressreader.model.bean.IssueLite;
import com.cnpeak.expressreader.utils.LocaleHelper;

import java.util.LinkedList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IssueLite} and makes a call to the
 *
 * @author builder by HUANG JIN on 18/11/9
 * @version 1.0
 * @description 单本杂志数据适配器单元布局
 */
public class MagazineUnitAdapter extends RecyclerView.Adapter {
    private Context mContext;
    //数据源
    private LinkedList<IssueLite> mIssueLites;
    //回调事件
    private MagazineUnitTypeAdapter.OnIssueItemClickListener onIssueItemClickListener;

    MagazineUnitAdapter(Context context, LinkedList<IssueLite> items) {
        this.mContext = context;
        mIssueLites = items;
    }

    @Override
    public int getItemViewType(int position) {
        return ErConstant.ITEM_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        if (mIssueLites != null && mIssueLites.size() != 0) {
            return mIssueLites.size();
        } else return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
                View itemView = LayoutInflater.from(mContext).inflate(
                        R.layout.expressreader_recycler_item_magazine_unit, null, false);
                return new IssueDataHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ErConstant.ITEM_TYPE_DATA:
                IssueDataHolder viewHolder = (IssueDataHolder) holder;
                IssueLite issueLite = mIssueLites.get(position);
                if (issueLite != null) {
                    //封面大圖
                    final String cover = issueLite.getCover();
                    Glide.with(mContext)
                            .load(cover)
                            .apply(new RequestOptions()
                                    .error(R.drawable.expressreader_default_cover)
                                    .placeholder(R.drawable.expressreader_default_cover))
                            .into(viewHolder.mIvIssueCover);
                    //当前期数
                    String issueNo = issueLite.getIssueNo();
                    String unit = mContext.getResources().getString(R.string.expressreader_magazine_unit);
                    if (!TextUtils.isEmpty(issueNo)) {
                        String locale = LocaleHelper.getLCID(mContext);
                        if (locale.equals(LocaleHelper.LCID_ZH)) {
                            viewHolder.mTvIssueNo.setText(String.format("%s%s", issueNo, unit));
                        } else {
                            viewHolder.mTvIssueNo.setText(String.format("%s%s", unit, issueNo));
                        }
                    }
                    //发表时期
                    String publishDate = issueLite.getPublishDate();
                    if (!TextUtils.isEmpty(publishDate)) {
                        viewHolder.mTvIssuePublishDate.setText(publishDate);
                    }
                    //issueListBean数据适配
                    LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    viewHolder.mRvIssueLiteList.setLayoutManager(manager);
                    //不让内部的RecyclerView获取焦点，避免出现数据初始化完成之后无法停留在顶部
                    viewHolder.mRvIssueLiteList.setFocusable(false);
                    //添加当前RecyclerView的itemDecoration
                    viewHolder.mRvIssueLiteList.addItemDecoration(new SimpleDividerDecoration(mContext));
                    //适配多样式布局
                    MagazineUnitTypeAdapter magazineUnitTypeAdapter = new MagazineUnitTypeAdapter(mContext, issueLite.getUnits());
                    magazineUnitTypeAdapter.setOnIssueItemClickListener(onIssueItemClickListener);
                    viewHolder.mRvIssueLiteList.setAdapter(magazineUnitTypeAdapter);
                    //设置点击事件
                    viewHolder.mLlIssueRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onIssueItemClickListener != null) {
                                onIssueItemClickListener.onCoverType(cover);
                            }
                        }
                    });
                }
                break;
        }

    }

    /**
     * 添加数据单元
     */
    void addIssueLite(IssueLite issueLite, int loadingType) {
        if (issueLite != null) {
            switch (loadingType) {
                case LoadingType.LOADING_REFRESH:
                    mIssueLites.clear();
                    //是否查看往期数据
                    mIssueLites.add(0, issueLite);
                    notifyDataSetChanged();
                    break;
                case LoadingType.LOADING_NEXT:
                    //加载更多
                    mIssueLites.add(getItemCount(), issueLite);
                    notifyItemInserted(getItemCount());
                    break;
                case LoadingType.LOADING_PRE:
                    //是否查看往期数据
                    mIssueLites.add(0, issueLite);
                    notifyItemInserted(0);
                    break;
            }
        }
    }

    void setOnIssueItemClickListener(MagazineUnitTypeAdapter.OnIssueItemClickListener onIssueItemClickListener) {
        this.onIssueItemClickListener = onIssueItemClickListener;
    }

    //无数据列表显示holder
    static class IssueDataHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mLlIssueRoot;
        private ImageView mIvIssueCover;
        private TextView mTvIssueNo;
        private TextView mTvIssuePublishDate;
        private RecyclerView mRvIssueLiteList;

        IssueDataHolder(@NonNull View itemView) {
            super(itemView);
            mLlIssueRoot = itemView.findViewById(R.id.rl_magazine_issue_root);
            mIvIssueCover = itemView.findViewById(R.id.iv_issue_lite_cover);
            mTvIssueNo = itemView.findViewById(R.id.tv_issue_lite_issueNo);
            mTvIssuePublishDate = itemView.findViewById(R.id.tv_issue_lite_publishDate);
            mRvIssueLiteList = itemView.findViewById(R.id.rv_issue_lite_list);
        }
    }

}
