package com.cnpeak.expressreader.view.magazine.magazine_review;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.model.bean.IssueList;
import com.cnpeak.expressreader.model.bean.MagazineList;
import com.cnpeak.expressreader.utils.LocaleHelper;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MagazineList} and makes a call to the
 *
 * @author builder by HUANG JIN on 18/11/13
 * @version 1.0
 * 往期杂志数据适配器
 */
public class MagazineReviewAdapter extends RecyclerView.Adapter<MagazineReviewAdapter.IssueLiseHolder> {
    private Context mContext;
    //IssueList数据源
    private final List<IssueList> mIssueListData;
    //回调对象
    private OnIssueItemSelectListener onIssueItemSelectListener;


    MagazineReviewAdapter(Context context, List<IssueList> issueListData) {
        this.mContext = context;
        mIssueListData = issueListData;
    }

    /**
     * 设置Item的监听事件
     */
    void setOnIssueItemSelectListener(OnIssueItemSelectListener listener) {
        this.onIssueItemSelectListener = listener;
    }

    @NonNull
    @Override
    public IssueLiseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expressreader_recycler_item_magazine_review, parent, false);
        return new IssueLiseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IssueLiseHolder holder, int position) {
        final IssueList issueListBean = mIssueListData.get(position);
        if (issueListBean != null) {
            //发表日期
            String publishDate = issueListBean.getPublishDate();
            if (!TextUtils.isEmpty(publishDate)) {
                holder.mTvMagazineIssuePublishDate.setText(publishDate);
            }
            //杂志封面
            String coverUrl = issueListBean.getCover();
            if (!TextUtils.isEmpty(coverUrl)) {
                Glide.with(mContext)
                        .load(coverUrl)
                        .apply(new RequestOptions()
                                .error(R.drawable.expressreader_default_cover)
                                .placeholder(R.drawable.expressreader_default_cover))
                        .into(holder.mIvMagazineIssueCover);
            }
            //杂志期数
            String issueNo = issueListBean.getIssueNo();
            String issueUnit = mContext.getResources().getString(R.string.expressreader_magazine_unit);
            if (!TextUtils.isEmpty(issueNo)) {
                String locale = LocaleHelper.getLCID(mContext);
                if (!TextUtils.isEmpty(locale) && locale.equals(LocaleHelper.LCID_ZH)) {
                    holder.mTvMagazineIssueNo.setText(String.format("%s%s", issueNo, issueUnit));
                } else {
                    holder.mTvMagazineIssueNo.setText(String.format("%s%s", issueUnit, issueNo));
                }
            }
            //设置item点击事件
            holder.mFlMagazineItemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onIssueItemSelectListener != null) {
                        onIssueItemSelectListener.onIssueItemSelected(issueListBean.getIssueId(), holder.getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnIssueItemSelectListener {
        void onIssueItemSelected(String issueId, int position);
    }

    @Override
    public int getItemCount() {
        return mIssueListData == null ? 0 : mIssueListData.size();
    }

    class IssueLiseHolder extends RecyclerView.ViewHolder {
        //root
        private FrameLayout mFlMagazineItemRoot;
        private ImageView mIvMagazineIssueCover;
        private TextView mTvMagazineIssueNo;
        private TextView mTvMagazineIssuePublishDate;

        IssueLiseHolder(View view) {
            super(view);
            mFlMagazineItemRoot = view.findViewById(R.id.fl_issue_item_root);
            mIvMagazineIssueCover = view.findViewById(R.id.iv_magazine_review_cover);
            mTvMagazineIssueNo = view.findViewById(R.id.tv_magazine_review_issueNo);
            mTvMagazineIssuePublishDate = view.findViewById(R.id.tv_magazine_review_publishDate);
        }
    }
}
