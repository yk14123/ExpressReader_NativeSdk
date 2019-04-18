package com.cnpeak.expressreader.net;

import com.cnpeak.expressreader.global.ErConstant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author builder by HUANG JIN on 18/11/6
 * @version 1.0
 * Retrofit+OkHttpClient封装的网络请求框架
 */
public class ApiManager {
    private static final int CONNECT_TIME_OUT = 10000;
    private static final int READ_TIME_OUT = 10000;
    private static final int WRITE_TIME_OUT = 10000;
    //采用单例模式封装Retrofit
    private static Retrofit sRetrofit;

    public static ApiManager builder() {
        return Holder.sApiManager;
    }

    //静态方法提供ApiManager实例 --->内部封装ApiManager访问实例
    private static class Holder {

        private static ApiManager sApiManager = new ApiManager();

        private Holder() {
        }

    }

    private ApiManager() {
        //初始化OkHttpClient+Retrofit
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .followRedirects(true)
                .sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager())
                .build();
        sRetrofit = new Retrofit.Builder()
                .baseUrl(ErConstant.BASE_SERVER_URL)
//                .baseUrl(ErConstant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClient)
                .build();
    }

    //获取ApiService接口调用对象
    public ApiService getService() {
        return sRetrofit.create(ApiService.class);
    }

}
