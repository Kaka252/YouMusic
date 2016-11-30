package com.zhouyou.music.base;

import android.app.Application;

import com.zhouyou.library.utils.Lib;
import com.zhouyou.library.utils.PrefUtils;
import com.zhouyou.music.service.AudioMediaService;

/**
 * 作者：ZhouYou
 * 日期：2016/11/18.
 */
public class App extends Application {

    private static App instance;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Lib.init(this);
        PrefUtils.init(this);
        AudioMediaService.startService(getApplicationContext());
    }
}
