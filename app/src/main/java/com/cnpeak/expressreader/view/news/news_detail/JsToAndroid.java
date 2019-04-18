package com.cnpeak.expressreader.view.news.news_detail;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.cnpeak.expressreader.utils.LogUtils;
import com.cnpeak.expressreader.utils.UIHelper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author builder by HUANG JIN on 18/11/12
 * @version 1.0
 * Html页面与Android原生之间的调用对象
 */
public class JsToAndroid {
    private static final String TAG = JsToAndroid.class.getSimpleName();
    //上下文环境
    private Context mContext;

    public JsToAndroid(Context context) {
        this.mContext = context;
    }

    /**
     * 设置img、video标签的样式
     */
    public void setHtmlElementStyle(WebView webView) {
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
                "window._expressReaderJsConnect.checkImageByIndex(imgArr, i);}" +
                "}})()");

    }

    /**
     * 根据索引查看当前图片的大图
     *
     * @param imgUrls 图片链接地址
     * @param index   当前图片位置索引
     */
    @JavascriptInterface
    public void checkImageByIndex(String[] imgUrls, int index) {
        if (imgUrls != null && imgUrls.length != 0) {
            LogUtils.i(TAG, "js method checkImageByIndex imgUrls.length >>>" + imgUrls.length + " index >>> " + index);
            UIHelper.startImageActivity(mContext, Arrays.asList(imgUrls), index, true);
        } else {
            LogUtils.e(TAG, "js method checkImageByIndex error,imgUrls is null >>>");
        }
    }

}
