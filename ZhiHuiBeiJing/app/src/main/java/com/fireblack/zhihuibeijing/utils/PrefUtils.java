package com.fireblack.zhihuibeijing.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ChengHao on 2016/7/18.
 *
 */
public class PrefUtils {
    public static final String PREF_NAME = "config";
    public static boolean getBoolean(Context context, String key, Boolean defaultValue){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return preferences.getBoolean(key,defaultValue);
    }
    public static void putBoolean(Context context,String key, boolean value){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key,value).commit();
    }
    public static String getString(Context context, String key, String defaultValue){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }
    public static void putString(Context context,String key, String value){
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        preferences.edit().putString(key,value).commit();
    }
}
