package com.zhouyou.music.media;

import com.zhouyou.music.entity.Audio;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/19.
 */
public interface IAudioTask {

    /**
     * 继续播放
     */
    boolean resume(Audio audio);

    /**
     * 暂停播放
     */
    void pause(Audio audio);

    /**
     * 播放下一个
     */
    void playNext(Audio audio);

    /**
     * 播放上一个
     */
    void playBack(Audio audio);

    /**
     * 获取音频列表
     *
     * @return
     */
    List<Audio> getAudioList();

}
