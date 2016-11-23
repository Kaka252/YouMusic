package com.zhouyou.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by leo on 2014/12/19.
 */
public class SP {

    private static final String SP_NAME = Lib.getApplication().getPackageName();

    private SharedPreferences sp;

    private SP(Context context) {
        if (context == null) {
            throw new NullPointerException("初始化" + SP.class.getName() + "类时Context实例不能为空");
        }
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private static SP instance;

    public static synchronized SP get() {
        if (instance == null) {
            instance = new SP(Lib.getApplication().getApplicationContext());
        }
        return instance;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key) {
        return getFloat(key, 0f);
    }

    public float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public Set<String> putStringSet(String key, Set<String> set) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key, set);
        editor.apply();
        return set;
    }

    public Set<String> getStringSet(String key) {
        return getStringSet(key, new HashSet<String>());
    }

    public Set<String> getStringSet(String key, Set<String> set) {
        return sp.getStringSet(key, set);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

}
