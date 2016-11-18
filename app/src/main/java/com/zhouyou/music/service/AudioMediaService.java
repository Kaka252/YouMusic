package com.zhouyou.music.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 作者：ZhouYou
 * 日期：2016/11/18.
 */
public class AudioMediaService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
