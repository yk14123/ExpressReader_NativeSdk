package com.cnpeak.expressreader.view.news.news_category;

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
import com.cnpeak.expressreader.utils.TimeUtils;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link HotSpot}
 *
 * @author builder by HUANG JIN on 18/11/9
 * @version 1.0  新闻资讯适配器
 */
public class NewsCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private final List<HotSpot> mNewsList;
    //回調接口处理对象
    private OnItemClickListener<HotSpot> mOnItemClickListener;
    //当前的加载类型
    private int mLoadingType = LoadingType.LOADING_REFRESH;

    /**
     * Instantiates a new News category adapter.
     *
     * @param context the context
     * @param items   the items
     */
    NewsCategoryAdapter(Context context, List<HotSpot> items) {
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
                        .inflate(R.layout.expressreader_include_loading, parent, false);
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
                            holder.mTvNewsPublishTime.setText(TimeUtils.fromToday(mContext, sourcePubDate));
                        }
                    }

                    //报社图片
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
                    emptyHolder.llLoadingRoot.setVisibility(View.GONE);
                    emptyHolder.tvErrorMsg.setVisibility(View.VISIBLE);
                    emptyHolder.tvErrorMsg.setText(R.string.expressreader_data_refresh);
//                    emptyHolder.llIncludeLoadingRoot.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (mOnItemClickListener != null) {
//                                mOnItemClickListener.refresh();
//                            }
//                        }
//                    });
                } else {
                    emptyHolder.llLoadingRoot.setVisibility(View.VISIBLE);
                    emptyHolder.tvErrorMsg.setVisibility(View.GONE);
                }
                break;
        }

    }

    /**
     * 数据刷新操作方法
     *
     * @param mValues    数据源
     * @param isLoadMore 是否是加载更多操作
     */
    public void updateData(List<HotSpot> mValues, boolean isLoadMore) {
        if (mValues != null) {
            if (!isLoadMore) {
                //数据刷新操作
                if (mNewsList != null) {
                    mNewsList.clear();
                    mNewsList.addAll(mValues);
                }
            } else {
                this.mNewsList.addAll(mValues);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 提供Adapter item的点击回调事件处理
     *
     * @param onItemClickListener the on item click listener
     */
    void setOnItemClickListener(OnItemClickListener<HotSpot> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
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

    /**
     * The type Data view holder.
     */
    static class DataViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLLNewsRoot;
        private ImageView mIvNewsIcon;
        private TextView mTvNewsName;
        private TextView mTvNewsPublishTime;
        private TextView mTvNewsTopTag;
        private TextView mTvNewsDescription;
        private ImageView mIvNewsCover;

        /**
         * Instantiates a new Data view holder.
         *
         * @param view the view
         */
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

    /**
     * The type Empty view holder.
     */
    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llIncludeLoadingRoot;
        private LinearLayout llLoadingRoot;
        private TextView tvErrorMsg;

        /**
         * Instantiates a new Empty view holder.
         *
         * @param itemView the item view
         */
        EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            llIncludeLoadingRoot = itemView.findViewById(R.id.ll_include_loading_root);
            llLoadingRoot = itemView.findViewById(R.id.ll_loading_view_root);
            tvErrorMsg = itemView.findViewById(R.id.tv_recycler_empty_message);
        }
    }

}
