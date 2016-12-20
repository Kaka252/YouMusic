package com.zhouyou.remote.client;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

import com.zhouyou.remote.IMusicReceiver;
import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.observer.MusicManager;
import com.zhouyou.remote.constants.MusicConstants;

import java.util.ArrayList;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 * 信号接受者
 */
public class Receiver extends IMusicReceiver.Stub {

    private ArrayList<String> playList;
    /*当前播放状态*/
    private int currState;
    /*当前播放音乐的路径*/
    private String currMusicPath;
    /*当前音乐的进度*/
    private int currPlayingPosition;
    /*当前音乐的时长*/
    private int currPlayingDuration;

    public int getCurrState() {
        return currState;
    }

    public String getCurrMusicPath() {
        return currMusicPath;
    }

    @Override
    public void onReceive(Intent data) throws RemoteException {
        currMusicPath = data.getStringExtra(MusicConstants.MUSIC_SELECTED);
        currState = data.getIntExtra(MusicConstants.MUSIC_STATE, 0);
        playList = data.getStringArrayListExtra(MusicConstants.MUSIC_PLAY_LIST);
        currPlayingPosition = data.getIntExtra(MusicConstants.MUSIC_PLAYING_POSITION, 0);
        currPlayingDuration = data.getIntExtra(MusicConstants.MUSIC_PLAYING_DURATION, 0);
        dispatch();
    }

    /**
     * 播放列表是否被初始化
     *
     * @return true - 初始化了
     */
    boolean hasInitializedPlayList() {
        return playList != null && playList.size() > 0;
    }

    /**
     * 消息分发
     */
    private void dispatch() {
        switch (currState) {
            case State.IDLE: // 闲置
                handler.sendEmptyMessage(ACTION_INIT);
                break;
            case State.INITIALIZED: // 初始化
                break;
            case State.PREPARING: // 正在准备
                break;
            case State.PREPARED: // 准备就绪
                break;
            case State.IN_PROGRESS: // 播放中
                break;
            case State.PAUSED: // 暂停
                handler.sendEmptyMessage(ACTION_PROGRESS_SUSPEND);
                handler.sendEmptyMessage(ACTION_NOTIFICATION);
            case State.COMPLETED: // 播放完成
//                handler.sendEmptyMessageDelayed(isPlayBack ? ACTION_PLAY_BACK : ACTION_PLAY_NEXT, 100);
                handler.sendEmptyMessage(ACTION_INIT);
                break;
            case State.STOPPED: // 播放终断
                handler.sendEmptyMessage(ACTION_INIT);
                break;
            case State.END: // 结束
                handler.sendEmptyMessage(ACTION_INIT);
                break;
            case State.ERROR: // 错误
//                Toast.ss("音频文件出错");
                handler.sendEmptyMessage(ACTION_INIT);
                break;
            default:
                break;
        }
        mMainHandler.sendEmptyMessage(0);
    }

    private Handler mMainHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            MusicManager.get().createAudioStatePublisher().notifySubscribers();
            return true;
        }
    });

    private static final int ACTION_INIT = 0;
    private static final int ACTION_PROGRESS_UPDATE = 3; // 更新播放时间
    private static final int ACTION_PROGRESS_SUSPEND = 4; // 暂停
    private static final int ACTION_NOTIFICATION = 5; // 发送通知栏消息

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_INIT:
//                    currentPosition = 0;
                    break;
                case ACTION_PROGRESS_UPDATE:
//                    if (!isProgressControlledByUser) {
//                        AudioManagerFactory.get().createProgressPublisher().notifySubscribers(getCurrentAudioProgress(), getCurrentAudioDuration());
//                        handler.sendEmptyMessageDelayed(ACTION_PROGRESS_UPDATE, 1000);
//                    } else {
//                        handler.sendEmptyMessage(ACTION_PROGRESS_UPDATE);
//                    }
                    break;
                case ACTION_PROGRESS_SUSPEND:
                    handler.removeMessages(ACTION_PROGRESS_UPDATE);
                    break;
                case ACTION_NOTIFICATION:
//                    NotificationReceiver.get().sendNotification();
                    break;
                default:
                    break;
            }
            return true;
        }
    });
}
