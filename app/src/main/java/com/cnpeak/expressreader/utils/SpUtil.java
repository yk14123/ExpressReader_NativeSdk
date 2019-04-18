package com.cnpeak.expressreader.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("ApplySharedPref")
public class SpUtil {

    private static SharedPreferences sSp;
    private static final String NAME = "expressreader_prefs";

    public static void setBoolean(Context context, String key, boolean b) {
        if (sSp == null)
            sSp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        sSp.edit().putBoolean(key, b).commit();
    }

    public static boolean getBoolean(Context context, String key) {
        if (sSp == null)
            sSp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sSp.getBoolean(key, false);
    }

    public static void setInt(Context context, String key, int b) {
        if (sSp == null)
            sSp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sSp.edit().putInt(key, b).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        if (sSp == null)
            sSp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sSp.getInt(key, defValue);
    }

    public static void setString(Context context, String key, String b) {
        if (sSp == null)
            sSp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sSp.edit().putString(key, b).commit();
    }

    public static String getString(Context context, String key) {
        if (sSp == null)
            sSp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sSp.getString(key, null);
    }
}
