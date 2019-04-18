package com.cnpeak.expressreader.view.mine;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseFragment;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.OnFontOptionListener;
import com.cnpeak.expressreader.model.db.SQLConstant;
import com.cnpeak.expressreader.utils.LogUtils;
import com.cnpeak.expressreader.utils.ManifestUtils;
import com.cnpeak.expressreader.utils.SpUtil;
import com.cnpeak.expressreader.utils.UIHelper;
import com.cnpeak.expressreader.view.dialog.FontSettingsDialog;

/**
 * Use the {@link MineFragment#newInstance} factory method to
 * builder an instance of this fragment.
 * <p>
 * * @author HUANG JIN
 * * @version 1.0
 * * @description 我的视图
 */
public class MineFragment extends BaseFragment implements IView {
    private TextView mTvFontSize;
    //字体选择框
    private FontSettingsDialog mFontDialog;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getFragmentLayoutID() {
        return R.layout.expressreader_fragment_mine;
    }

    @Override
    protected BasePresenter createFragmentPresenter() {
        return new BasePresenter();
    }

    @Override
    protected void lazyFetchData() {
        int fontOption = SpUtil.getInt(mContext, ErConstant.FONT_OPTION, FontSettingsDialog.FONT_NORMAL);
        setFontSelected(fontOption);
    }

    @Override
    public void findView() {
        //初始化标题栏
        initAppbar();
        //初始化收藏选项
        initFavorLayout();
        //初始化阅读记录选项
        initRecordLayout();
        //初始化字体选项
        initFontLayout();
        //初始化语言设置选项
        initLocaleLayout();
        //初始化当前版本
        initVersionLayout();
    }

    private void initAppbar() {
        ImageView ivBack = mContentView.findViewById(R.id.iv_appbar_back);
        ivBack.setVisibility(View.GONE);
        TextView tvTitle = mContentView.findViewById(R.id.tv_appbar_title);
        tvTitle.setText(getResources().getString(R.string.expressreader_mine));
    }

    private void initFavorLayout() {
        LinearLayout mFlFavorite = mContentView.findViewById(R.id.fl_mine_favorite);
        mFlFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.startHistoryActivity(mContext, SQLConstant.DB_TYPE_FAVORITE);
            }
        });
    }

    private void initRecordLayout() {
        LinearLayout mFlRecord = mContentView.findViewById(R.id.fl_mine_record);
        mFlRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.startHistoryActivity(mContext, SQLConstant.DB_TYPE_HISTORY);
            }
        });
    }

    private void initVersionLayout() {
        TextView mTvAppVersion = mContentView.findViewById(R.id.tv_mine_appVersion);
        String versionName = ManifestUtils.getVersionName(mContext);
        if (!TextUtils.isEmpty(versionName)) {
            mTvAppVersion.setText(versionName);
        }
    }

    private void initLocaleLayout() {
        LinearLayout mFlLocale = mContentView.findViewById(R.id.fl_mine_locale);
        mFlLocale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.startLocaleActivity(mContext);
            }
        });
    }

    private void initFontLayout() {
        LinearLayout mFlFontSize = mContentView.findViewById(R.id.fl_mine_font);
        mFlFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFontDialog == null) {
                    mFontDialog = new FontSettingsDialog(mContext);
                    mFontDialog.setOnFontOptionListener(new OnFontOptionListener() {
                        @Override
                        public void onFontSelected(int fontOption) {
                            setFontSelected(fontOption);
                        }
                    });
                }
                mFontDialog.show();
            }
        });
        mTvFontSize = mContentView.findViewById(R.id.tv_mine_fontSize);

    }


    /**
     * 显示当前的语言选择设定
     */
    private void setFontSelected(int fontOption) {
        LogUtils.i("setFontSelected fontOption >>> " + fontOption);
        switch (fontOption) {
            case FontSettingsDialog.FONT_LARGEST:
                mTvFontSize.setText(mContext.getString(R.string.expressreader_font_largest));
                break;
            case FontSettingsDialog.FONT_LARGE:
                mTvFontSize.setText(mContext.getString(R.string.expressreader_font_large));
                break;
            case FontSettingsDialog.FONT_NORMAL:
                mTvFontSize.setText(mContext.getString(R.string.expressreader_font_normal));
                break;
            case FontSettingsDialog.FONT_SMALL:
                mTvFontSize.setText(mContext.getString(R.string.expressreader_font_small));
                break;
        }
    }

}
