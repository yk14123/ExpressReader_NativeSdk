package com.cnpeak.expressreader.view.news;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseFragment;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.model.bean.Category;
import com.cnpeak.expressreader.model.bean.HotSpot;
import com.cnpeak.expressreader.view.news.news_category.NewsCategoryFragment;

import java.util.ArrayList;
import java.util.List;

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
     * 搜索区域
     */
//    private SearchView mSearchView;
    /**
     * 新闻分类
     */
    private TabLayout mNewsTabLayout;//Tab选择控件
    private List<Category> mCategories;//分类列表数据
    /**
     * 滑动容器
     */
    private ViewPager mNewsViewPager;//数据填充容器
    private List<Fragment> mFragments;//Fragment数组
    private NewsCategoryPaperAdapter mNewsPagerAdapter;//数据适配器

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
        //初始化搜索区域
//        initSearchArea();
        //tab标签部分
        initTabWithViewPaper();
    }

//    private void initSearchArea() {
//        mSearchView = mContentView.findViewById(R.id.sv_search_bar);
//        mSearchView.setQuery("", false);
//        mSearchView.clearFocus();  //可以收起键盘
//        mSearchView.onActionViewCollapsed();
//    }

    private void initTabWithViewPaper() {
        mNewsTabLayout = mContentView.findViewById(R.id.tl_fragment_hotspot);
        mNewsViewPager = mContentView.findViewById(R.id.vp_fragment_hotspot_news);
        mCategories = new ArrayList<>();
        mFragments = new ArrayList<>();
        mNewsPagerAdapter = new NewsCategoryPaperAdapter(
                getChildFragmentManager(), mCategories, mFragments);
        mNewsViewPager.setAdapter(mNewsPagerAdapter);
        mNewsTabLayout.setupWithViewPager(mNewsViewPager);

    }


    @Override
    protected void lazyFetchData() {
        //请求分类标签数据
        mNewsPresenter.getCategory(LCID);
        //请求banner热点资讯
//        mNewsPresenter.getStickTop(LCID);
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
        super.onDetach();
    }

}
