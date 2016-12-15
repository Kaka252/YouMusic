package com.zhouyou.remote.client;

import com.zhouyou.remote.Music;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 */
public class MusicMsgFactory {

    public static Music setupMusic(int audioId, String audioPath, int currentPosition) {
        Music intent = new Music();
        intent.setAudioId(audioId);
        intent.setAudioPath(audioPath);
        intent.setCurrentPosition(currentPosition);
        return intent;
    }
}
