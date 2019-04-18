package com.cnpeak.expressreader.view.mine.locale;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.OnItemClickListener;
import com.cnpeak.expressreader.model.bean.LCID;
import com.cnpeak.expressreader.utils.SpUtil;
import java.util.List;

/**
 * 支持的语言列表适配器
 */
public class LocaleAdapter extends RecyclerView.Adapter<LocaleAdapter.LocaleViewHolder> {
    private Context mContext;
    private List<LCID> mLocaleList;
    //默认语系
    private String mDefaultLcid;
    //当前选中项索引
    private int mCurrentCheckedIndex;
    //回调
    private OnItemClickListener<LCID> localeChangedListener;

    LocaleAdapter(Context mContext, List<LCID> localeLis) {
        this.mContext = mContext;
        this.mLocaleList = localeLis;
        mDefaultLcid = SpUtil.getString(mContext, ErConstant.LOCALE_OPTION);
    }

    @NonNull
    @Override
    public LocaleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.expressreader_recycler_item_locale_list, viewGroup, false);
        return new LocaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LocaleViewHolder holder, int position) {
        final LCID lcid = mLocaleList.get(position);
        if (lcid != null) {
            holder.mTvLocaleName.setText(lcid.getDisplayName());
            String lcidCode = lcid.getLCID();
            if (!TextUtils.isEmpty(lcidCode) && TextUtils.equals(lcidCode, mDefaultLcid)) {
                mCurrentCheckedIndex = holder.getAdapterPosition();
                holder.mIvCheckState.setVisibility(View.VISIBLE);
            } else {
                holder.mIvCheckState.setVisibility(View.GONE);
            }

            holder.mFlLocaleItemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更换状态
                    int oldIndex = mCurrentCheckedIndex;
                    Log.d("LocaleAdapter", "onClick: oldIndex>> " + oldIndex);
                    String sequence = lcid.getSequence();
                    mCurrentCheckedIndex = Integer.parseInt(sequence) - 1;
                    mDefaultLcid = lcid.getLCID();
                    Log.d("LocaleAdapter", "onClick: mCurrentCheckedIndex>> " + mCurrentCheckedIndex);
                    notifyItemChanged(oldIndex);
                    //切换当前当前语言项
                    holder.mIvCheckState.setVisibility(View.VISIBLE);
                    if (localeChangedListener != null) {
                        localeChangedListener.onItemClick(lcid);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLocaleList.size();
    }

    void updateLocaleList(List<LCID> lcidList) {
        if (lcidList != null && lcidList.size() != 0) {
            this.mLocaleList.addAll(lcidList);
            notifyDataSetChanged();
        }
    }

    public void setLocaleChangedListener(OnItemClickListener<LCID> localeChangedListener) {
        this.localeChangedListener = localeChangedListener;
    }

    class LocaleViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout mFlLocaleItemRoot;
        private TextView mTvLocaleName;
        private ImageView mIvCheckState;

        LocaleViewHolder(@NonNull View itemView) {
            super(itemView);
            mFlLocaleItemRoot = itemView.findViewById(R.id.fl_locale_item_root);
            mTvLocaleName = itemView.findViewById(R.id.tv_locale_name);
            mIvCheckState = itemView.findViewById(R.id.iv_locale_check_state);
        }
    }
}
