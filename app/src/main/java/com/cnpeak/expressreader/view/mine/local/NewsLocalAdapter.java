package com.cnpeak.expressreader.view.mine.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
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
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.utils.GlideCircleTransform;
import com.cnpeak.expressreader.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link HotSpot}
 *
 * @author builder by HUANG JIN on 18/11/9
 * @version 1.0
 * 1、新闻资讯数据条目通用的适配器（热点资讯模块列表item、阅读历史列表）
 * 2、阅读历史列表页面支持数据编辑操作，显示单选框UI;
 */
public class NewsLocalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "NewsCategoryAdapter";
    private Context mContext;
    private final List<HotSpot> mNewsList;
    //当前列表是否可支持编辑状态
    private boolean mCheckable = false;
    //当前加入删除列表的容器
    private SparseArray<HotSpot> mCheckedArray = new SparseArray<>();
    //回調接口处理对象
    private OnLocalItemClickListener mOnLocalItemClickListener;

    NewsLocalAdapter(Context context, List<HotSpot> items) {
        this.mContext = context;
        mNewsList = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (mNewsList == null || mNewsList.size() == 0) {
            return ErConstant.ITEM_TYPE_EMPTY;
        } else
            return ErConstant.ITEM_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        if (mNewsList == null || mNewsList.size() == 0) {
            return 1;
        } else {
            return mNewsList.size();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ErConstant.ITEM_TYPE_EMPTY:
                View emptyView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.expressreader_include_recycler_empty, parent, false);
                return new EmptyViewHolder(emptyView);
            default:
                View contentView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.expressreader_recycler_item_news_content, parent, false);
                return new DataViewHolder(contentView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case ErConstant.ITEM_TYPE_EMPTY:
                EmptyViewHolder emptyHolder = (EmptyViewHolder) viewHolder;
                emptyHolder.mLLEmptyLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                emptyHolder.mTvEmptyData.setText(mContext.getResources()
                        .getString(R.string.expressreader_favorite_data_empty));
                break;
            case ErConstant.ITEM_TYPE_DATA:
                final DataViewHolder holder = (DataViewHolder) viewHolder;
                final HotSpot hotSpot = mNewsList.get(position);
                if (hotSpot != null) {
                    //描述
                    String sourceTitle = hotSpot.getSourceTitle();
                    if (!TextUtils.isEmpty(sourceTitle)) {
                        holder.mTvNewsDescription.setText(sourceTitle);
                    }
                    //报社名称
                    String newsName = hotSpot.getNewsName();
                    if (!TextUtils.isEmpty(newsName)) {
                        holder.mTvNewsName.setText(newsName);
                    }
                    //是否显示置顶标签
                    if (hotSpot.isCanTop()) {
                        holder.mTvNewsTopTag.setVisibility(View.VISIBLE);
                        holder.mTvNewsPublishTime.setVisibility(View.GONE);

                    } else {
                        //发布时间 ---> 转化成相对时间(相对于当前时间的差值)
                        holder.mTvNewsTopTag.setVisibility(View.GONE);
                        holder.mTvNewsPublishTime.setVisibility(View.VISIBLE);
                        String sourcePubDate = hotSpot.getSourcePubDate();
                        if (!TextUtils.isEmpty(sourcePubDate)) {
                            holder.mTvNewsPublishTime.setText(sourcePubDate);
                        }
                    }

                    //报社图片
                    if (TextUtils.isEmpty(hotSpot.getNewsIconUrl())) {
                        hotSpot.setNewsIconUrl(hotSpot.getNewsIconUrl());
                    }
                    Glide.with(mContext)
                            .load(hotSpot.getNewsIconUrl())
                            .apply(new RequestOptions()
                                    .error(R.drawable.expressreader_news_default_head)
                                    .placeholder(R.drawable.expressreader_news_default_head)
                                    .centerInside()
                                    .circleCrop()
                                    .transform(new GlideCircleTransform(mContext)))
                            .into(holder.mIvNewsIcon);

                    //新闻图片-->默认去除第一张图片进行显示
                    List<String> imgUrls = hotSpot.getImgUrls();
                    if (imgUrls == null || imgUrls.size() == 0) {
                        //如果图片地址为空，则将图片显示控件隐藏
                        holder.mIvNewsCover.setVisibility(View.GONE);
                    } else {
                        String imgUrl = imgUrls.get(0);
                        holder.mIvNewsCover.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(imgUrl)
                                .apply(new RequestOptions()
                                        .error(R.drawable.expressreader_default_cover)
                                        .placeholder(R.drawable.expressreader_default_cover))
                                .into(holder.mIvNewsCover);

                    }
                    //设置Item的点击事件
                    holder.mLLNewsRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnLocalItemClickListener != null) {
                                mOnLocalItemClickListener.onItemClick(hotSpot);
                            }
                        }
                    });

                    //是否支持数据库存储编辑
                    holder.mCbCheckable.setVisibility(mCheckable ? View.VISIBLE : View.GONE);
                    //当前是否选中项
                    if (mCheckedArray.get(holder.getAdapterPosition()) != null) {
                        holder.mCbCheckable.setChecked(true);
                    } else holder.mCbCheckable.setChecked(false);

                    holder.mCbCheckable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            LogUtils.d(TAG, "onCheckedChanged: adapterPosition >>>  ¬ " + holder.getAdapterPosition());
                            if (isChecked) {
                                mCheckedArray.append(holder.getAdapterPosition(), hotSpot);
                            } else {
                                mCheckedArray.remove(holder.getAdapterPosition());
                            }
                            if (mOnLocalItemClickListener != null) {
                                mOnLocalItemClickListener.onCheckedChanged(mCheckedArray.size());
                            }
                        }
                    });
                }
                break;
        }

    }

    List<HotSpot> getCheckedList() {
        List<HotSpot> issueUnits = new ArrayList<>();
        if (mCheckedArray != null && mCheckedArray.size() != 0) {
            for (int i = 0; i < mCheckedArray.size(); i++) {
                int key = mCheckedArray.keyAt(i);
                HotSpot hotSpot = mCheckedArray.get(key);
                if (hotSpot != null) {
                    issueUnits.add(hotSpot);
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
    public void updateData(List<HotSpot> mValues, boolean isLoadMore) {
        if (mValues != null) {
            LogUtils.d(TAG, "updateData size >>> " + mValues.size());
            if (!isLoadMore) {
                //数据刷新操作
                if (mNewsList != null) {
                    mNewsList.clear();
                    mNewsList.addAll(mValues);
                }
                if (mCheckedArray != null) {
                    mCheckedArray.clear();
                }
                mCheckable = false;
            } else {
                this.mNewsList.addAll(mValues);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 是否可以编辑
     */
    void setCheckable() {
        if (mNewsList != null && mNewsList.size() != 0) {
            LogUtils.d(TAG, "setCheckable: size >>> " + mNewsList.size());
            this.mCheckable = !mCheckable;
            notifyDataSetChanged();
            if (mOnLocalItemClickListener != null) {
                mOnLocalItemClickListener.onCheckable(mCheckable);
            }
        } else LogUtils.d(TAG, "mNewsList size ==0  >>> ");
    }

    /**
     * 提供Adapter item的点击回调事件处理
     */
    void setOnItemClickListener(OnLocalItemClickListener onLocalItemClickListener) {
        this.mOnLocalItemClickListener = onLocalItemClickListener;
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLLNewsRoot;
        private CheckBox mCbCheckable;
        private ImageView mIvNewsIcon;
        private TextView mTvNewsName;
        private TextView mTvNewsPublishTime;
        private TextView mTvNewsTopTag;
        private TextView mTvNewsDescription;
        private ImageView mIvNewsCover;

        DataViewHolder(View view) {
            super(view);
            mLLNewsRoot = view.findViewById(R.id.ll_hotspot_item_root);
            mCbCheckable = view.findViewById(R.id.cb_hotspot_item_checkable);
            mIvNewsIcon = view.findViewById(R.id.iv_hotspot_item_newsIcon);
            mTvNewsName = view.findViewById(R.id.tv_hotspot_item_newsName);
            mTvNewsPublishTime = view.findViewById(R.id.tv_hotspot_item_publishTime);
            mTvNewsTopTag = view.findViewById(R.id.tv_hotspot_item_tag);
            mTvNewsDescription = view.findViewById(R.id.tv_hotspot_item_description);
            mIvNewsCover = view.findViewById(R.id.iv_hotspot_item_newsCover);
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLLEmptyLayout;
        private TextView mTvEmptyData;

        EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            mLLEmptyLayout = itemView.findViewById(R.id.ll_recycler_empty_root);
            mTvEmptyData = itemView.findViewById(R.id.tv_recycler_empty_message);
        }
    }

}
