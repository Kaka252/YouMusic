package com.zhouyou.music.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.wonderkiln.blurkit.BlurKit;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.zhouyou.library.utils.Lib;
import com.zhouyou.library.utils.PrefUtils;
import com.zhouyou.library.utils.app.IApplication;
import com.zhouyou.music.db.DBHelper;
import com.zhouyou.music.media.MusicNotificationService;
import com.zhouyou.network.okhttp.OkHttpSdk;
import com.zhouyou.remote.client.MusicServiceSDK;

/**
 * 作者：ZhouYou
 * 日期：2016/11/18.
 */
//@DefaultLifeCycle(
//        application = "com.zhouyou.music.SampleApplication",
//        flags = ShareConstants.TINKER_ENABLE_ALL,
//        loadVerifyFlag = false
//)
public class App extends Application implements IApplication {

    private static Context context;
    private static App instance;

//    public App(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
//        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
//    }

    public static App get() {
        return instance;
    }

    public static DBHelper helper;

//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//        MultiDex.install(base);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;
        context = getApplicationContext();
        Lib.init(this);
        BlurKit.init(context);
        PrefUtils.init(context);
        MusicServiceSDK.init(context).startMusicService();
        MusicNotificationService.startService(context);
        NoHttp.initialize(context, new NoHttp.Config().setConnectTimeout(30 * 1000).setReadTimeout(30 * 1000));
        Logger.setDebug(true);
        Logger.setTag("http");
        Stetho.initialize(
                Stetho.newInitializerBuilder(getApplicationContext())
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(getApplicationContext()))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(getApplicationContext()))
                        .build());
        // 配置Okhttp
        OkHttpSdk.initConfig(this);
    }

    public DBHelper getHelper() {
        if (helper == null) {
            helper = DBHelper.getHelper(getApplicationContext());
        }
        return helper;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public IApplication getInstance() {
        return instance;
    }

    @Override
    public String getPackageName() {
        return "com.zhouyou.music";
    }
}
