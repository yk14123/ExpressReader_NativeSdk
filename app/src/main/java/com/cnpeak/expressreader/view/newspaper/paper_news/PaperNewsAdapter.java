package com.cnpeak.expressreader.view.newspaper.paper_news;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.LoadingType;
import com.cnpeak.expressreader.interf.OnItemClickListener;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.utils.GlideCircleTransform;
import com.cnpeak.expressreader.utils.LogUtils;
import com.cnpeak.expressreader.utils.TimeUtils;
import com.cnpeak.expressreader.view.widget.ExpandableTextView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link HotSpot} and makes a call to the
 *
 * @author builder by HUANG JIN on 18/11/9
 * @version 1.0
 * @description 热点资讯数据适配器
 */
public class PaperNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = PaperNewsAdapter.class.getSimpleName();

    private Context mContext;

    private final List<HotSpot> mNewsList;
    //报社头像地址
    private String mNewsIconUrl;
    //报社名称
    private String mNewsName;
    //报社简介
    private String mDescription;
    //回調接口处理对象
    private OnItemClickListener<HotSpot> mOnItemClickListener;
    //当前的加载类型
    private int mLoadingType = LoadingType.LOADING_REFRESH;

    PaperNewsAdapter(Context context, List<HotSpot> items) {
        this.mContext = context;
        mNewsList = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ErConstant.ITEM_TYPE_HEADER;
        } else if (mNewsList == null || mNewsList.size() == 0) {
            return ErConstant.ITEM_TYPE_EMPTY;
        } else
            return ErConstant.ITEM_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        if (mNewsList == null || mNewsList.size() == 0) {
            return 2;
        } else
            return mNewsList.size() + 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ErConstant.ITEM_TYPE_HEADER:
                View headerView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.expressreader_recycler_item_newspaper_header, parent, false);
                return new HeaderViewHolder(headerView);
            case ErConstant.ITEM_TYPE_EMPTY:
                View emptyView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.expressreader_include_recycler_empty, parent, false);
                return new EmptyViewHolder(emptyView);
            default:
                View dataView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.expressreader_recycler_item_news_content, parent, false);
                return new DataViewHolder(dataView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case ErConstant.ITEM_TYPE_DATA:
                DataViewHolder dataHolder = (DataViewHolder) viewHolder;
                final HotSpot hotSpot = mNewsList.get(position - 1);
                if (hotSpot != null) {
                    //描述
                    String sourceTitle = hotSpot.getSourceTitle();
                    if (!TextUtils.isEmpty(sourceTitle)) {
                        dataHolder.mTvNewsDescription.setText(sourceTitle);
                    }
                    //报社名称
                    String newsName = hotSpot.getNewsName();
                    if (!TextUtils.isEmpty(newsName)) {
                        dataHolder.mTvNewsName.setText(newsName);
                    }
                    //是否显示置顶标签
                    if (hotSpot.isCanTop()) {
                        dataHolder.mTvNewsTopTag.setVisibility(View.VISIBLE);
                        dataHolder.mTvNewsPublishTime.setVisibility(View.GONE);
                    } else {
                        //发布时间 ---> 转化成相对时间(相对于当前时间的差值)
                        dataHolder.mTvNewsTopTag.setVisibility(View.GONE);
                        dataHolder.mTvNewsPublishTime.setVisibility(View.VISIBLE);
                        String sourcePubDate = hotSpot.getSourcePubDate();
                        if (!TextUtils.isEmpty(sourcePubDate)) {
                            if (sourcePubDate.contains("T")) {
                                sourcePubDate = sourcePubDate.replace("T", " ");
                            }
                            dataHolder.mTvNewsPublishTime.setText(TimeUtils.fromToday(mContext, sourcePubDate));
                        }
                    }
                    //报社图片
                    if (TextUtils.isEmpty(hotSpot.getNewsIconUrl())) {
                        hotSpot.setNewsIconUrl(mNewsIconUrl);
                    }
                    Glide.with(mContext)
                            .load(hotSpot.getNewsIconUrl())
                            .apply(new RequestOptions()
                                    .error(R.drawable.expressreader_news_default_head)
                                    .placeholder(R.drawable.expressreader_news_default_head)
                                    .centerInside()
                                    .circleCrop()
                                    .transform(new GlideCircleTransform(mContext)))
                            .into(dataHolder.mIvNewsIcon);

                    //新闻图片-->默认去除第一张图片进行显示
                    List<String> imgUrls = hotSpot.getImgUrls();
                    if (imgUrls == null || imgUrls.size() == 0) {
                        //如果图片地址为空，则将图片显示控件隐藏
                        dataHolder.mIvNewsCover.setVisibility(View.GONE);
                    } else {
                        String imgUrl = imgUrls.get(0);
                        LogUtils.i(TAG, "imgUrl>>>>" + imgUrl);
                        dataHolder.mIvNewsCover.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(imgUrl)
                                .apply(new RequestOptions()
                                        .error(R.drawable.expressreader_default_cover)
                                        .placeholder(R.drawable.expressreader_default_cover))
                                .into(dataHolder.mIvNewsCover);

                    }
                    //设置Item的点击事件
                    dataHolder.mLLNewsRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClick(hotSpot);
                            }
                        }
                    });
                }
                break;
            case ErConstant.ITEM_TYPE_EMPTY:
                EmptyViewHolder emptyHolder = (EmptyViewHolder) viewHolder;
                if (mLoadingType == LoadingType.LOADING_ERROR) {
                    emptyHolder.mTvEmptyData.setVisibility(View.VISIBLE);
                    emptyHolder.mTvEmptyData.setText(mContext.getResources()
                            .getString(R.string.expressreader_data_refresh));
                } else {
                    emptyHolder.mTvEmptyData.setVisibility(View.GONE);
                }
                break;
            case ErConstant.ITEM_TYPE_HEADER:
                HeaderViewHolder headerHolder = (HeaderViewHolder) viewHolder;
                if (!TextUtils.isEmpty(mNewsName)) {
                    headerHolder.mTvNewsName.setText(mNewsName);
                }
                if (!TextUtils.isEmpty(mDescription)) {
                    headerHolder.mExpandDescription.setText(mDescription);
                }
                break;
        }
    }

    /**
     * 初始化Header
     */
    void initHeader(String newsIconUrl, String newsName, String newsDescription) {
        this.mNewsIconUrl = newsIconUrl;
        this.mNewsName = newsName;
        this.mDescription = newsDescription;
    }

    /**
     * 数据刷新操作方法
     *
     * @param mValues    数据源
     * @param isLoadMore 是否是加载更多操作
     */
    public void updateData(List<HotSpot> mValues, boolean isLoadMore) {
        if (mValues != null && mValues.size() != 0) {
            if (isLoadMore) {
                this.mNewsList.addAll(mValues);
            } else {
                //数据刷新操作
                mNewsList.clear();
                mNewsList.addAll(mValues);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * Reset loading type.
     *
     * @param loadingType the loading type
     */
    void resetLoadingType(int loadingType) {
        this.mLoadingType = loadingType;
        notifyDataSetChanged();
    }

    void setOnItemClickListener(OnItemClickListener<HotSpot> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLLNewsRoot;
        private ImageView mIvNewsIcon;
        private TextView mTvNewsName;
        private TextView mTvNewsPublishTime;
        private TextView mTvNewsTopTag;
        private TextView mTvNewsDescription;
        private ImageView mIvNewsCover;

        DataViewHolder(View view) {
            super(view);
            mLLNewsRoot = view.findViewById(R.id.ll_hotspot_item_root);
            mIvNewsIcon = view.findViewById(R.id.iv_hotspot_item_newsIcon);
            mTvNewsName = view.findViewById(R.id.tv_hotspot_item_newsName);
            mTvNewsPublishTime = view.findViewById(R.id.tv_hotspot_item_publishTime);
            mTvNewsTopTag = view.findViewById(R.id.tv_hotspot_item_tag);
            mTvNewsDescription = view.findViewById(R.id.tv_hotspot_item_description);
            mIvNewsCover = view.findViewById(R.id.iv_hotspot_item_newsCover);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvNewsName;
        private ExpandableTextView mExpandDescription;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            mExpandDescription = itemView.findViewById(R.id.etv_newspaper_description);
            mTvNewsName = itemView.findViewById(R.id.tv_newspaper_title);
        }
    }

    //无数据列表显示holder
    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvEmptyData;

        EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvEmptyData = itemView.findViewById(R.id.tv_recycler_empty_message);
        }
    }
}
