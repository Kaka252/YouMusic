package com.zhouyou.music.base;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.wonderkiln.blurkit.BlurKit;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.zhouyou.library.utils.Lib;
import com.zhouyou.library.utils.PrefUtils;
import com.zhouyou.music.db.DBHelper;
import com.zhouyou.remote.client.MusicServiceSDK;

/**
 * 作者：ZhouYou
 * 日期：2016/11/18.
 */
public class App extends Application {

    private static App instance;

    public static App get() {
        return instance;
    }

    public static DBHelper helper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Lib.init(this);
        BlurKit.init(this);
        PrefUtils.init(this);
        MusicServiceSDK.init(this).startMusicService();
        NoHttp.initialize(this, new NoHttp.Config().setConnectTimeout(30 * 1000).setReadTimeout(30 * 1000));
        Logger.setDebug(true);
        Logger.setTag("http");
        Stetho.initialize(
                Stetho.newInitializerBuilder(getApplicationContext())
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(getApplicationContext()))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(getApplicationContext()))
                        .build());
    }

    public DBHelper getHelper() {
        if (helper == null) {
            helper = DBHelper.getHelper(getApplicationContext());
        }
        return helper;
    }

}
