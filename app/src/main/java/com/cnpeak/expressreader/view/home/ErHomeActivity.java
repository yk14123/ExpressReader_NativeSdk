package com.cnpeak.expressreader.view.home;

import com.cnpeak.expressreader.utils.ScreenUtils;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseActivity;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.utils.LogUtils;
import com.cnpeak.expressreader.view.magazine.MagazineFragment;
import com.cnpeak.expressreader.view.mine.MineFragment;
import com.cnpeak.expressreader.view.news.NewsFragment;
import com.cnpeak.expressreader.view.newspaper.NewspaperFragment;

/**
 * 项目首页
 *
 * @version 1.0
 * 首页视图(结构 : TAB + Fragment视图切换)
 */
public class ErHomeActivity extends BaseActivity implements IView {
    private static final String TAG = ErHomeActivity.class.getSimpleName();
    //Fragment管理器
    private FragmentManager mFragmentManager;
    //组件
    private Fragment mCurrentFragment;
    private NewsFragment mNewsFragment;
    private NewspaperFragment mNewspaperFragment;
    private MagazineFragment mMagazineFragment;
    private MineFragment mMineFragment;
    //底部Tab切换容器
    private RadioGroup mBottomRadioGroup;
    //StatusBar填充View
    private Toolbar mStatusToolbar;
    /**
     * The M main present.
     */
    public ErHomePresenter mMainPresent;
    //监听返回键点击时长
    private long mLastClickTimeMillis;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.expressreader_activity_home;
    }

    @Override
    protected BasePresenter initPresenter() {
        mFragmentManager = getSupportFragmentManager();
        return mMainPresent = new ErHomePresenter(this);
    }

    @Override
    public void initView() {
        initStatusBar();
        //初始化Fragment实例
        initFragments();
        //初始化底部导航栏
        initBottomNavigation();

    }

    private void initStatusBar() {
        mStatusToolbar = findViewById(R.id.tl_activity_main);
        int statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        ViewGroup.LayoutParams layoutParams = mStatusToolbar.getLayoutParams();
        layoutParams.height = statusBarHeight;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mStatusToolbar.setLayoutParams(layoutParams);
        mStatusToolbar.setTitle("");
        setSupportActionBar(mStatusToolbar);
    }

    /**
     * 动态改变当前状态栏的颜色
     *
     * @param colorRes 状态栏的颜色
     */
    protected void initStatusBarColor(int colorRes) {
        mStatusToolbar.setBackground(new ColorDrawable(getResources().getColor(colorRes)));
    }

    /**
     * 初始化Fragment实例
     */
    private void initFragments() {
        mFragmentManager = getSupportFragmentManager();
        //解决重叠问题,首先通过findFragmentByTag的方式查找是否有该Fragment实例存在
        Fragment newsFragment = mFragmentManager
                .findFragmentByTag(NewsFragment.class.getSimpleName());
        if (newsFragment instanceof NewsFragment) {
            mNewsFragment = (NewsFragment) newsFragment;
        } else {
            mNewsFragment = NewsFragment.newInstance();
        }

        Fragment magazineFragment = mFragmentManager
                .findFragmentByTag(MagazineFragment.class.getSimpleName());
        if (magazineFragment instanceof MagazineFragment) {
            mMagazineFragment = (MagazineFragment) magazineFragment;
        } else {
            mMagazineFragment = MagazineFragment.newInstance();
        }

        Fragment newspaperFragment = mFragmentManager
                .findFragmentByTag(NewspaperFragment.class.getSimpleName());
        if (newspaperFragment instanceof NewspaperFragment) {
            mNewspaperFragment = (NewspaperFragment) newspaperFragment;
        } else {
            mNewspaperFragment = NewspaperFragment.newInstance();
        }

        Fragment mineFragment = mFragmentManager
                .findFragmentByTag(MineFragment.class.getSimpleName());
        if (mineFragment instanceof MineFragment) {
            mMineFragment = (MineFragment) mineFragment;
        } else {
            mMineFragment = MineFragment.newInstance();
        }
        //初始化首选项
        switchFragment(mNewsFragment).commit();
    }

    private void initBottomNavigation() {
        mBottomRadioGroup = findViewById(R.id.rg_bottom_navigation);
        mBottomRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                LogUtils.i(TAG, "onCheckedChanged i >>> " + i);
                if (i == R.id.rb_navigation_hotspot) {//快讯
                    initStatusBarColor(R.color.expressreader_colorPrimaryDark);
                    switchFragment(mNewsFragment).commit();
                } else if (i == R.id.rb_navigation_newspaper) { //报纸
                    initStatusBarColor(R.color.expressreader_white);
                    switchFragment(mNewspaperFragment).commit();
                } else if (i == R.id.rb_navigation_magazine) {//杂志
                    initStatusBarColor(R.color.expressreader_magazineBackgroundColor);
                    switchFragment(mMagazineFragment).commit();
                } else if (i == R.id.rb_navigation_mine) { //我的
                    initStatusBarColor(R.color.expressreader_white);
                    switchFragment(mMineFragment).commit();
                }

            }
        });

    }

    /**
     * 切换fragment模块
     *
     * @param targetFragment 目标fragment
     */
    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (mCurrentFragment != null) {
                transaction.hide(mCurrentFragment);
            }
            transaction.add(R.id.fl_fragment_container, targetFragment,
                    targetFragment.getClass().getSimpleName());
        } else {
            transaction
                    .hide(mCurrentFragment)
                    .show(targetFragment);
        }
        mCurrentFragment = targetFragment;
        return transaction;
    }

    /**
     * 监听当前返回键的的点击事件
     * 判断当前界面是否处于顶部
     */
    @Override
    public void onBackPressed() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mLastClickTimeMillis <= 1500) {
            finish();
        } else {
            if (mCurrentFragment instanceof NewsFragment) {
                if (mNewsFragment != null && !mNewsFragment.canScrollVertical()) {
                    showExitInformation();
                }
            } else if (mCurrentFragment instanceof NewspaperFragment) {
                if (mNewspaperFragment != null && !mNewspaperFragment.canScrollVertical()) {
                    showExitInformation();
                }
            } else if (mCurrentFragment instanceof MagazineFragment) {
                if (mMagazineFragment != null && !mMagazineFragment.canScrollVertical()) {
                    showExitInformation();
                }
            } else {
                showExitInformation();
            }
        }
    }

    private void showExitInformation() {
        Snackbar.make(mBottomRadioGroup, R.string.expressreader_app_exit_tips, Snackbar.LENGTH_SHORT).show();
        mLastClickTimeMillis = System.currentTimeMillis();
    }

}
