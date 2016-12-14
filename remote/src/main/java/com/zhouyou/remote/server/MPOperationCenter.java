package com.zhouyou.remote.server;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.zhouyou.remote.IMusicControlInterface;
import com.zhouyou.remote.IMusicReceiver;
import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicMsgFactory;

import java.io.IOException;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 */
public class MPOperationCenter extends IMusicControlInterface.Stub implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = MPOperationCenter.class.getSimpleName();

    private MediaPlayer mediaPlayer;
    private Context context;

    public MPOperationCenter(Context context) {
        this.context = context;
    }

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

    /**
     * 播放音乐
     * @param intent 音乐数据
     * @throws RemoteException
     */
    @Override
    public void play(Music intent) throws RemoteException {
        String audioPath = intent.getAudioPath();
        if (TextUtils.isEmpty(audioPath)) {
            switchMediaState(State.ERROR);
        } else {
            init();
            try {
                if (MusicMsgFactory.getMediaState() == State.IDLE) {
                    Uri uri = Uri.parse(audioPath);
                    mediaPlayer.setDataSource(context, uri);
                }
                switchMediaState(State.INITIALIZED);
                if (MusicMsgFactory.getMediaState() != State.ERROR) {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                if (MusicMsgFactory.getMediaState() == State.INITIALIZED || MusicMsgFactory.getMediaState() == State.STOPPED) {
//                    PrefUtils.put(Constants.DATA_INT, audio.id);
                    switchMediaState(State.PREPARING);
                }
            } catch (IOException e) {
                Log.e("MusicError", e.toString());
                switchMediaState(State.ERROR);
            }
        }
    }

    /**
     * 切换播放状态
     * @param state 播放状态
     * @throws RemoteException
     */
    @Override
    public void switchMediaState(int state) throws RemoteException {
        MusicMsgFactory.setMediaState(state);
        switch (state) {
            case State.IDLE: // 闲置
                break;
            case State.INITIALIZED: // 初始化
                break;
            case State.PREPARING: // 正在准备
                mediaPlayer.prepareAsync();
                break;
            case State.PREPARED: // 准备就绪
//                if (currentPosition > 0 && currentPosition <= getCurrentAudioDuration()) {
//                    mediaPlayer.seekTo(currentPosition);
//                }
                mediaPlayer.start();
                switchMediaState(State.IN_PROGRESS);
                break;
            case State.PAUSED: // 暂停
                mediaPlayer.pause();
            case State.COMPLETED: // 播放完成
                break;
            case State.STOPPED: // 播放终断
                mediaPlayer.stop();
                break;
            case State.END: // 结束
                mediaPlayer.stop();
                break;
            case State.ERROR: // 错误
                mediaPlayer.reset();
                break;
            default:
                break;
        }
        printLog(state); // 打印日志
        receiver.onReceive(state);
    }

    private IMusicReceiver receiver;
    /**
     * 注册接收回调
     * @param receiver
     */
    @Override
    public void registerReceiver(IMusicReceiver receiver) {
        this.receiver = receiver;
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

    /**
     * 打印日志
     */
    private void printLog(int state) {
        switch (state) {
            case State.IDLE:
                Log.d(TAG, "AudioState: " + State.IDLE + " - 闲置");
                break;
            case State.INITIALIZED: // 初始化
                Log.d(TAG, "AudioState: " + State.INITIALIZED + " - 初始化");
                break;
            case State.PREPARING: // 正在准备
                Log.d(TAG, "AudioState: " + State.PREPARING + " - 正在准备");
                break;
            case State.PREPARED: // 准备就绪
                Log.d(TAG, "AudioState: " + State.PREPARED + " - 准备就绪");
                break;
            case State.PAUSED: // 暂停
                Log.d(TAG, "AudioState: " + State.PAUSED + " - 暂停");
                break;
            case State.COMPLETED: // 播放完成
                Log.d(TAG, "AudioState: " + State.COMPLETED + " - 播放完成");
                break;
            case State.STOPPED: // 播放终断
                Log.d(TAG, "AudioState: " + State.STOPPED + " - 播放终断");
                break;
            case State.END: // 结束
                Log.d(TAG, "AudioState: " + State.END + " - 结束");
                break;
            case State.ERROR: // 错误
                Log.d(TAG, "AudioState: " + State.ERROR + " - 错误");
                break;
            default:
                break;
        }
    }
}
