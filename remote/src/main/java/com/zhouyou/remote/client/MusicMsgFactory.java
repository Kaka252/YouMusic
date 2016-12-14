package com.zhouyou.remote.client;

import com.zhouyou.remote.Music;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 */
public class MusicMsgFactory {

    public static Music currPlaying;

    public static Music createMusicMsg(int state, String audioPath, int currentPosition) {
        Music intent = new Music();
        intent.setState(state);
        intent.setAudioPath(audioPath);
        intent.setCurrentPosition(currentPosition);
        currPlaying = intent;
        return intent;
    }

    public static Music getCurrPlaying() {
        return currPlaying;
    }
}
