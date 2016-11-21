package com.zhouyou.music.service;

import android.os.Binder;

import com.zhouyou.music.media.MusicPlaySDK;

/**
 * 作者：ZhouYou
 * 日期：2016/11/19.
 */
public class AudioTask extends Binder {

    private MusicPlaySDK sdk;

    public AudioTask() {
        sdk = MusicPlaySDK.get();
    }
}
