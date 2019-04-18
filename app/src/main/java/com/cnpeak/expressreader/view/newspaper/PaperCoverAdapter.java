package com.cnpeak.expressreader.view.newspaper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.LoadingType;
import com.cnpeak.expressreader.interf.OnItemClickListener;
import com.cnpeak.expressreader.model.bean.PaperCover;
import com.cnpeak.expressreader.model.dao.PaperCoverDaoImpl;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;


/**
 * 报纸列表适配器
 * 为了方便数据的增删操作，将数据转换成LinkedList进行操作
 * 任务列表：
 * 1、收藏数据的本地化处理
 * 2、取消收藏之后，当前索引位置处理
 */
public class PaperCoverAdapter extends RecyclerView.Adapter {
    private static final String TAG = "PaperCoverAdapter";
    private Context mContext;
    private List<PaperCover> mPaperCovers;
    //选中项
    private SparseBooleanArray mCheckList = new SparseBooleanArray();
    //回调
    private OnPaperItemClickListener onPaperItemClickListener;
    //当前的加载类型
    private int mLoadingType = LoadingType.LOADING_REFRESH;

    /**
     * Instantiates a new Paper cover adapter.
     *
     * @param mContext     the m context
     * @param mPaperCovers the m paper covers
     */
    PaperCoverAdapter(Context mContext, List<PaperCover> mPaperCovers) {
        this.mContext = mContext;
        this.mPaperCovers = mPaperCovers;
    }

    @Override
    public int getItemViewType(int position) {
        if (mPaperCovers == null || mPaperCovers.size() == 0) {
            return ErConstant.ITEM_TYPE_EMPTY;
        } else
            return ErConstant.ITEM_TYPE_DATA;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ErConstant.ITEM_TYPE_EMPTY:
                View emptyView = LayoutInflater.from(mContext).inflate(
                        R.layout.expressreader_include_loading, viewGroup, false);
                return new EmptyViewHolder(emptyView);
            default:
                View content = LayoutInflater.from(mContext).inflate(
                        R.layout.expressreader_recycler_item_newspaper_list, viewGroup, false);
                return new PaperViewHolder(content);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case ErConstant.ITEM_TYPE_DATA:
                final PaperViewHolder holder = (PaperViewHolder) viewHolder;
                final PaperCover paperCover = mPaperCovers.get(position);
                if (paperCover != null) {
                    //最新发表事件
                    String sourcePubDate = paperCover.getSourcePubDate();
                    if (!TextUtils.isEmpty(sourcePubDate)) {
                        holder.mTvPublishTime.setText(sourcePubDate);
                    }
                    //封面
                    String logoImgUrl = paperCover.getPaperLogoImgUrl();
                    Glide.with(mContext)
                            .load(logoImgUrl)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.expressreader_default_cover)
                                    .error(R.drawable.expressreader_default_cover))
                            .into(holder.mIvNewspaperCover);
                    //描述
                    String description = paperCover.getPaperDescription();
                    if (!TextUtils.isEmpty(description)) {
                        holder.mTvNewspaperDescription.setText(description);
                    }
                    //设置root的点击事件
                    holder.mRlNewspaperRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onPaperItemClickListener != null) {
                                onPaperItemClickListener.onItemClick(paperCover);
                            }
                        }
                    });

                    //在初始化CheckBok状态和点选事件回调事件之前先把状态监听事件置空
                    holder.mCheckFavorite.setOnCheckedChangeListener(null);
                    //设置CheckBox的点选状态
                    holder.mCheckFavorite.setChecked(mCheckList.get(position));
                    //设置状态变化监听事件
                    holder.mCheckFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Log.d(TAG, "当前点击的位置 >>> " + holder.getAdapterPosition() + " isChecked >>> " + isChecked);
                            //更新当前收藏项的收藏时间
                            paperCover.setCheckedTimestamp(isChecked ? System.currentTimeMillis() : 0);
                            updateFavorState(paperCover);
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
                } else {
                    emptyHolder.llLoadingRoot.setVisibility(View.VISIBLE);
                    emptyHolder.tvErrorMsg.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mPaperCovers == null || mPaperCovers.size() == 0) {
            return 1;
        } else
            return mPaperCovers.size();
    }

    /**
     * 更新当前选中项的状态
     * 更新当前PaperCover的收藏时间字段，并根据此字段进行升序排序得到新的排列列表
     */
    private void updateFavorState(PaperCover paperCover) {
        PaperCoverDaoImpl.updateFavorState(paperCover)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<PaperCover>>() {
                    @Override
                    public void onNext(List<PaperCover> localPaperList) {
                        Log.d(TAG, "updateFavorState onNext: localPaperList.size>>> " + localPaperList.size());
                        if (localPaperList.size() != 0) {
                            updateData(localPaperList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "updateFavorState onError: >>> " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * Update the data.
     *
     * @param paperCovers the paper covers
     */
    public void updateData(List<PaperCover> paperCovers) {
        Log.d(TAG, "updateData: >>>");
        if (paperCovers != null) {
            mPaperCovers.clear();
            mPaperCovers.addAll(paperCovers);
            mCheckList.clear();
            for (int i = 0; i < paperCovers.size(); i++) {
                PaperCover paperCover = paperCovers.get(i);
                mCheckList.put(i, paperCover.getCheckedTimestamp() > 0);
            }
            notifyDataSetChanged();

            if (onPaperItemClickListener != null) {
                onPaperItemClickListener.onBackTop();
            }
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

    /**
     * 监听事件
     */
    public interface OnPaperItemClickListener extends OnItemClickListener<PaperCover> {
        /**
         * On back top.
         */
        void onBackTop();
    }

    /**
     * Sets on paper item click listener.
     *
     * @param onPaperItemClickListener the on paper item click listener
     */
    void setOnPaperItemClickListener(OnPaperItemClickListener onPaperItemClickListener) {
        this.onPaperItemClickListener = onPaperItemClickListener;
    }

    /**
     * The type Paper view holder.
     */
    static class PaperViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlNewspaperRoot;
        private ImageView mIvNewspaperCover;
        private TextView mTvPublishTime;
        private CheckBox mCheckFavorite;
        private TextView mTvNewspaperDescription;

        /**
         * Instantiates a new Paper view holder.
         *
         * @param itemView the item view
         */
        PaperViewHolder(@NonNull View itemView) {
            super(itemView);
            mRlNewspaperRoot = itemView.findViewById(R.id.rl_newspaper_item_root);
            mIvNewspaperCover = itemView.findViewById(R.id.iv_newspaper_item_cover);
            mTvPublishTime = itemView.findViewById(R.id.tv_newspaper_item_date);
            mCheckFavorite = itemView.findViewById(R.id.cb_newspaper_item_favorite);
            mTvNewspaperDescription = itemView.findViewById(R.id.tv_newspaper_item_description);
        }
    }

    /**
     * The type Empty view holder.
     */
    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llLoadingRoot;
        private TextView tvErrorMsg;

        /**
         * Instantiates a new Empty view holder.
         *
         * @param itemView the item view
         */
        EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            llLoadingRoot = itemView.findViewById(R.id.ll_loading_view_root);
            tvErrorMsg = itemView.findViewById(R.id.tv_recycler_empty_message);
        }
    }

}
