package com.zhouyou.library.utils;

import android.app.Application;

/**
 * 作者：ZhouYou
 * 日期：2016/11/23.
 */
public class Lib {

    private static Application app;

    public static void init(Application app) {
        Lib.app = app;
    }

    public static Application getApplication() {
        if (Lib.app == null) {
            throw new IllegalArgumentException("LBase application is null");
        }
        return Lib.app;
    }
}
