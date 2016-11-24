package com.zhouyou.music.media.receiver;

import com.zhouyou.music.entity.Audio;

/**
 * 作者：ZhouYou
 * 日期：2016/11/24.
 */
public interface OnAudioStateChangeListener {

    /**
     * 音频状态变化的回调函数
     *
     * @param audio 音频
     * @param state 状态
     */
    void onAudioStateChange(Audio audio, int state);
}
