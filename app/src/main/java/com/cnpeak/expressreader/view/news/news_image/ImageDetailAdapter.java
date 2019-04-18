package com.cnpeak.expressreader.view.news.news_image;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cnpeak.expressreader.R;
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * 热点资讯详情内大图适配器
 *
 * @author HUANGJIN
 * @version 1.0
 */
public class ImageDetailAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mImgUrl;
    //回调
    private OnImageClickListener onImageClickListener;

    public ImageDetailAdapter(Context context, List<String> mImgUrl) {
        this.mContext = context;
        this.mImgUrl = mImgUrl;
    }

    /**
     * 返回viewpager要显示几页
     */
    @Override
    public int getCount() {
        return mImgUrl == null ? 0 : mImgUrl.size();
    }

    /**
     * 返回要显示的view,即要显示的视图
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View mContentView = LayoutInflater.from(mContext).inflate(
                R.layout.expressreader_pager_item_detail_image, container, false);

        //图片
        PhotoView photoView = mContentView.findViewById(R.id.iv_hotspot_detail_image);
        Glide.with(mContentView)
                .load(mImgUrl.get(position))
                .apply(new RequestOptions()
                        .error(R.drawable.expressreader_default_cover)
                        .placeholder(R.drawable.expressreader_default_cover))
                .into(photoView);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (onImageClickListener != null) {
                    onImageClickListener.onImageClick(position);
                }
            }
        });

        photoView.setOnOutsidePhotoTapListener(new OnOutsidePhotoTapListener() {
            @Override
            public void onOutsidePhotoTap(ImageView imageView) {
                if (onImageClickListener != null) {
                    onImageClickListener.onImageClick(position);
                }
            }
        });
        container.addView(mContentView);//将布局添加到container中
        return mContentView;
    }

    /**
     * 销毁条目
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    /**
     * 该函数用来判断instantiateItem(ViewGroup, int)
     * 函数所返回来的Key与一个页面视图是否是代表的同一个视图(即它俩是否是对应的，对应的表示同一个View)
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    //Item点击事件
    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }
}
