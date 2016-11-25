package com.zhouyou.music.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.music.base.App;
import com.zhouyou.music.entity.AudioLocalDataManager;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.state.AudioPlayState;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/21.
 */
public class MusicPlaySDK implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {


    private Context context;
    private int currState;
    private Audio currAudio;
    private MediaPlayer mediaPlayer;

    private static class SDKHolder {
        private static final MusicPlaySDK SDK = new MusicPlaySDK();
    }

    public static MusicPlaySDK get() {
        return SDKHolder.SDK;
    }

    /**
     * 获取当前音乐的播放状态
     *
     * @return
     */
    public int getCurrState() {
        return currState;
    }

    /**
     * 获取当前音乐的实体
     *
     * @return
     */
    public Audio getCurrAudio() {
        return currAudio;
    }

    private MusicPlaySDK() {
        context = App.get().getApplicationContext();
        currAudio = ListUtils.getElement(getPlayList(), 0);
    }

    /**
     * 初始化
     */
    public void init() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            changeState(AudioPlayState.IDLE);
        } else {
            if (isReset()) {
                mediaPlayer.reset();
                changeState(AudioPlayState.IDLE);
            }
        }
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    /**
     * 重置状态
     *
     * @return
     */
    private boolean isReset() {
        return currState == AudioPlayState.IDLE || currState == AudioPlayState.INITIALIZED || currState == AudioPlayState.PREPARED ||
                currState == AudioPlayState.PLAYING || currState == AudioPlayState.PAUSED || currState == AudioPlayState.STOPPED ||
                currState == AudioPlayState.COMPLETED || currState == AudioPlayState.ERROR;
    }

    /**
     * 开始播放音频
     *
     * @param audio
     */
    public void prepare(Audio audio) {
        if (audio == null) {
            changeState(AudioPlayState.ERROR);
            return;
        }
        init();
        try {
            if (currState == AudioPlayState.IDLE) {
                Uri uri = Uri.parse(audio.path);
                mediaPlayer.setDataSource(context, uri);
            }
            changeState(AudioPlayState.INITIALIZED);
            if (currState != AudioPlayState.ERROR) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            if (currState == AudioPlayState.INITIALIZED || currState == AudioPlayState.STOPPED) {
                mediaPlayer.prepareAsync();
                currAudio = audio;
                changeState(AudioPlayState.PREPARING);
            }
        } catch (Exception e) {
            e.printStackTrace();
            changeState(AudioPlayState.ERROR);
        }
    }

//    /**
//     * 开始播放
//     */
//    public void play() {
//        mediaPlayer.start();
//        changeState(AudioPlayState.STARTED);
//    }
//
//    /**
//     * 暂停
//     */
//    public void pause() {
//        mediaPlayer.pause();
//        changeState(AudioPlayState.PAUSED);
//    }
//
//    /**
//     * 停止
//     */
//    public void stop() {
//        mediaPlayer.stop();
//        changeState(AudioPlayState.STOPPED);
//    }

    /**
     * 获取播放进度
     */
    public int getCurrentPlayProgress() {
        float f = mediaPlayer.getCurrentPosition() * 1.0f / mediaPlayer.getDuration();
        return (int) (f * 100);
    }

    /**
     * 播放下一首
     */
    public void playNext() {
        List<Audio> list = getPlayList();
        if (ListUtils.isEmpty(list)) return;
        if (currAudio == null) {
            currAudio = ListUtils.getElement(list, 0);
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
            currAudio = ListUtils.getElement(list, index);
        }
        prepare(currAudio);
    }

    /**
     * 播放上一首
     */
    public void playBack() {
        List<Audio> list = getPlayList();
        if (ListUtils.isEmpty(list)) return;
        if (currAudio == null) {
            currAudio = ListUtils.getElement(list, 0);
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
            currAudio = ListUtils.getElement(list, index);
        }
        prepare(currAudio);
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

    @Override
    public void onCompletion(MediaPlayer mp) {
        changeState(AudioPlayState.COMPLETED);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        changeState(AudioPlayState.ERROR);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        changeState(AudioPlayState.PREPARED);
    }

    /**
     * 改变音乐的播放状态
     *
     * @param state
     */
    public void changeState(int state) {
        currState = state;
        switch (currState) {
            case AudioPlayState.IDLE: // 闲置
                Log.d("MusicState", "changeState: " + AudioPlayState.IDLE + " - 闲置");
                break;
            case AudioPlayState.INITIALIZED: // 初始化
                Log.d("MusicState", "changeState: " + AudioPlayState.INITIALIZED + " - 初始化");
                break;
            case AudioPlayState.PREPARING: // 正在准备
                Log.d("MusicState", "changeState: " + AudioPlayState.PREPARING + " - 正在准备");
                break;
            case AudioPlayState.PREPARED: // 准备就绪
                Log.d("MusicState", "changeState: " + AudioPlayState.PREPARED + " - 准备就绪");
                changeState(AudioPlayState.PLAYING);
                break;
            case AudioPlayState.PLAYING: // 正在播放
                Log.d("MusicState", "changeState: " + AudioPlayState.PLAYING + " - 正在播放");
                mediaPlayer.start();
                break;
            case AudioPlayState.PAUSED: // 暂停
                Log.d("MusicState", "changeState: " + AudioPlayState.PAUSED + " - 暂停");
                mediaPlayer.pause();
                break;
            case AudioPlayState.COMPLETED: // 播放完成
                Log.d("MusicState", "changeState: " + AudioPlayState.COMPLETED + " - 播放完成");
                handler.sendEmptyMessageDelayed(ACTION_PLAY_NEXT, 500);
                break;
            case AudioPlayState.STOPPED: // 播放终断
                Log.d("MusicState", "changeState: " + AudioPlayState.STOPPED + " - 播放终断");
                mediaPlayer.stop();
                break;
            case AudioPlayState.END: // 结束
                Log.d("MusicState", "changeState: " + AudioPlayState.END + " - 结束");
                mediaPlayer.stop();
                break;
            case AudioPlayState.ERROR: // 错误
                Log.d("MusicState", "changeState: " + AudioPlayState.ERROR + " - 错误");
                mediaPlayer.stop();
                Toast.makeText(context, "音频文件出错", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessageDelayed(ACTION_PLAY_NEXT, 2000);
                break;
            default:
                break;
        }
        // 发送通知
        AudioManagerFactory.get().createAudioStateManager().notifySubscribers(currAudio, currState);
    }

    private static final int ACTION_PLAY_NEXT = 1; // 播放下一首

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_PLAY_NEXT:
                    playNext();
                    break;
                default:
                    break;
            }
            return true;
        }
    });
}
