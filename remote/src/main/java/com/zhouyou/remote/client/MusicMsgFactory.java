package com.zhouyou.remote.client;

import com.zhouyou.remote.Music;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 */
public class MusicMsgFactory {

    public static Music setupMusic(String audioPath, int currentPosition) {
        Music intent = new Music();
        intent.setAudioPath(audioPath);
        intent.setCurrentPosition(currentPosition);
        return intent;
    }

    private static int currentState = 0;

    public static void setMediaState(int state) {
        currentState = state;
    }

    public static int getMediaState() {
        return currentState;
    }
}
