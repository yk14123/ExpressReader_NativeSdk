package com.cnpeak.expressreader.view.mine.locale;

import android.content.Intent;
import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseActivity;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.interf.OnItemClickListener;
import com.cnpeak.expressreader.model.bean.LCID;
import com.cnpeak.expressreader.utils.LocaleHelper;
import com.cnpeak.expressreader.view.home.ErHomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 语言环境设置界面
 */
public class LocaleSettingActivity extends BaseActivity implements LocaleView {
    private static final String TAG = "LocaleSettingActivity";
    //控制器
    private LocalePresenter mLocalePresenter;
    //适配器
    private LocaleAdapter mLocaleAdapter;
    //当前选择
    private LCID mLCIDBean;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.expressreader_activity_locale_setting;
    }

    @Override
    protected BasePresenter initPresenter() {
        return mLocalePresenter = new LocalePresenter(this);
    }

    @Override
    public void initView() {
        //初始化标题栏
        initAppBar();
        //初始化语言选项列表
        initRecyclerView();
        //加载数据
        initLCID();
    }

    private void initAppBar() {
        //取消
        TextView ivCancel = findViewById(R.id.tv_locale_cancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //确认
        TextView tvConfirm = findViewById(R.id.tv_locale_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocalePresenter.clearCache();
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView localeRecycler = findViewById(R.id.rv_locale_group);
        localeRecycler.setLayoutManager(new LinearLayoutManager(this));
        localeRecycler.setHasFixedSize(true);
        localeRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 1;

            }
        });
        List<LCID> mLcidList = new ArrayList<>();
        mLocaleAdapter = new LocaleAdapter(this, mLcidList);
        mLocaleAdapter.setLocaleChangedListener(new OnItemClickListener<LCID>() {
            @Override
            public void onItemClick(LCID lcid) {
                Log.d(TAG, "onItemClick: lcid >>> " + lcid.getDisplayName());
                mLCIDBean = lcid;
            }

            @Override
            public void refresh() {

            }
        });
        localeRecycler.setAdapter(mLocaleAdapter);
    }

    private void initLCID() {
        mLocalePresenter.getLCID();
    }

    @Override
    public void getLCID(List<LCID> lcidList) {
        mLocaleAdapter.updateLocaleList(lcidList);
    }

    @Override
    public void onLocaleConfigChanged(boolean result) {
        if (mLCIDBean != null && result) {
            Log.d(TAG, "onLocaleConfigChanged ...");
            //当前选择非覆盖当前默认的语言选项
            LocaleHelper.setLCID(mContext, mLCIDBean.getLCID());
//            LocaleHelper.applyLocaleChanged(this);
            LocaleHelper.applyLocaleChanged(getApplicationContext());
            //然后重新启动Activity,加载新配置的语言资源
//            UIHelper.startSplashActivity(this);
            Intent intent = new Intent(this, ErHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
