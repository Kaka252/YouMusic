package com.zhouyou.remote.client.observer;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 */
public class MusicManager {

    private static class AMFHolder {
        private static final MusicManager FACTORY = new MusicManager();
    }

    public static MusicManager get() {
        return AMFHolder.FACTORY;
    }

    private MusicManager() {

    }

    private IMusicStatePublisher statePublisher = new MusicStatePublisher();

    public IMusicStatePublisher createAudioStatePublisher() {
        return statePublisher;
    }
}
