package com.cnpeak.expressreader.interf;

public interface TTSEventCallback {

    void onAuthError(String message);

    void onConnectError(String message, int errorCode);

}
