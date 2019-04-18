package com.cnpeak.expressreader.view.news.news_image;

import android.content.Intent;

import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseActivity;
import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.utils.LocaleHelper;
import com.cnpeak.expressreader.utils.LogUtils;
import com.cnpeak.expressreader.view.widget.ZoomViewPaper;

import java.util.ArrayList;

public class ImageDetailActivity extends BaseActivity<IView, ImageDetailPresenter> implements IView {
    private static final String TAG = ImageDetailActivity.class.getSimpleName();
    //当前索引显示
    private TextView mTvCurrentIndex;
    //图片数组信息
    private ArrayList<String> mImageUrls;
    //当前需要显示的图片位置
    private int mCurrentImageIndex;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.expressreader_activity_image_detail;
    }

    @Override
    protected ImageDetailPresenter initPresenter() {
        return new ImageDetailPresenter(this);
    }

    @Override
    protected void getExtraFromBundle(Intent intent) {
        super.getExtraFromBundle(intent);
        if (intent != null) {
            //获取图片数组信息
            mImageUrls = intent.getStringArrayListExtra(ErConstant.IMAGE_DETAIL_URLS);
            mCurrentImageIndex = intent.getIntExtra(ErConstant.IMAGE_CURRENT_INDEX, 0);
        }
    }

    @Override
    public void initView() {
        //初始化父容器点击事件
        initRoot();
        //初始化图片Paper
        initViewPaper();
        //初始化当前图片的索引值
        initImageIndex();

    }

    private void initRoot() {
        RelativeLayout mRlImageScaleRoot = findViewById(R.id.rl_image_detail_root);
        //设置root的点击事件
        mRlImageScaleRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initViewPaper() {
        ZoomViewPaper mImagePaper = findViewById(R.id.vp_image_detail_pager);
        //适配器
        ImageDetailAdapter mImageAdapter = new ImageDetailAdapter(mContext, mImageUrls);
        mImageAdapter.setOnImageClickListener(new ImageDetailAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(int position) {
                finish();
            }
        });
        mImagePaper.setAdapter(mImageAdapter);
        mImagePaper.setOffscreenPageLimit(3);
        //显示当前位置的图片
        mImagePaper.setCurrentItem(mCurrentImageIndex);
        mImagePaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                LogUtils.i(TAG, "onPageSelected i >>>" + i);
                mTvCurrentIndex.setText(String.format(LocaleHelper.getLocale(mContext),
                        "%d/%d", i + 1, mImageUrls.size()));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initImageIndex() {
        mTvCurrentIndex = findViewById(R.id.tv_image_detail_index);
        if (mCurrentImageIndex >= 0) {
            mTvCurrentIndex.setText(String.format(LocaleHelper.getLocale(mContext),
                    "%d/%d", mCurrentImageIndex + 1, mImageUrls.size()));
            mTvCurrentIndex.setVisibility(View.VISIBLE);
        } else {
            mTvCurrentIndex.setVisibility(View.GONE);
        }
    }

    /**
     * 复写finish方法方式实现退出时的动画
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.expressreader_scale_view_out);
    }
}
