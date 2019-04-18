package com.cnpeak.expressreader.interf;

public interface TranslateEventCallback {

    void onTranslateResult(String translate, String requestId);

    void onTranslateError(int errorCode, String errorMsg, String requestId);
}
