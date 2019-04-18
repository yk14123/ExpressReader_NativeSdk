package com.cnpeak.expressreader.view.widget.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cnpeak.expressreader.R;

import androidx.annotation.NonNull;

public class ActionWebView extends WebView {
    private static final String TAG = "ActionWebView";

    private Context context;

    //当前自定义的WebView的ActionMode对象
    private ActionMode mActionMode;

    //原生回调对象
    private JsToAndroid mJsToAndroid;

    //回调对象
    private ActionWebCallback mActionCallback;

    //是否显示自定义弹出菜单
    private boolean isCustomMenu = false;

    public ActionWebView(Context context) {
        super(context);
        this.context = context;
        initSettings();
    }

    public ActionWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initSettings();
    }

    public ActionWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initSettings();
    }

    public void isCustomMenu(boolean isCustomMenu) {
        this.isCustomMenu = isCustomMenu;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initSettings() {
        //页面标题
        setVerticalScrollBarEnabled(false);//不能垂直滑动
        setHorizontalScrollBarEnabled(false);//不能水平滑动
        WebSettings settings = getSettings();
        //settings.setUseWideViewPort(true);//调整到适合webView的大小，不过尽量不要用，有些手机有问题
        settings.setLoadWithOverviewMode(true);//设置WebView是否使用预览模式加载界面。
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);//设置js可用
        setWebViewClient(new ActionWebClient());
        setWebChromeClient(new ErWebChromeClient());
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        if (isCustomMenu) {
            return resolveActionMode(super.startActionMode(callback));
        } else {
            return super.startActionMode(callback);
        }
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        if (isCustomMenu) {
            return resolveActionMode(super.startActionMode(callback, type));
        } else {
            return super.startActionMode(callback, type);
        }
    }

    private ActionMode resolveActionMode(ActionMode actionMode) {
        if (actionMode == null) {
            return null;
        } else {
            //清除默认的系统的弹出式菜单
            Menu menu = actionMode.getMenu();
            mActionMode = actionMode;
            menu.clear();
            //添加自定义菜单选项文字
            MenuInflater inflater = new MenuInflater(context);
            inflater.inflate(R.menu.expressreader_menu_pop_action, menu);
            //设置点击事件
            menuItemSelected(menu);
        }
        mActionMode = actionMode;
        return actionMode;
    }

    private void menuItemSelected(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            final int temp = i;
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (mJsToAndroid != null) {
                        mJsToAndroid.getSelectionText(ActionWebView.this, temp);
                    }
                    //释放当前菜单
                    releaseAction();
                    return true;
                }
            });
        }
    }

    public void releaseAction() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    public void addJsInterface(@NonNull JsToAndroid object, String name) {
        this.mJsToAndroid = object;
        this.mActionCallback = object.getActionCallback();
        addJavascriptInterface(object, name);
    }

    /**
     * WebView客户端对象
     */
    public class ActionWebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // 当前由于是通过HttpContext拼接的内容部分，图片和视频标签未做适配，需要在注入此段Js样式
            // 后期换成html5链接之后可以去除
            if (mJsToAndroid != null) {
                mJsToAndroid.resetElementStyle(ActionWebView.this);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //防止当前应用采用系统自带的浏览器打开，需要复写此方法；
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }

    //全屏监听对象
    WebChromeClient.CustomViewCallback mCallback;
    View mCustomView;

    class ErWebChromeClient extends WebChromeClient {

        //H5请求全屏时会调用此方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            Log.d(TAG, "onShowCustomView: >>>");


            mCustomView = view;
            mCallback = callback;

            //开始视屏播放
            if (mActionCallback != null) {
                mActionCallback.onPlayVideo(true, mCustomView);
            }

        }

        //H5推出全屏时会调用
        @Override
        public void onHideCustomView() {
            Log.d(TAG, "onHideCustomView: >>>");

            //停止全屏播放
            if (mActionCallback != null) {
                mActionCallback.onPlayVideo(false, mCustomView);
                mCustomView = null;
            }

            if (mCallback != null) {
                mCallback.onCustomViewHidden();
                mCallback = null;
            }

        }

    }

}
