package com.cnpeak.expressreader.view.news;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cnpeak.expressreader.model.bean.Category;

import java.util.List;

/**
 * 热点资讯分类标签切换，详情页面适配器
 * ViewPaper+TabLayout联动
 */
public class NewsCategoryPaperAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private List<Category> mCategories;

    /**
     * Instantiates a new News category paper adapter.
     *
     * @param manager    the manager
     * @param categories the categories
     * @param fragments  the fragments
     */
    NewsCategoryPaperAdapter(FragmentManager manager, List<Category> categories, List<Fragment> fragments) {
        super(manager);
        this.mCategories = categories;
        this.mFragments = fragments;
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mCategories != null && mCategories.size() != 0) {
            Category category = mCategories.get(position);
            if (category != null) {
                return category.getCategoryText();
            }
        }
        return super.getPageTitle(position);
    }

    /**
     * Update data.
     *
     * @param categories the categories
     * @param fragments  the fragments
     */
    void updateData(List<Category> categories, List<Fragment> fragments) {
        this.mCategories = categories;
        this.mFragments = fragments;
        notifyDataSetChanged();
    }
}
