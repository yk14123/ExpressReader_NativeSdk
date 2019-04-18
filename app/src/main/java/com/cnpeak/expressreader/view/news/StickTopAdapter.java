package com.cnpeak.expressreader.view.news;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.utils.UIHelper;

import java.util.List;

/**
 * 热点资讯页面顶部文字跑马灯数据适配器 see bean{@link HotSpot}
 */
public class StickTopAdapter extends PagerAdapter {
    private Context context;
    private List<HotSpot> banners;

    /**
     * Instantiates a new Stick top adapter.
     *
     * @param context the context
     * @param banners the banners
     */
    StickTopAdapter(Context context, List<HotSpot> banners) {
        this.context = context;
        this.banners = banners;
    }

    @Override
    public int getCount() {
//        return banners.size() == 0 ? 0 : Integer.MAX_VALUE;
        return banners == null ? 0 : banners.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //计算当前真实的位置索引
//        position = position % banners.size();
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.expressreader_pager_item_news_banner, container, false);
        TextView tvBannerText = contentView.findViewById(R.id.tv_hotspot_banner_text);
        final HotSpot hotSpot = banners.get(position);
        if (hotSpot != null) {
            String sourceTitle = hotSpot.getSourceTitle();
            if (!TextUtils.isEmpty(sourceTitle))
                tvBannerText.setText(sourceTitle);
        }
        //设置点击事件
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.startNewsDetailActivity(context, hotSpot);
            }
        });
        container.addView(contentView);
        return contentView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    /**
     * 更新数据
     *
     * @param banners the banners
     */
    public void updateData(List<HotSpot> banners) {
        this.banners = banners;
        notifyDataSetChanged();
    }

}
