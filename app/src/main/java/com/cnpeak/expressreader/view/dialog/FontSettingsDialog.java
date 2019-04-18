package com.cnpeak.expressreader.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.OnFontOptionListener;
import com.cnpeak.expressreader.utils.LogUtils;
import com.cnpeak.expressreader.utils.SpUtil;

/**
 * 字体选择对话框
 * 当前项目多出需要用到该对话框,考虑复用
 *
 * @author HUANGJIN on 2018/11/15
 * @version 1.0
 */
public class FontSettingsDialog extends AlertDialog implements View.OnClickListener,
        DialogInterface.OnShowListener, DialogInterface.OnDismissListener {
    //当前字体常量
    public static final int FONT_LARGEST = 160;
    public static final int FONT_LARGE = 140;
    public static final int FONT_NORMAL = 120;
    public static final int FONT_SMALL = 100;
    private Context mContext;
    //特大字号
    private TextView tvFontLargest;
    //大字号
    private TextView tvFontLarge;
    //中字号
    private TextView tvFontNormal;
    //小字号
    private TextView tvFontSmall;
    //取消
    private TextView tvFontCancel;
    //事件回调
    private OnFontOptionListener onFontOptionListener;

    public FontSettingsDialog(@NonNull Context context) {
        this(context, R.style.actionSheetDialogStyle);
    }

    private FontSettingsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        View mContentView = LayoutInflater.from(mContext).inflate(R.layout.expressreader_dialog_font_options, null);
        //初始化控件
        tvFontLargest = mContentView.findViewById(R.id.tv_font_option_largest);
        tvFontLarge = mContentView.findViewById(R.id.tv_font_option_large);
        tvFontNormal = mContentView.findViewById(R.id.tv_font_option_normal);
        tvFontSmall = mContentView.findViewById(R.id.tv_font_option_small);
        tvFontCancel = mContentView.findViewById(R.id.tv_font_option_cancel);
        tvFontLargest.setOnClickListener(this);
        tvFontLarge.setOnClickListener(this);
        tvFontNormal.setOnClickListener(this);
        tvFontSmall.setOnClickListener(this);
        tvFontCancel.setOnClickListener(this);
        setView(mContentView);
        //設置点击外部可以消失
        setCanceledOnTouchOutside(true);
        //设置从底部弹出,设置宽度适配屏幕宽度
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
        //设置当前显示的监听事件
        setOnShowListener(this);
        //设置之前的监听事件
        setOnDismissListener(this);
    }

    /**
     * 设置当前字体大小默认选择项的突出效果
     */
    private void setFontOptionChecked(Integer fontSize) {
        LogUtils.i("FontSetting current fontSize >>> " + fontSize);
        switch (fontSize) {
            case FONT_LARGEST:
                tvFontLargest.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.dp_8));
                tvFontLargest.setTextColor(Color.parseColor("#025EE5"));
                break;
            case FONT_LARGE:
                tvFontLarge.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.dp_8));
                tvFontLarge.setTextColor(Color.parseColor("#025EE5"));
                break;
            case FONT_NORMAL:
                tvFontNormal.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.dp_8));
                tvFontNormal.setTextColor(Color.parseColor("#025EE5"));
                break;
            case FONT_SMALL:
                tvFontSmall.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.dp_8));
                tvFontSmall.setTextColor(Color.parseColor("#025EE5"));
                break;
        }
    }

    /**
     * 设置默认的文字效果
     */
    private void setFontOptionUnChecked() {
        tvFontLargest.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.dp_6));
        tvFontLargest.setTextColor(Color.parseColor("#78BEEB"));
        tvFontLarge.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.dp_6));
        tvFontLarge.setTextColor(Color.parseColor("#78BEEB"));
        tvFontNormal.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.dp_6));
        tvFontNormal.setTextColor(Color.parseColor("#78BEEB"));
        tvFontSmall.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.dp_6));
        tvFontSmall.setTextColor(Color.parseColor("#78BEEB"));
    }

    /**
     * 保存設置并改变当前内容页面布局
     */
    @Override
    public void onClick(View v) {
        if (v == tvFontLargest) {
            SpUtil.setInt(mContext, ErConstant.FONT_OPTION, FONT_LARGEST);
            if (onFontOptionListener != null) {
                onFontOptionListener.onFontSelected(FONT_LARGEST);
            }
            dismiss();
        } else if (v == tvFontLarge) {
            SpUtil.setInt(mContext, ErConstant.FONT_OPTION, FONT_LARGE);
            if (onFontOptionListener != null) {
                onFontOptionListener.onFontSelected(FONT_LARGE);
            }
            dismiss();
        } else if (v == tvFontNormal) {
            SpUtil.setInt(mContext, ErConstant.FONT_OPTION, FONT_NORMAL);
            if (onFontOptionListener != null) {
                onFontOptionListener.onFontSelected(FONT_NORMAL);
            }
            dismiss();
        } else if (v == tvFontSmall) {
            SpUtil.setInt(mContext, ErConstant.FONT_OPTION, FONT_SMALL);
            if (onFontOptionListener != null) {
                onFontOptionListener.onFontSelected(FONT_SMALL);
            }
            dismiss();
        } else if (v == tvFontCancel) {
            dismiss();
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        //初始化默认值
        int fontOption = SpUtil.getInt(mContext, ErConstant.FONT_OPTION, FONT_NORMAL);
        setFontOptionChecked(fontOption);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //重置状态
        setFontOptionUnChecked();
    }

    /**
     * 设置事件回调
     *
     * @param listener 回调对象
     */
    public void setOnFontOptionListener(OnFontOptionListener listener) {
        this.onFontOptionListener = listener;
    }
}
