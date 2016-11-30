package com.zhouyou.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 作者：周游
 * 日期：16/1/7
 * 偏好处理
 */
public class PrefUtils {

    private static SharedPreferences sp;

    /**
     * 获取偏好执行器
     *
     * @return
     */
    public static void init(Context context) {
        sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    /**
     * 添加偏好内容：异步，不会引起长时间等待
     *
     * @param key
     * @param value
     */
    public static void put(String key, Object value) {
        if (TextUtils.isEmpty(key) || value == null) return;
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof String ||
                value instanceof StringBuilder ||
                value instanceof StringBuffer) {
            editor.putString(key, String.valueOf(value));
            editor.apply();
        } else if (value instanceof Integer) {
            editor.putInt(key, (int) value);
            editor.apply();
        } else if (value instanceof Long) {
            editor.putLong(key, (long) value);
            editor.apply();
        } else if (value instanceof Float) {
            editor.putFloat(key, (float) value);
            editor.apply();
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (boolean) value);
            editor.apply();
        }
    }

    /**
     * 添加偏好内容：同步，可能会引起长时间等待
     *
     * @param key
     * @param value
     * @return 添加是否成功
     */
    public static boolean putSync(String key, Object value) {
        if (TextUtils.isEmpty(key) || value == null)
            return false;
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof String ||
                value instanceof StringBuilder ||
                value instanceof StringBuffer) {
            editor.putString(key, String.valueOf(value));
            return editor.commit();
        } else if (value instanceof Integer) {
            editor.putInt(key, (int) value);
            return editor.commit();
        } else if (value instanceof Long) {
            editor.putLong(key, (long) value);
            return editor.commit();
        } else if (value instanceof Float) {
            editor.putFloat(key, (float) value);
            return editor.commit();
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (boolean) value);
            return editor.commit();
        }
        return false;
    }

    /**
     * 获取字符串
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getString(key, null);
    }

    /**
     * 获取字符串
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    /**
     * 获取整型
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 获取整型
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    /**
     * 获取长整型
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        return getLong(key, 0L);
    }

    /**
     * 获取长整型
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    /**
     * 获取浮点型
     *
     * @param key
     * @return
     */
    public static float getFloat(String key) {
        return getFloat(key, 0F);
    }

    /**
     * 获取浮点型
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    /**
     * 获取布尔型
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * 获取布尔型
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 移除偏好内容：异步，不会引起长时间等待
     *
     * @param key
     */
    public static void remove(String key) {
        if (TextUtils.isEmpty(key)) return;
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 移除偏好内容：同步，可能会引起长时间等待
     *
     * @param key
     * @return
     */
    public static boolean removeSync(String key) {
        if (TextUtils.isEmpty(key)) return false;
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        return editor.commit();
    }

}
