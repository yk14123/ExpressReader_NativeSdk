package com.cnpeak.expressreader.view.news;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseFragment;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.model.bean.Category;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.utils.LogUtils;
import com.cnpeak.expressreader.view.dialog.NewsTabSortWindow;
import com.cnpeak.expressreader.view.news.news_category.NewsCategoryFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * The type News fragment.
 *
 * @author builder by HUANG JIN on 18/11/9
 * @version 1.0
 * 热点资讯Fragment视图
 */
public class NewsFragment extends BaseFragment implements NewsView {
    private static final String TAG = NewsFragment.class.getSimpleName();
    //控制器
    private NewsPresenter mNewsPresenter;
    /**
     * 新闻分类
     */
    private LinearLayout mLlTabWrapper;//标签容器
    private TabLayout mNewsTabLayout;//Tab选择控件
    private List<Category> mCategories;//分类列表数据
    private NewsTabSortWindow mNewsTabSortWindow; //标签编辑对话框
    /**
     * 滑动容器
     */
    private ViewPager mNewsViewPager;//数据填充容器
    private List<Fragment> mFragments;//Fragment数组
    private NewsCategoryPaperAdapter mNewsPagerAdapter;//数据适配器
    /**
     * 跑马灯新闻
     */
    private ViewPager mBannerPager;
    private List<HotSpot> mBannerNewsList;//头条热点新闻数组
    private StickTopAdapter mBannerAdapter;
    private Disposable mBannerDisposable;

    /**
     * 采用静态方法封装相关必备参数，并提供该Fragment实例
     */
    public NewsFragment() {
    }

    /**
     * New instance news fragment.
     *
     * @return the news fragment
     */
    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    protected int getFragmentLayoutID() {
        return R.layout.expressreader_fragment_news;
    }

    @Override
    protected BasePresenter createFragmentPresenter() {
        return mNewsPresenter = new NewsPresenter(this);
    }

    @Override
    public void findView() {
        //初始化tab标签编辑按钮
        initCategoryAction();
        //tab标签部分
        initTabWithViewPaper();
        //初始化跑马灯新闻滑动控件
        initBannerPaper();
    }

    private void initTabWithViewPaper() {
        mLlTabWrapper = mContentView.findViewById(R.id.ll_hotspot_tab_wrapper);
        mNewsTabLayout = mContentView.findViewById(R.id.tl_fragment_hotspot);
        mNewsViewPager = mContentView.findViewById(R.id.vp_fragment_hotspot_news);
        mCategories = new ArrayList<>();
        mFragments = new ArrayList<>();
        mNewsPagerAdapter = new NewsCategoryPaperAdapter(
                getChildFragmentManager(), mCategories, mFragments);
        mNewsViewPager.setAdapter(mNewsPagerAdapter);
        mNewsTabLayout.setupWithViewPager(mNewsViewPager);

    }

    private void initBannerPaper() {
        mBannerPager = mContentView.findViewById(R.id.vp_fragment_hotspot_banner);
        mBannerNewsList = new ArrayList<>();
        mBannerAdapter = new StickTopAdapter(mContext, mBannerNewsList);
        mBannerPager.setAdapter(mBannerAdapter);

    }

    private void initCategoryAction() {
        ImageView mIvCategory = mContentView.findViewById(R.id.iv_hotspot_menu);
        mIvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCategories != null) {
                    showNewsSortWindow();
                }
            }
        });
    }

    @Override
    protected void lazyFetchData() {
        //请求分类标签数据
        mNewsPresenter.getCategory(LCID);
        //请求banner热点资讯
        mNewsPresenter.getStickTop(LCID);
    }

    /**
     * 显示渠道编辑Window
     */
    private void showNewsSortWindow() {
        LogUtils.d(TAG, "showNewsSortWindow: >>>");
        //标签编辑
        if (mNewsTabSortWindow == null) {
            //初始化
            mNewsTabSortWindow = new NewsTabSortWindow(mContext);
            mNewsTabSortWindow.setCategories(mCategories, mNewsTabLayout.getSelectedTabPosition());
            mNewsTabSortWindow.setOnNewsSelectedListener(new NewsTabSortWindow.OnNewsSelectedListener() {
                @Override
                public void onNewsSelected(int position) {
                    LogUtils.d(TAG, "onNewsSelected: position >>> " + position);
                    mNewsTabSortWindow.dismiss();
                    mNewsViewPager.setCurrentItem(position, true);
                }

                @Override
                public void onNewsCategoryChanged(List<Category> categories) {
                    if (categories != null && categories.size() != 0) {
                        mCategories = categories;
                        resetTabNewsList();
                    }
                }
            });
        } else {
            mNewsTabSortWindow.setSelectedIndex(mNewsTabLayout.getSelectedTabPosition());
        }
        mNewsTabSortWindow.showAsDropDown(mLlTabWrapper, 0, 0, Gravity.TOP);
    }

    @Override
    public void getCategory(List<Category> categories) {
        if (categories != null && categories.size() != 0) {
            this.mCategories = categories;
            resetTabNewsList();
        }
    }

    private void resetTabNewsList() {
        mNewsTabLayout.removeAllTabs();
        mFragments.clear();
        for (int i = 0; i < mCategories.size(); i++) {
            Category category = mCategories.get(i);
            if (category != null) {
                mFragments.add(NewsCategoryFragment.newInstance(category.getChannelID(),
                        "", category.getCategoryText()));
            }
        }
        mNewsPagerAdapter.updateData(mCategories, mFragments);
        //设置当前默认的缓存页面
        mNewsViewPager.setOffscreenPageLimit(mCategories.size());
        //初始化首选项
        mNewsViewPager.setCurrentItem(0);
    }

    /**
     * 初始化Banner控件
     *
     * @param banners banner列表数据
     */
    @Override
    public void getStickTop(List<HotSpot> banners) {
        if (banners != null && banners.size() != 0) {
            this.mBannerNewsList = banners;
            mBannerAdapter.updateData(mBannerNewsList);
            //初始化定时器，并开始滑动
            setInterval();
        }
    }

    private void setInterval() {
        mBannerDisposable = Observable.interval(2, 5, TimeUnit.SECONDS)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) {
                        int index = aLong.intValue();
                        return index % mBannerNewsList.size();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        mBannerPager.setCurrentItem(integer, true);
                    }
                });
    }

    /**
     * 当前界面是否可以置顶
     *
     * @return the boolean
     */
    public boolean canScrollVertical() {
        try {
            int position = mNewsViewPager.getCurrentItem();
            if (mFragments != null && mFragments.size() != 0) {
                NewsCategoryFragment categoryFragment =
                        (NewsCategoryFragment) mFragments.get(position);
                return categoryFragment.canScrollTop();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public void onDetach() {
        //当前fragment onDetach之后停止发送消息
        if (mBannerDisposable != null) {
            LogUtils.d(TAG, "onDetach: cancel>>>");
            mBannerDisposable.dispose();
        }
        super.onDetach();
    }

}
