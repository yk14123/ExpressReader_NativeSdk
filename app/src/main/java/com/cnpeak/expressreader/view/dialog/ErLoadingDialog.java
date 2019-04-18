package com.cnpeak.expressreader.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.cnpeak.expressreader.R;

/**
 * 项目通用的对话框
 */
public class ErLoadingDialog extends Dialog {
    private TextView mTvLoadingMessage;

    public ErLoadingDialog(Context context) {
        super(context);
        //设置对话框背景透明
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setContentView(R.layout.expressreader_include_recycler_loading);
        mTvLoadingMessage = findViewById(R.id.tv_loading_message);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param message 给用户展示的提示信息
     */
    public void setMessage(String message) {
        mTvLoadingMessage.setText(message);
    }
}
