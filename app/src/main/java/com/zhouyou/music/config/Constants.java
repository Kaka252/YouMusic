package com.zhouyou.music.config;

import com.zhouyou.music.base.App;

/**
 * 作者：ZhouYou
 * 日期：2016/11/21.
 */
public class Constants {

    private static final String PACKAGE = App.get().getPackageName() + ".";

    public static final String DATA_INT = PACKAGE + "DATA_INT";
    public static final String DATA_LONG = PACKAGE + "DATA_LONG";
    public static final String DATA_STRING = PACKAGE + "DATA_STRING";
    public static final String DATA_ENTITY = PACKAGE + "DATA_ENTITY";
    /**
     * 发送音频播放的状态广播
     */
    public static final String RECEIVER_AUDIO_STATE_CHANGE = PACKAGE + "RECEIVER_AUDIO_STATE_CHANGE";

    public static final String RECEIVER_AUDIO_NOTIFICATION = PACKAGE + "RECEIVER_AUDIO_NOTIFICATION";
}
