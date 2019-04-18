package com.cnpeak.expressreader.net;

import android.os.Debug;
import android.util.Log;

import com.cnpeak.expressreader.BuildConfig;
import com.cnpeak.expressreader.global.ErConstant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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

    private OkHttpClient okHttpClient;

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
        initClient();
    }

    //初始化OkHttpClient+Retrofit
    private void initClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .followRedirects(true)
                .sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    //打印retrofit日志
                    Log.e("NetLog", "message = " + message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
        }
        okHttpClient = clientBuilder.build();

        sRetrofit = new Retrofit.Builder()
                .baseUrl(ErConstant.BASE_SERVER_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonFormatFactory.create())
                .client(okHttpClient)
                .build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    //获取ApiService接口调用对象
    public <T> T createServiceFrom(Class<T> clz) {
        //检查是否初始化
        if (sRetrofit == null) {
            initClient();
        }
        return sRetrofit.create(clz);
    }

}
