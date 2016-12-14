package com.zhouyou.remote.client;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import com.zhouyou.remote.IMusicReceiver;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.observer.MusicManager;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 * 信号接受者
 */
public class Receiver extends IMusicReceiver.Stub {

    @Override
    public void onReceive(int currState) throws RemoteException {
        dispatch(currState);
    }

    /**
     * 消息分发
     * @param currState
     */
    private void dispatch(int currState) {
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
                handler.sendEmptyMessageDelayed(ACTION_PLAY_NEXT, 2000);
                break;
            default:
                break;
        }
        MusicManager.get().createAudioStatePublisher().notifySubscribers(currState);
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
//                    currentPosition = 0;
                    break;
                case ACTION_PLAY_NEXT:
//                    playNext();
                    break;
                case ACTION_PLAY_BACK:
//                    playBack();
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
