package com.zhouyou.music.media;

import com.zhouyou.music.media.state.AudioStateManager;
import com.zhouyou.music.media.state.IAudioStateManager;

/**
 * 作者：ZhouYou
 * 日期：2016/11/25.
 */
public class AudioManagerFactory {

    private static AudioManagerFactory factory = new AudioManagerFactory();

    public static AudioManagerFactory get() {
        return factory;
    }

    private AudioManagerFactory() {

    }

    private IAudioStateManager audioStateManager = new AudioStateManager();

    public IAudioStateManager createAudioStateManager() {
        return audioStateManager;
    }
}
