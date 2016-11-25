package com.zhouyou.music.service;

import com.zhouyou.music.entity.Audio;

/**
 * 作者：ZhouYou
 * 日期：2016/11/19.
 */
public interface OnAudioPlayCallback {

    /**
     * 音乐播放的状态
     *
     * @param audio 音频
     * @param state 状态
     */
    void onStateChanged(Audio audio, int state);
}
