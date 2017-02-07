package com.zhouyou.library.utils;

import com.zhouyou.library.utils.app.IApplication;

/**
 * 作者：ZhouYou
 * 日期：2016/11/23.
 */
public class Lib {

    private static IApplication app;

    public static void init(IApplication app) {
        Lib.app = app;
    }

    public static IApplication getApplication() {
        if (Lib.app == null) {
            throw new IllegalArgumentException("LBase application is null");
        }
        return Lib.app;
    }
}
