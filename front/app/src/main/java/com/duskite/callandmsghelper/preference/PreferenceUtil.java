package com.duskite.callandmsghelper.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    private static PreferenceUtil preferenceUtil;
    private SharedPreferences sharedPreferences;


    private PreferenceUtil(Context context){
        sharedPreferences = context.getSharedPreferences("prefsName", Context.MODE_PRIVATE);
    }

    public static PreferenceUtil getInstance(Context context){
        if(preferenceUtil == null){
            preferenceUtil = new PreferenceUtil(context);
        }
        return preferenceUtil;
    }

    public void setString(String key, String value){
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key){
        return sharedPreferences.getString(key, null).toString();
    }
}
