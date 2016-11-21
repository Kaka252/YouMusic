package com.zhouyou.music.service;

import android.os.Binder;

import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.IAudioTask;
import com.zhouyou.music.media.MusicPlaySDK;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/19.
 */
public class AudioTask extends Binder implements IAudioTask {

    private MusicPlaySDK sdk;

    public AudioTask() {
        sdk = MusicPlaySDK.get();
    }

    /**
     * 播放音乐
     *
     * @param audio 音频文件
     */
    @Override
    public boolean resume(Audio audio) {
        return sdk.prepare(audio);
    }

    /**
     * 暂停音乐
     *
     * @param audio
     */
    @Override
    public void pause(Audio audio) {

    }

    /**
     * 播放下一首
     *
     * @param audio
     */
    @Override
    public void playNext(Audio audio) {

    }

    /**
     * 播放上一首
     *
     * @param audio
     */
    @Override
    public void playBack(Audio audio) {

    }

    /**
     * 获取音频列表
     *
     * @return
     */
    @Override
    public List<Audio> getAudioList() {
        return sdk.getAudioList();
    }
}
