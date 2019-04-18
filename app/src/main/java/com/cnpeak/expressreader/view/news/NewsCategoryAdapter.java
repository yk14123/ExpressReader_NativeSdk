package com.cnpeak.expressreader.view.news;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.model.bean.Category;
import com.cnpeak.expressreader.view.dialog.NewsTabSortWindow;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Category} and makes a call to the
 *
 * @author builder by HUANG JIN on 18/12/26
 * @version 1.0
 * 频道列表适配器
 */
public class NewsCategoryAdapter extends RecyclerView.Adapter<NewsCategoryAdapter.CategoryViewHolder> {
    //Channel数据源
    private List<Category> mCategories;
    //当前选中项
    private int mCurrentSelected;
    //回调
    private NewsTabSortWindow.OnNewsSelectedListener onNewsSelectedListener;

    public NewsCategoryAdapter(List<Category> categories, int selectedIndex) {
        mCategories = categories;
        this.mCurrentSelected = selectedIndex;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expressreader_recycler_item_news_category, parent, false);
        return new CategoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {
        final Category category = mCategories.get(position);
        if (category != null) {
            //发表日期
            String categoryText = category.getCategoryText();
            if (!TextUtils.isEmpty(categoryText)) {
                holder.tvChannelName.setText(categoryText);
                //当前选中项的状态
                holder.tvChannelName.setSelected(mCurrentSelected == position);
            }
            //点击渠道跳转至指定的tab
            holder.tvChannelName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNewsSelectedListener != null) {
                        onNewsSelectedListener.onNewsSelected(position);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mCategories == null ? 0 : mCategories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvChannelName;

        CategoryViewHolder(View view) {
            super(view);
            tvChannelName = view.findViewById(R.id.tv_channel_name);
        }
    }

    /**
     * 设置当前选中项状态，恢复为选中项的颜色
     */
    public void seCurrentSelected(int position) {
        this.mCurrentSelected = position;
        notifyDataSetChanged();
    }

    /**
     * 设置监听事件
     */
    public void setOnNewsSelectedListener(NewsTabSortWindow.OnNewsSelectedListener onNewsSelectedListener) {
        this.onNewsSelectedListener = onNewsSelectedListener;
    }


}
