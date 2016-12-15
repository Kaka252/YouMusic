package com.zhouyou.music.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.library.utils.PrefUtils;
import com.zhouyou.library.utils.T;
import com.zhouyou.music.base.App;
import com.zhouyou.music.config.Constants;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.entity.AudioLocalDataManager;
import com.zhouyou.music.media.state.AudioPlayState;
import com.zhouyou.music.notification.NotificationReceiver;
import com.zhouyou.remote.client.MusicServiceSDK;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/21.
 */
@Deprecated
public class MediaCoreSDK implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {


    private Context context;
    private int currState;
    private Audio currAudio;
    private MediaPlayer mediaPlayer;

    private static class SDKHolder {
        private static final MediaCoreSDK SDK = new MediaCoreSDK();
    }

    public static MediaCoreSDK get() {
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

    private MediaCoreSDK() {
        context = App.get().getApplicationContext();
        currAudio = AudioLocalDataManager.get().getLastPlayedAudio();
        if (currAudio == null) {
            currAudio = ListUtils.getElement(getPlayList(), 0);
        }
    }

    /**
     * 重置状态
     *
     * @return
     */
    private boolean isReset() {
        return currState == AudioPlayState.IDLE || currState == AudioPlayState.INITIALIZED || currState == AudioPlayState.PREPARED ||
                currState == AudioPlayState.IN_PROGRESS || currState == AudioPlayState.PAUSED || currState == AudioPlayState.STOPPED ||
                currState == AudioPlayState.COMPLETED || currState == AudioPlayState.ERROR;
    }

    /**
     * 初始化
     */
    public void init() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            changeState(AudioPlayState.IDLE);
        } else {
            if (isReset()) {
                mediaPlayer.reset();
                changeState(AudioPlayState.IDLE);
            }
        }
    }

    /**
     * 开始播放音频
     *
     * @param audio
     */
    public synchronized void prepare(Audio audio) {
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
                currAudio = audio;
                PrefUtils.put(Constants.DATA_INT, audio.id);
                changeState(AudioPlayState.PREPARING);
            }
        } catch (Exception e) {
            Log.e("MusicError", e.toString());
            changeState(AudioPlayState.ERROR);
        }
    }

    private int currentPosition = 0;

    /**
     * 更新进度
     *
     * @param progress
     */
    public void updateProgress(int progress) {
        float percent = progress * 1.0f / 100;
        if (mediaPlayer == null || currAudio == null) return;
        if (mediaPlayer.isPlaying()) {
            currentPosition = (int) (percent * mediaPlayer.getDuration());
            mediaPlayer.seekTo(currentPosition);
        } else {
            currentPosition = (int) (percent * currAudio.duration);
        }
    }

    /**
     * 获取播放进度
     */
    public int getCurrentAudioProgress() {
        if (mediaPlayer == null || currAudio == null) return 0;
        if (!mediaPlayer.isPlaying()) return currentPosition;
        currentPosition = mediaPlayer.getCurrentPosition();
        return currentPosition;
    }

    /**
     * 获取当前播放音频的时长
     */
    public int getCurrentAudioDuration() {
        int duration = 0;
        if (currAudio != null) {
            duration = currAudio.duration;
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            duration = mediaPlayer.getDuration();
        }
        return duration;
    }

    /**
     * 进度是否由用户控制
     */
    private boolean isProgressControlledByUser = false;

    public void setProgressControlledByUser(boolean progressControlledByUser) {
        isProgressControlledByUser = progressControlledByUser;
    }

    /**
     * 是否播放上一首
     */
    private boolean isPlayBack = false;

    public void setPlayBack(boolean isPlayBack) {
        this.isPlayBack = isPlayBack;
    }

    /**
     * 播放下一首
     */
    private void playNext() {
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
    private void playBack() {
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
        isPlayBack = false;
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
                handler.sendEmptyMessage(ACTION_INIT);
                break;
            case AudioPlayState.INITIALIZED: // 初始化
                Log.d("MusicState", "changeState: " + AudioPlayState.INITIALIZED + " - 初始化");
                break;
            case AudioPlayState.PREPARING: // 正在准备
                mediaPlayer.prepareAsync();
                Log.d("MusicState", "changeState: " + AudioPlayState.PREPARING + " - 正在准备");
                break;
            case AudioPlayState.PREPARED: // 准备就绪
                Log.d("MusicState", "changeState: " + AudioPlayState.PREPARED + " - 准备就绪");
                if (currentPosition > 0 && currentPosition <= getCurrentAudioDuration()) {
                    mediaPlayer.seekTo(currentPosition);
                }
                mediaPlayer.start();
                changeState(AudioPlayState.IN_PROGRESS);
                break;
            case AudioPlayState.IN_PROGRESS: // 播放中
                Log.d("MusicState", "changeState: " + AudioPlayState.IN_PROGRESS + " - 正在播放");
                handler.sendEmptyMessage(ACTION_PROGRESS_UPDATE);
                handler.sendEmptyMessage(ACTION_NOTIFICATION);
                break;
            case AudioPlayState.PAUSED: // 暂停
                Log.d("MusicState", "changeState: " + AudioPlayState.PAUSED + " - 暂停");
                mediaPlayer.pause();
                handler.sendEmptyMessage(ACTION_PROGRESS_SUSPEND);
                handler.sendEmptyMessage(ACTION_NOTIFICATION);
                break;
            case AudioPlayState.COMPLETED: // 播放完成
                Log.d("MusicState", "changeState: " + AudioPlayState.COMPLETED + " - 播放完成");
                handler.sendEmptyMessageDelayed(isPlayBack ? ACTION_PLAY_BACK : ACTION_PLAY_NEXT, 100);
                handler.sendEmptyMessage(ACTION_INIT);
                break;
            case AudioPlayState.STOPPED: // 播放终断
                Log.d("MusicState", "changeState: " + AudioPlayState.STOPPED + " - 播放终断");
                mediaPlayer.stop();
                handler.sendEmptyMessage(ACTION_INIT);
                break;
            case AudioPlayState.END: // 结束
                Log.d("MusicState", "changeState: " + AudioPlayState.END + " - 结束");
                mediaPlayer.stop();
                handler.sendEmptyMessage(ACTION_INIT);
                break;
            case AudioPlayState.ERROR: // 错误
                Log.d("MusicState", "changeState: " + AudioPlayState.ERROR + " - 错误");
                mediaPlayer.reset();
                T.ss("音频文件出错");
                handler.sendEmptyMessage(ACTION_INIT);
//                handler.sendEmptyMessageDelayed(ACTION_PLAY_NEXT, 2000);
                break;
            default:
                break;
        }
        // 发送通知
        AudioManagerFactory.get().createAudioStatePublisher().notifySubscribers(currAudio, currState);
    }

    private static final int ACTION_INIT = 0;
    private static final int ACTION_PLAY_NEXT = 1; // 播放下一首
    private static final int ACTION_PLAY_BACK = 2; // 播放上一首
    private static final int ACTION_PROGRESS_UPDATE = 3; // 更新播放时间
    private static final int ACTION_PROGRESS_SUSPEND = 4; // 暂停
    private static final int ACTION_NOTIFICATION = 5; // 发送通知栏消息

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_INIT:
                    currentPosition = 0;
                    break;
                case ACTION_PLAY_NEXT:
                    playNext();
                    break;
                case ACTION_PLAY_BACK:
                    playBack();
                    break;
                case ACTION_PROGRESS_UPDATE:
                    if (!isProgressControlledByUser) {
                        AudioManagerFactory.get().createProgressPublisher().notifySubscribers(getCurrentAudioProgress(), getCurrentAudioDuration());
                        handler.sendEmptyMessageDelayed(ACTION_PROGRESS_UPDATE, 1000);
                    } else {
                        handler.sendEmptyMessage(ACTION_PROGRESS_UPDATE);
                    }
                    break;
                case ACTION_PROGRESS_SUSPEND:
                    handler.removeMessages(ACTION_PROGRESS_UPDATE);
                    break;
                case ACTION_NOTIFICATION:
                    NotificationReceiver.get().sendNotification();
                    break;
                default:
                    break;
            }
            return true;
        }
    });
}
