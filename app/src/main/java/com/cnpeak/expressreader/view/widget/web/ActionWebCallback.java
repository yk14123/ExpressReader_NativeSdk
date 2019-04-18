package com.cnpeak.expressreader.view.widget.web;

import android.view.View;

import java.util.List;

/**
 * ActionWebView内部弹出菜单功能回调
 */
public interface ActionWebCallback {

    /**
     * 查看大图
     *
     * @param imgUrls      图片列表
     * @param defaultIndex 是否显示默认索引位置
     */
    void onZoomPicture(List<String> imgUrls, int defaultIndex);

    /**
     * On tts.
     *
     * @param menuId  the menu id
     * @param content the content
     */
    void onTranslate(int menuId, String content);

    /**
     * On play video.
     *
     * @param fullScreen the full screen
     * @param customView the custom view
     */
    void onPlayVideo(boolean fullScreen, View customView);

}
