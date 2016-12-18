package com.zhouyou.music.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.library.utils.T;
import com.zhouyou.music.base.App;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.entity.AudioLocalDataManager;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicServiceSDK;

import java.util.ArrayList;
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

    private boolean isPlayBack;

    public void setPlayBack(boolean isPlayBack) {
        this.isPlayBack = isPlayBack;
    }

    public boolean isPlayBack() {
        return isPlayBack;
    }

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
        String musicPath = MusicServiceSDK.get().getMusicPath();
        for (Audio mAudio : audioList) {
            if (mAudio == null || TextUtils.isEmpty(mAudio.path)) continue;
            if (TextUtils.equals(musicPath, mAudio.path)) {
                currAudio = mAudio;
            }
        }
        return currAudio;
    }

    public Audio getCurrAudio() {
        return currAudio;
    }

    public Audio getCacheAudio() {
        Audio audio;
        if (getCurrentPlayingMusicState() == State.IDLE || getCurrentPlayingMusicState() == State.PREPARED) {
            audio = getPlayingMusic();
        } else {
            audio = getCurrAudio();
        }
        return audio;
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

    /**
     * 播放下一首
     */
    public Audio getNext() {
        List<Audio> list = getPlayList();
        if (ListUtils.isEmpty(list)) return null;
        Audio mAudio;
        if (currAudio == null) {
            mAudio = ListUtils.getElement(list, 0);
        } else {
            int index = 0;
            for (Audio audio : list) {
                if (audio == null) continue;
                if (audio.id == currAudio.id) {
                    index = list.indexOf(audio) + 1;
                    if (index >= list.size()) {
                        index = 0;
                    }
                }
            }
            mAudio = ListUtils.getElement(list, index);
        }
        return mAudio;
    }

    /**
     * 播放上一首
     */
    public Audio getLast() {
        List<Audio> list = getPlayList();
        if (ListUtils.isEmpty(list)) return null;
        Audio mAudio;
        if (currAudio == null) {
            mAudio = ListUtils.getElement(list, 0);
        } else {
            int index = 0;
            for (Audio audio : list) {
                if (audio == null) continue;
                if (audio.id == currAudio.id) {
                    index = list.indexOf(audio) - 1;
                    if (index < 0) {
                        index = list.size() - 1;
                    }
                }
            }
            mAudio = ListUtils.getElement(list, index);
        }
        return mAudio;
    }

    /**
     * 是否是当前播放的音乐
     *
     * @param path
     * @return
     */
    public boolean isPlayingCurrentMusic(String path) {
        return !TextUtils.isEmpty(path) && TextUtils.equals(path, getCurrentPlayingMusicPath());
    }

    /**
     * 获取当前正在播放的音乐路径
     *
     * @return
     */
    public String getCurrentPlayingMusicPath() {
        return MusicServiceSDK.get().getMusicPath();
    }

    /**
     * 获取当前正在播放的音乐状态
     *
     * @return
     */
    public int getCurrentPlayingMusicState() {
        return MusicServiceSDK.get().getState();
    }

    /**
     * 音乐是否正处于播放中
     *
     * @return
     */
    public boolean isMusicPlaying() {
        return getCurrentPlayingMusicState() == State.IN_PROGRESS;
    }

    /**
     * 生成播放列表，并开始播放指定音乐
     *
     * @param data          播放列表
     * @param selectedMusic 播放歌曲
     */
    public void playMusic(List<Audio> data, String selectedMusic) {
        if (ListUtils.isEmpty(data)) {
            T.ss("没有可用的音乐播放列表");
            return;
        }
        ArrayList<String> playList = new ArrayList<>();
        for (Audio audio : data) {
            if (audio == null || TextUtils.isEmpty(audio.path)) continue;
            playList.add(audio.path);
        }
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putStringArrayList("playList", playList);
        b.putString("selectedMusic", selectedMusic);
        intent.putExtras(b);
        MusicServiceSDK.get().playMusicList(intent);
    }

    /**
     * 暂停播放
     */
    public void pause() {
        MusicServiceSDK.get().pause();
    }

    /**
     * 继续播放
     */
    public void resume() {
        MusicServiceSDK.get().resume();
    }

    /**
     * 完成播放
     *
     * @param isPlayBack 是否播放上一首
     */
    public void complete(boolean isPlayBack) {
        MusicServiceSDK.get().complete();
    }

    /**
     * 完成播放 - 下一首播放
     */
    public void complete() {
        complete(false);
    }
}
