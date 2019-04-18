package com.cnpeak.expressreader.view.magazine.magazine_units;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.model.bean.IssueLite;
import com.cnpeak.expressreader.view.widget.TagText;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IssueLite}
 *
 * @author builder by HUANG JIN on 18/11/9
 * @version 1.0
 * @description 单本杂志数据适配器单元布局
 */
public class MagazineUnitTypeAdapter extends RecyclerView.Adapter {
    //UType常量
    private static final int NORMAL = 1;//小图
    private static final int COVER = 2;//封面故事
    private static final int HOT = 3;//热门话题
    private Context mContext;
    //数据源
    private final List<IssueLite.UnitsBean> mIssueBeans;
    //回调
    private OnIssueItemClickListener mOnIssueItemClickListener;

    MagazineUnitTypeAdapter(Context context, List<IssueLite.UnitsBean> items) {
        this.mContext = context;
        mIssueBeans = items;
    }

    @Override
    public int getItemCount() {
        if (mIssueBeans == null || mIssueBeans.size() == 0) {
            return 0;
        } else
            return mIssueBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        IssueLite.UnitsBean issueLite = mIssueBeans.get(position);
        return issueLite.getUType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case COVER:
                View tagView = layoutInflater.inflate(
                        R.layout.expressreader_recycler_item_magazine_type_cover, parent, false);
                return new IssueTagHolder(tagView);
            default:
                View normalView = layoutInflater.inflate(
                        R.layout.expressreader_recycler_item_magazine_type_normal, parent, false);
                return new IssueNormalHolder(normalView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case NORMAL:
                IssueNormalHolder normalHolder = (IssueNormalHolder) viewHolder;
                final IssueLite.UnitsBean normalIssue = mIssueBeans.get(position);
                //标题
                String title = normalIssue.getTitle();
                if (normalIssue.getUType() == HOT) {
                    //显示热门标签
                    String hotLabel = mContext.getResources().getString(R.string.expressreader_magazine_label_hot);
                    normalHolder.mTagText.setLeftTag(hotLabel, title);
                } else {
                    normalHolder.mTagText.setLeftTag("", title);
                }
                //小图
                List<String> thumbImage = normalIssue.getThumbImage();
                if (thumbImage != null) {
                    Glide.with(mContext)
                            .load(thumbImage.get(0))
                            .apply(new RequestOptions()
                                    .error(R.drawable.expressreader_default_cover)
                                    .placeholder(R.drawable.expressreader_default_cover))
                            .into(normalHolder.mIvIssueNormalCover);
                }
                //设置Item监听
                normalHolder.mFlIssueNormalRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnIssueItemClickListener != null) {
                            mOnIssueItemClickListener.onNormalType(normalIssue.getMnId());
                        }
                    }
                });
                break;
            case COVER:
                IssueTagHolder storyHolder = (IssueTagHolder) viewHolder;
                final IssueLite.UnitsBean tagIssue = mIssueBeans.get(position);
                //标题
                String storyTitle = tagIssue.getTitle();
                if (!TextUtils.isEmpty(storyTitle)) {
                    String coverLabel = mContext.getString(R.string.expressreader_magazine_label_cover);
                    storyHolder.mTagText.setLeftTag(coverLabel, storyTitle);
                }
                //小图
                List<String> storyImage = tagIssue.getThumbImage();
                Glide.with(mContext)
                        .load(storyImage.get(0))
                        .apply(new RequestOptions()
                                .error(R.drawable.expressreader_default_cover)
                                .placeholder(R.drawable.expressreader_default_cover))
                        .into(storyHolder.mIvIssueTopicCover);
                //设置Item监听
                storyHolder.mLlIssueTopicRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnIssueItemClickListener != null) {
                            mOnIssueItemClickListener.onNormalType(tagIssue.getMnId());
                        }
                    }
                });
                break;
        }
    }


    /**
     * 设置回调处理对象
     */
    void setOnIssueItemClickListener(OnIssueItemClickListener mOnIssueItemClickListener) {
        this.mOnIssueItemClickListener = mOnIssueItemClickListener;
    }

    /**
     * 点击事件回调接口--->用于处理RecyclerView内部item事件
     */
    public interface OnIssueItemClickListener {
        /**
         * 点击issueUnit单元
         */
        void onNormalType(String MnId);

        /**
         * 点击issueCover封面
         */
        void onCoverType(String coverUrl);
    }


    //小图holder
    public class IssueNormalHolder extends RecyclerView.ViewHolder {
        private LinearLayout mFlIssueNormalRoot;
        private TagText mTagText;
        private ImageView mIvIssueNormalCover;

        IssueNormalHolder(@NonNull View itemView) {
            super(itemView);
            mFlIssueNormalRoot = itemView.findViewById(R.id.ll_recycler_empty_root);
            mTagText = itemView.findViewById(R.id.tv_magazine_history_item_title);
            mIvIssueNormalCover = itemView.findViewById(R.id.iv_issue_local_item_cover);
        }
    }

    //封面故事、热门话题holder
    public class IssueTagHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlIssueTopicRoot;
        private TagText mTagText;
        private ImageView mIvIssueTopicCover;

        IssueTagHolder(@NonNull View itemView) {
            super(itemView);
            mLlIssueTopicRoot = itemView.findViewById(R.id.ll_issue_lite_topic_root);
            mTagText = itemView.findViewById(R.id.tv_magazine_history_item_title);
            mIvIssueTopicCover = itemView.findViewById(R.id.iv_magazine_cover_unit);
        }
    }

}
