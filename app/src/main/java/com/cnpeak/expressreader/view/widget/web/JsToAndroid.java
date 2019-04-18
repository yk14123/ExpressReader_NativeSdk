package com.cnpeak.expressreader.view.widget.web;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.cnpeak.expressreader.utils.LogUtils;

import java.util.Arrays;

/**
 * @author builder by HUANG JIN on 18/11/12
 * @version 1.0
 * Html页面与Android原生之间的调用对象
 */
public class JsToAndroid {
    private static final String TAG = JsToAndroid.class.getSimpleName();

    //回调事件
    private ActionWebCallback mActionCallback;

    public JsToAndroid(ActionWebCallback actionCallback) {
        this.mActionCallback = actionCallback;
    }

    ActionWebCallback getActionCallback() {
        return mActionCallback;
    }

    /**
     * 设置img、video标签的样式
     */
    void resetElementStyle(WebView webView) {
        webView.loadUrl("javascript:(function(){" +
                "var videos =document.getElementsByTagName('video');" +
                "var videoLen = videos.length;" +
                "for(var i =0;i<videoLen;i++){" +
                "videos[i].style.maxWidth = '100%';videos[i].style.maxHeight = 'auto';}" +
                "let images = document.getElementsByTagName('img');" +
                "let imgLen = images.length;let imgArr = new Array();" +
                "for(let i =0;i<imgLen;i++){" +
                "images[i].style.maxWidth = '100%';images[i].style.height = 'auto';" +
                "imgArr[i] = images[i].src;" +
                "images[i].onclick = function(){" +
                "window.JsToAndroid.onZoomPicture(imgArr, i);}" +
                "}})()");

    }

    /**
     * 获取当前选中的文本
     */
    void getSelectionText(WebView webView, int itemId) {
        String js = "(function getSelectedText() {" +
                "var txt;" +
                "var title = \"" + itemId + "\";" +
                "if (window.getSelection) {" +
                "txt = window.getSelection().toString();" +
                "} else if (window.document.getSelection) {" +
                "txt = window.document.getSelection().toString();" +
                "} else if (window.document.selection) {" +
                "txt = window.document.selection.createRange().text;" +
                "}" +
                "JsToAndroid.onTranslate(title,txt);" +
                "})()";
        webView.loadUrl("javascript:" + js);
    }


    /**
     * 查看大图
     *
     * @param imgUrls 图片链接地址
     * @param index   当前图片位置索引
     */
    @JavascriptInterface
    public void onZoomPicture(String[] imgUrls, int index) {
        if (imgUrls != null && imgUrls.length != 0) {
            if (mActionCallback != null) {
                mActionCallback.onZoomPicture(Arrays.asList(imgUrls), index);
            }
        } else {
            LogUtils.e(TAG, "js method checkImageByIndex error,imgUrls is null >>>");
        }
    }

    /**
     * 复制翻译功能
     *
     * @param menuId  标题
     * @param content 复制内容
     */
    @JavascriptInterface
    public void onTranslate(int menuId, String content) {
        if (!TextUtils.isEmpty(content)) {
            if (mActionCallback != null) {
                mActionCallback.onTranslate(menuId, content);
            }
        } else {
            LogUtils.e(TAG, "js method checkImageByIndex error,imgUrls is null >>>");
        }
    }

}
