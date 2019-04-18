package com.cnpeak.expressreader.utils;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import androidx.annotation.StringDef;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.cnpeak.expressreader.global.ErConstant;

import java.util.Locale;

/**
 * 应用类语言环境设置帮助类
 */
public class LocaleHelper {

    private static final String TAG = "LocaleHelper";
    /**
     * 简体中文
     */
    public static final String LCID_ZH = "2052";
    /**
     * 英文
     */
    public static final String LCID_EN = "1033";
    /**
     * 阿拉伯语系
     */
    public static final String LCID_AR = "1025";
    /**
     * 西班牙语系
     */
    public static final String LCID_ES = "1034";
    /**
     * 俄文
     */
    public static final String LCID_RU = "1049";


    /**
     * The interface Lcid.
     */
    @StringDef({LCID_ZH, LCID_EN, LCID_AR, LCID_ES, LCID_RU})
    public @interface LCID {
    }

    /**
     * 初始化当前application的语系环境
     * 判断当前的配置中是否存在已存储的语系，如果有则默认适配当前保存的语系；
     * 如果没有则根据当前手机设置的默认语系进行设置
     *
     * @param application application的语言环境
     */
    public static void initLCID(Application application) {
        String locale = getLCID(application);
        if (TextUtils.isEmpty(locale)) {
            Resources resources = application.getResources();
            Configuration configuration = resources.getConfiguration();
            //兼容性api
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                LocaleList locales = configuration.getLocales();//多语言获取第一项
                Locale currentLocale = locales.get(0);
                locale = currentLocale.getLanguage();
            } else {
                locale = Locale.getDefault().getLanguage();
            }
            setLCID(application, locale);
        }
    }

    /**
     * 设定当前Application的locale选项
     * 根据系统的locale语系进行匹配，如果匹配不到所支持的列表项则默认为中文
     *
     * @param context the context
     * @param locale  the locale
     */
    public static void setLCID(Context context, String locale) {
        Log.d(TAG, "setLCID: locale >>> " + locale);
        switch (locale) {
            case LCID_EN:
                SpUtil.setString(context, ErConstant.LOCALE_OPTION, LCID_EN);
                break;
            case LCID_AR:
                SpUtil.setString(context, ErConstant.LOCALE_OPTION, LCID_AR);
                break;
            case LCID_ES:
                SpUtil.setString(context, ErConstant.LOCALE_OPTION, LCID_ES);
                break;
            case LCID_RU:
                SpUtil.setString(context, ErConstant.LOCALE_OPTION, LCID_RU);
                break;
            default://其他语系默认设置为中国区
                SpUtil.setString(context, ErConstant.LOCALE_OPTION, LCID_ZH);
                break;
        }
    }

    /**
     * 获取当前默认的locale选项
     *
     * @param context the context
     * @return the lcid
     */
    public static String getLCID(Context context) {
        String LCID = SpUtil.getString(context, ErConstant.LOCALE_OPTION);
        if (TextUtils.isEmpty(LCID)) {
            return LCID_ZH;
        } else
            return LCID;
    }

    /**
     * 获取当前的LCID符号，匹配对应的Locale
     */
    private static Locale getLocale(String lcid) {
        Locale locale;
        switch (lcid) {
            case LCID_EN:
                locale = Locale.ENGLISH;
                break;
            case LCID_ES:
                locale = new Locale("es");
                break;
            case LCID_AR:
                locale = new Locale("ar");
                break;
            case LCID_RU:
                locale = new Locale("ru");
                break;
            default:
                locale = Locale.CHINESE;
                break;
        }
        return locale;

    }

    /**
     * 获取当前LCID默认匹配的Locale
     *
     * @param context the context
     * @return the current locale
     */
    public static Locale getLocale(Context context) {
        String LCID = getLCID(context);
        return getLocale(LCID);
    }


    /**
     * 切换当前App的语言系统
     *
     * @param context 环境
     */
    public static void applyLocaleChanged(Context context) {
        String locale = getLCID(context);
        applyConfiguration(context, locale);
    }

    private static void applyConfiguration(Context context, String newLanguage) {
        LogUtils.d(TAG, "applyConfiguration newLanguage>>>" + newLanguage);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        // app locale
        Locale locale = getLocale(newLanguage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }


    /**
     * Attach base context context.
     *
     * @param context the context
     * @return the context
     */
    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, getLCID(context));
        } else {
            return context;
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String newLanguage) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        // app locale
        Locale locale;
        switch (newLanguage) {
            case LCID_ZH:
                locale = Locale.CHINESE;
                break;
            case LCID_EN:
                locale = Locale.ENGLISH;
                break;
            case LCID_ES:
                locale = new Locale("es");
                break;
            case LCID_AR:
                locale = new Locale("ar");
                break;
            case LCID_RU:
                locale = new Locale("ru");
                break;
            default:
                locale = Locale.CHINESE;
                break;
        }
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }
}
