package com.cnpeak.expressreader;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.cnpeak.expressreader.model.db.ErDatabase;
import com.cnpeak.expressreader.model.db.ErDatabaseCreator;
import com.cnpeak.expressreader.utils.LocaleHelper;
import com.cnpeak.expressreader.utils.LogUtils;

/**
 * Application实例
 */
public class ErApplication extends Application {
    private static final String TAG = "ErApplication";
    //当前Application类实例
    private static ErApplication mErApplication;
    //数据库访问对象
    private static ErDatabase mErDatabase;

    public void onCreate() {
        super.onCreate();
        mErApplication = this;

        initLogger();

        initLocaleDev();

        initDatabase();
    }

    /**
     * 是否开启调试日志 - - 测试期间开启debug模式
     */
    private void initLogger() {
        boolean debug = BuildConfig.DEBUG;
        LogUtils.setLogEnable(debug);
        LogUtils.d("isDebug ? >>> " + debug);
    }

    /**
     * 初始化语言Locale环境
     */
    private void initLocaleDev() {
        LocaleHelper.initLCID(this);
    }

    /**
     * 初始化Room数据库对象
     */
    private void initDatabase() {
        mErDatabase = ErDatabaseCreator.create(this);
    }

    /**
     * 获取数据库管理类对象
     */
    public static ErDatabase getErDatabase() {
        return mErDatabase;
    }

    /**
     * Gets app context.
     *
     * @return the app context
     */
    public static ErApplication getAppContext() {
        return mErApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        LogUtils.d(TAG, "ErApplication attachBaseContext: >>>");
        super.attachBaseContext(LocaleHelper.attachBaseContext(base));
    }

    /**
     * Handling Configuration Changes
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d(TAG, "ErApplication onConfigurationChanged: >>>");
        LocaleHelper.applyLocaleChanged(this);
    }

}
