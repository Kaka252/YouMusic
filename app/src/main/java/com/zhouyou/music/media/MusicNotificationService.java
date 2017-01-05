package com.zhouyou.music.media;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhouyou.library.utils.PrefUtils;
import com.zhouyou.library.utils.T;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.notification.NotificationReceiver;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.observer.IMusicStateSubscriber;
import com.zhouyou.remote.client.observer.MusicManager;

/**
 * 作者：ZhouYou
 * 日期：2016/12/29.
 */
public class MusicNotificationService extends Service implements IMusicStateSubscriber {

    private static final String TAG = "MusicService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MusicManager.get().createAudioStatePublisher().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onUpdateChange(int state, String path) {
        Log.d(TAG, "onUpdateChange: " + state);
        switch (state) {
            case State.IDLE:
                break;
            case State.INITIALIZED:
                break;
            case State.PREPARING:
                ClientCoreSDK.get().saveCurrentPlayMusicPath(path);
                break;
            case State.PREPARED:
                NotificationReceiver.get().sendNotification(State.PREPARED);
                break;
            case State.IN_PROGRESS:
                break;
            case State.PAUSED:
                NotificationReceiver.get().sendNotification(State.PAUSED);
                break;
            case State.COMPLETED:
                handler.sendEmptyMessage(0);
                break;
            case State.STOPPED:
                break;
            case State.END:
                break;
            case State.ERROR:
                T.ss("加载音频文件失败");
                handler.sendEmptyMessageDelayed(0, 2000);
                break;
            default:
                break;
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Audio audio = ClientCoreSDK.get().getNextOne();
            if (audio == null) {
                T.ss("加载音频文件失败");
            } else {
                ClientCoreSDK.get().playMusic(audio.path);
            }
            return true;
        }
    });

    public static void startService(Context cxt) {
        if (cxt == null) return;
        Context context = cxt.getApplicationContext();
        Intent intent = new Intent(context, MusicNotificationService.class);
        try {
            context.startService(intent);
        } catch (Throwable t) {
            // 未知异常
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicManager.get().createAudioStatePublisher().unregister(this);
    }
}
