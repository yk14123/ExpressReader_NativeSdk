package com.cnpeak.expressreader.view.mine.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.model.bean.IssueUnit;
import com.cnpeak.expressreader.view.widget.TagText;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IssueUnit}
 *
 * @author builder by HUANG JIN onLocalItemClickListener 18/11/9
 * @version 1.0
 * @description 报纸本地数据适配器
 */
public class IssueUnitLocalAdapter extends RecyclerView.Adapter {
    private static final String TAG = "IssueUnitLocalAdapter";
    //UType常量
    private static final int EMPTY = 0;//无数据列表
    private static final int NORMAL = 1;//小图
    private static final int COVER = 2;//封面故事
    private Context mContext;
    //数据源
    private final List<IssueUnit> mLocalIssueList;
    //回调
    private OnLocalItemClickListener onLocalItemClickListener;
    //当前列表是否可支持编辑状态
    private boolean mCheckable = false;
    //当前加入删除列表的容器
    private SparseArray<IssueUnit> mCheckedArray = new SparseArray<>();

    IssueUnitLocalAdapter(Context context, List<IssueUnit> items) {
        this.mContext = context;
        mLocalIssueList = items;
    }

    @Override
    public int getItemCount() {
        if (mLocalIssueList == null || mLocalIssueList.size() == 0) {
            return 1;
        } else {
            return mLocalIssueList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mLocalIssueList == null || mLocalIssueList.size() == 0) {
            return EMPTY;
        } else {
            IssueUnit issueUnit = mLocalIssueList.get(position);
            return issueUnit.getUType();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case COVER:
                View coverView = layoutInflater.inflate(
                        R.layout.expressreader_recycler_item_issue_local_cover, parent, false);
                return new IssueCoverHolder(coverView);
            case NORMAL:
                View normalView = layoutInflater.inflate(
                        R.layout.expressreader_recycler_item_issue_local_normal, parent, false);
                return new IssueNormalHolder(normalView);

            default:
                View emptyView = layoutInflater.inflate(
                        R.layout.expressreader_include_recycler_empty, parent, false);
                return new IssueEmptyHolder(emptyView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case EMPTY:
                IssueEmptyHolder emptyHolder = (IssueEmptyHolder) viewHolder;
                emptyHolder.mLlEmptyRoot.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                emptyHolder.mTvEmptyData.setText(R.string.expressreader_favorite_data_empty);
                break;
            case NORMAL:
                final IssueUnit normalUnit = mLocalIssueList.get(position);
                final IssueNormalHolder normalHolder = (IssueNormalHolder) viewHolder;
                //杂志名称&标签
                String mgName = normalUnit.getMgName();
                String label = normalUnit.getIssueNo() + mContext.getResources()
                        .getString(R.string.expressreader_magazine_unit);
                normalHolder.tvIssueUnitName.setRightTag(label, mgName);
                //标题
                String title = normalUnit.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    normalHolder.tvIssueUnitTitle.setText(title);
                }
                //小图
                List<String> thumbImage = normalUnit.getImages();
                if (thumbImage != null) {
                    Glide.with(mContext)
                            .load(thumbImage.get(0))
                            .apply(new RequestOptions()
                                    .error(R.drawable.expressreader_default_cover)
                                    .placeholder(R.drawable.expressreader_default_cover))
                            .into(normalHolder.ivIssueUnitCover);
                }
                //单选框
                normalHolder.cbUnitFavorite.setVisibility(mCheckable ? View.VISIBLE : View.GONE);

                //当前是否选中项
                if (mCheckedArray.get(normalHolder.getAdapterPosition()) != null) {
                    normalHolder.cbUnitFavorite.setChecked(true);
                } else normalHolder.cbUnitFavorite.setChecked(false);

                normalHolder.cbUnitFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.d(TAG, "onCheckedChanged: adapterPosition >>>  ¬ " + normalHolder.getAdapterPosition());
                        if (isChecked) {
                            mCheckedArray.append(normalHolder.getAdapterPosition(), normalUnit);
                        } else {
                            mCheckedArray.remove(normalHolder.getAdapterPosition());
                        }
                        if (onLocalItemClickListener != null) {
                            onLocalItemClickListener.onCheckedChanged(mCheckedArray.size());
                        }
                    }
                });
                //设置Item监听
                normalHolder.llIssueUnitRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onLocalItemClickListener != null) {
                            onLocalItemClickListener.onItemClick(normalUnit);
                        }
                    }
                });
                break;
            case COVER:
                final IssueCoverHolder storyHolder = (IssueCoverHolder) viewHolder;
                final IssueUnit coverUnit = mLocalIssueList.get(position);
                //杂志名称&标签
                String coverMgName = coverUnit.getMgName();
                String issueNo = coverUnit.getIssueNo();
                if (!TextUtils.isEmpty(coverMgName)) {
                    String coverLabel = issueNo + mContext.getString(R.string.expressreader_magazine_unit);
                    storyHolder.tvIssueUnitName.setLeftTag(coverLabel, coverMgName);
                }
                //标题
                String coverTitle = coverUnit.getTitle();
                if (!TextUtils.isEmpty(coverTitle)) {
                    storyHolder.tvIssueUnitTitle.setText(coverTitle);
                }
                //小图
                List<String> storyImage = coverUnit.getImages();
                Glide.with(mContext)
                        .load(storyImage.get(0))
                        .apply(new RequestOptions()
                                .error(R.drawable.expressreader_default_cover)
                                .placeholder(R.drawable.expressreader_default_cover))
                        .into(storyHolder.ivIssueUnitCover);

                //单选框
                storyHolder.cbUnitFavorite.setVisibility(mCheckable ? View.VISIBLE : View.GONE);

                //当前是否选中项
                if (mCheckedArray.get(storyHolder.getAdapterPosition()) != null) {
                    storyHolder.cbUnitFavorite.setChecked(true);
                } else storyHolder.cbUnitFavorite.setChecked(false);

                storyHolder.cbUnitFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.d(TAG, "onCheckedChanged: adapterPosition >>>  ¬ " + storyHolder.getAdapterPosition());
                        if (isChecked) {
                            mCheckedArray.append(storyHolder.getAdapterPosition(), coverUnit);
                        } else {
                            mCheckedArray.remove(storyHolder.getAdapterPosition());
                        }
                        if (onLocalItemClickListener != null) {
                            onLocalItemClickListener.onCheckedChanged(mCheckedArray.size());
                        }
                    }
                });

                //设置Item监听
                storyHolder.llIssueUnitRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onLocalItemClickListener != null) {
                            onLocalItemClickListener.onItemClick(coverUnit);
                        }
                    }
                });
                break;
        }
    }

    void setCheckable() {
        if (mLocalIssueList != null && mLocalIssueList.size() != 0) {
            this.mCheckable = !mCheckable;
            notifyDataSetChanged();
            if (onLocalItemClickListener != null) {
                onLocalItemClickListener.onCheckable(mCheckable);
            }
        }
    }

    List<IssueUnit> getCheckedList() {
        List<IssueUnit> issueUnits = new ArrayList<>();
        if (mCheckedArray != null && mCheckedArray.size() != 0) {
            for (int i = 0; i < mCheckedArray.size(); i++) {
                int key = mCheckedArray.keyAt(i);
                IssueUnit issueUnit = mCheckedArray.get(key);
                if (issueUnit != null) {
                    issueUnits.add(issueUnit);
                }
            }
        }
        return issueUnits;
    }


    /**
     * 数据刷新操作方法
     *
     * @param mValues    数据源
     * @param isLoadMore 是否是加载更多操作
     */
    public void updateData(List<IssueUnit> mValues, boolean isLoadMore) {
        if (mValues != null) {
            if (!isLoadMore) {
                //数据刷新操作
                if (mLocalIssueList != null) {
                    mLocalIssueList.clear();
                    mLocalIssueList.addAll(mValues);
                }
            } else {
                this.mLocalIssueList.addAll(mValues);
            }
            if (mCheckedArray != null) {
                mCheckedArray.clear();
            }
            mCheckable = false;
            notifyDataSetChanged();
        }
    }

    /**
     * 设置回调处理对象
     */
    void setOnLocalItemClickListener(OnLocalItemClickListener mOnIssueUnitClickListener) {
        this.onLocalItemClickListener = mOnIssueUnitClickListener;
    }

    //小图holder
    public class IssueNormalHolder extends RecyclerView.ViewHolder {
        private LinearLayout llIssueUnitRoot;
        private CheckBox cbUnitFavorite;
        private TagText tvIssueUnitName;
        private TextView tvIssueUnitTitle;
        private ImageView ivIssueUnitCover;

        IssueNormalHolder(@NonNull View itemView) {
            super(itemView);
            llIssueUnitRoot = itemView.findViewById(R.id.ll_recycler_empty_root);
            cbUnitFavorite = itemView.findViewById(R.id.cb_magazine_history_checkable_cover_unit);
            tvIssueUnitName = itemView.findViewById(R.id.tv_magazine_history_item_title);
            tvIssueUnitTitle = itemView.findViewById(R.id.tv_magazine_history_item_description);
            ivIssueUnitCover = itemView.findViewById(R.id.iv_issue_local_item_cover);
        }
    }

    //封面故事、热门话题holder
    public class IssueCoverHolder extends RecyclerView.ViewHolder {
        private LinearLayout llIssueUnitRoot;
        private CheckBox cbUnitFavorite;
        private TagText tvIssueUnitName;
        private TextView tvIssueUnitTitle;
        private ImageView ivIssueUnitCover;

        IssueCoverHolder(@NonNull View itemView) {
            super(itemView);
            llIssueUnitRoot = itemView.findViewById(R.id.ll_magazine_history_item_cover_root);
            cbUnitFavorite = itemView.findViewById(R.id.cb_issue_local_item_checkable);
            tvIssueUnitName = itemView.findViewById(R.id.tv_issue_local_item_name);
            tvIssueUnitTitle = itemView.findViewById(R.id.tv_issue_local_item_title);
            ivIssueUnitCover = itemView.findViewById(R.id.iv_issue_local_item_cover);
        }
    }

    //无数据列表显示holder
    public class IssueEmptyHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlEmptyRoot;
        private TextView mTvEmptyData;

        IssueEmptyHolder(@NonNull View itemView) {
            super(itemView);
            mLlEmptyRoot = itemView.findViewById(R.id.ll_recycler_empty_root);
            mTvEmptyData = itemView.findViewById(R.id.tv_recycler_empty_message);
        }
    }

}
