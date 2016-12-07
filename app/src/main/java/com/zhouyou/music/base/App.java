package com.zhouyou.music.base;

import android.app.Application;

import com.wonderkiln.blurkit.BlurKit;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
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
        BlurKit.init(this);
        PrefUtils.init(this);
        NoHttp.initialize(this, new NoHttp.Config().setConnectTimeout(30 * 1000).setReadTimeout(30 * 1000));
        Logger.setDebug(true);
        Logger.setTag("http");
        AudioMediaService.startService(this);
    }
}
