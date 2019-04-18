package com.cnpeak.expressreader.view.magazine;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.interf.OnItemClickListener;
import com.cnpeak.expressreader.model.bean.IssueList;
import com.cnpeak.expressreader.model.bean.MagazineList;
import com.cnpeak.expressreader.model.dao.MagazineListDaoImpl;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MagazineList} and makes a call to the
 *
 * @author builder by HUANG JIN on 28/11/9
 * @version 1.0
 * 热点资讯数据适配器
 */
public class MagazineListAdapter extends RecyclerView.Adapter<MagazineListAdapter.MagazineViewHolder> {
    private static final String TAG = "MagazineListAdapter";
    private Context mContext;
    private OnMagazineListItemListener mOnItemClickListener;

    private List<MagazineList> mMagazineList;
    //选中项
    private SparseBooleanArray mCheckList = new SparseBooleanArray();

    MagazineListAdapter(Context context, List<MagazineList> magazineList) {
        this.mContext = context;
        mMagazineList = magazineList;
    }

    @Override
    public int getItemCount() {
        if (mMagazineList == null || mMagazineList.size() == 0) {
            return 0;
        } else
            return mMagazineList.size();
    }

    @NonNull
    @Override
    public MagazineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.expressreader_recycler_item_magazine_type, parent, false);
        return new MagazineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MagazineViewHolder holder, int position) {
        final MagazineList magazine = mMagazineList.get(position);
        List<IssueList> issueList = magazine.getIssueList();
        if (issueList != null && issueList.size() != 0) {
            IssueList listBean = issueList.get(0);
            if (listBean != null) {
                //发表日期
                String publishDate = listBean.getPublishDate();
                if (!TextUtils.isEmpty(publishDate)) {
                    holder.tvMagazinePublishDate.setText(publishDate);
                }
                //杂志封面
                String coverUrl = listBean.getCover();
                if (!TextUtils.isEmpty(coverUrl)) {
                    Glide.with(mContext)
                            .load(coverUrl)
                            .apply(new RequestOptions()
                                    .error(R.drawable.expressreader_default_cover)
                                    .placeholder(R.drawable.expressreader_default_cover))
                            .into(holder.ivMagazineViewCover);
                }
            }
        }
        //杂志名称
        String mgName = magazine.getMgName();
        if (!TextUtils.isEmpty(mgName)) {
            holder.tvMagazineName.setText(mgName);
        }
        //设置item的点击事件
        holder.rlMagazineViewRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(magazine);
                }
            }
        });
        //收藏事件
        holder.cbMagazineFavorite.setOnCheckedChangeListener(null);
        holder.cbMagazineFavorite.setChecked(mCheckList.get(position));
        holder.cbMagazineFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //更新当前收藏项的收藏时间
                magazine.setCheckedTimestamp(isChecked ? System.currentTimeMillis() : 0);
                //更新本地数据库的状态
                updateFavorState(magazine);
            }
        });
    }

    /**
     * 更新当前选中项的状态
     * 更新当前PaperCover的收藏时间字段，并根据此字段进行升序排序得到新的排列列表
     */
    private void updateFavorState(MagazineList magazineList) {
        MagazineListDaoImpl.updateFavorState(magazineList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<MagazineList>>() {
                    @Override
                    public void onNext(List<MagazineList> localPaperList) {
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
     * 数据刷新操作方法
     *
     * @param mValues 数据源
     */
    public void updateData(List<MagazineList> mValues) {
        Log.d(TAG, "updateData: >>>");
        if (mValues != null && mValues.size() != 0) {
            mMagazineList.clear();
            mMagazineList.addAll(mValues);
            mCheckList.clear();
            for (int i = 0; i < mMagazineList.size(); i++) {
                MagazineList magazineList = mMagazineList.get(i);
                mCheckList.put(i, magazineList.getCheckedTimestamp() > 0);
            }
            notifyDataSetChanged();

            if (mOnItemClickListener != null) {
                mOnItemClickListener.onBackTop();
            }
        }
    }

    //item点击事件回传接口
    public interface OnMagazineListItemListener extends OnItemClickListener<MagazineList> {
        void onBackTop();
    }

    /**
     * 设置Item的点击事件
     *
     * @param onMagazineItemClick item点击回调接口
     */
    void setOnMagazineItemClick(OnMagazineListItemListener onMagazineItemClick) {
        this.mOnItemClickListener = onMagazineItemClick;
    }

    static class MagazineViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlMagazineViewRoot;
        private ImageView ivMagazineViewCover;
        private TextView tvMagazineName;
        private TextView tvMagazinePublishDate;
        private CheckBox cbMagazineFavorite;

        MagazineViewHolder(View view) {
            super(view);
            rlMagazineViewRoot = view.findViewById(R.id.rl_magazine_item_root);
            ivMagazineViewCover = view.findViewById(R.id.iv_magazine_view_cover);
            tvMagazineName = view.findViewById(R.id.tv_magazine_view_name);
            tvMagazinePublishDate = view.findViewById(R.id.tv_magazine_view_date);
            cbMagazineFavorite = view.findViewById(R.id.cb_magazine_view_favorite);
        }
    }
}
