package com.zhouyou.remote.server;

import android.media.MediaPlayer;
import android.os.RemoteException;
import android.util.Log;

import com.zhouyou.remote.IMusicControlInterface;
import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicMsgFactory;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 */
public class MPBinder extends IMusicControlInterface.Stub implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = MPBinder.class.getSimpleName();

    private MediaPlayer mediaPlayer;

    @Override
    public void init() throws RemoteException {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            switchMediaState(State.IDLE);
        } else {
            if (isReset()) {
                mediaPlayer.reset();
                switchMediaState(State.IDLE);
            }
        }
    }

    @Override
    public void play(Music intent) throws RemoteException {

    }

    @Override
    public void switchMediaState(int state) throws RemoteException {
        MusicMsgFactory.setMediaState(state);
        switch (state) {
            case State.IDLE:
                Log.d(TAG, "AudioState: " + State.IDLE + " - 闲置");
                break;
            case State.INITIALIZED: // 初始化
                Log.d(TAG, "AudioState: " + State.INITIALIZED + " - 初始化");
                break;
            case State.PREPARING: // 正在准备
                mediaPlayer.prepareAsync();
                Log.d(TAG, "AudioState: " + State.PREPARING + " - 正在准备");
                break;
            case State.PREPARED: // 准备就绪
                Log.d(TAG, "AudioState: " + State.PREPARED + " - 准备就绪");
//                if (currentPosition > 0 && currentPosition <= getCurrentAudioDuration()) {
//                    mediaPlayer.seekTo(currentPosition);
//                }
                mediaPlayer.start();
                switchMediaState(State.IN_PROGRESS);
                break;
            case State.PAUSED: // 暂停
                Log.d(TAG, "AudioState: " + State.PAUSED + " - 暂停");
                mediaPlayer.pause();
//                handler.sendEmptyMessage(ACTION_PROGRESS_SUSPEND);
//                handler.sendEmptyMessage(ACTION_NOTIFICATION);
            case State.COMPLETED: // 播放完成
                Log.d(TAG, "AudioState: " + State.COMPLETED + " - 播放完成");
//                handler.sendEmptyMessageDelayed(isPlayBack ? ACTION_PLAY_BACK : ACTION_PLAY_NEXT, 100);
//                handler.sendEmptyMessage(ACTION_INIT);
                break;
            case State.STOPPED: // 播放终断
                Log.d(TAG, "AudioState: " + State.STOPPED + " - 播放终断");
                mediaPlayer.stop();
//                handler.sendEmptyMessage(ACTION_INIT);
                break;
            case State.END: // 结束
                Log.d(TAG, "AudioState: " + State.END + " - 结束");
                mediaPlayer.stop();
//                handler.sendEmptyMessage(ACTION_INIT);
                break;
            case State.ERROR: // 错误
                Log.d(TAG, "AudioState: " + State.ERROR + " - 错误");
                mediaPlayer.reset();
//                Toast.ss("音频文件出错");
//                handler.sendEmptyMessage(ACTION_INIT);
//                handler.sendEmptyMessageDelayed(ACTION_PLAY_NEXT, 2000);
                break;
            default:
                break;
        }
    }

    /**
     * 重置状态
     *
     * @return
     */
    private boolean isReset() {
        int currentState = MusicMsgFactory.getMediaState();
        return currentState == State.IDLE || currentState == State.INITIALIZED || currentState == State.PREPARED ||
                currentState == State.IN_PROGRESS || currentState == State.PAUSED || currentState == State.STOPPED ||
                currentState == State.COMPLETED || currentState == State.ERROR;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            switchMediaState(State.COMPLETED);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        try {
            switchMediaState(State.ERROR);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        try {
            switchMediaState(State.PREPARED);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
