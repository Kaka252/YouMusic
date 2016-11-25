package com.zhouyou.music.media;

import com.zhouyou.music.media.state.AudioProgressPublisher;
import com.zhouyou.music.media.state.AudioStatePublisher;
import com.zhouyou.music.media.state.IAudioProgressPublisher;
import com.zhouyou.music.media.state.IAudioStatePublisher;

/**
 * 作者：ZhouYou
 * 日期：2016/11/25.
 */
public class AudioManagerFactory {

    private static class AMFHolder {
        private static final AudioManagerFactory FACTORY = new AudioManagerFactory();
    }

    public static AudioManagerFactory get() {
        return AMFHolder.FACTORY;
    }

    private AudioManagerFactory() {

    }

    private IAudioProgressPublisher progressPublisher = new AudioProgressPublisher();

    public IAudioProgressPublisher createProgressPublisher() {
        return progressPublisher;
    }

    private IAudioStatePublisher statePublisher = new AudioStatePublisher();

    public IAudioStatePublisher createAudioStatePublisher() {
        return statePublisher;
    }
}
