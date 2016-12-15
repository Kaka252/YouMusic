package com.zhouyou.music.media;

import android.content.Context;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.music.base.App;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.entity.AudioLocalDataManager;
import com.zhouyou.remote.client.MusicServiceSDK;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 * 客户端的数据管理类
 */
public class ClientCoreSDK {

    private Context context;
    /*当前播放的音乐*/
    private Audio currAudio;

    private static class ClientHolder {
        private static final ClientCoreSDK SDK = new ClientCoreSDK();
    }

    public static ClientCoreSDK get() {
        return ClientHolder.SDK;
    }

    private ClientCoreSDK() {
        context = App.get().getApplicationContext();
    }

    /**
     * 获取当前正在播放的音乐
     *
     * @return
     */
    public Audio getPlayingMusic() {
        List<Audio> audioList = getPlayList();
        if (ListUtils.isEmpty(audioList)) return null;
        int musicId = MusicServiceSDK.get().getMusicId();
        for (Audio mAudio : audioList) {
            if (mAudio == null) continue;
            if (mAudio.id == musicId) {
                currAudio = mAudio;
            }
        }
        return currAudio;
    }

    public Audio getCurrAudio() {
        return currAudio;
    }

    /**
     * 获取播放列表
     *
     * @return
     */
    public List<Audio> getPlayList() {
        List<Audio> audioList = AudioLocalDataManager.get().getAudioCacheList();
        if (ListUtils.isEmpty(audioList)) {
            audioList = AudioLocalDataManager.get().getAudioList();
        }
        return audioList;
    }
}