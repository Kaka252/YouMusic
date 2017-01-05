package com.zhouyou.music.media;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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
    public void onUpdateChange(int state) {
        Log.d(TAG, "onUpdateChange: " + state);
        switch (state) {
            case State.IDLE:
                break;
            case State.INITIALIZED:
                break;
            case State.PREPARING:
                break;
            case State.PREPARED:
                NotificationReceiver.get().sendNotification();
                break;
            case State.IN_PROGRESS:
                break;
            case State.PAUSED:
                NotificationReceiver.get().sendNotification();
                break;
            case State.COMPLETED:
                Audio audio = ClientCoreSDK.get().getNextOne();
                if (audio != null) {
                    ClientCoreSDK.get().playMusic(audio.path);
                }
                break;
            case State.STOPPED:
                break;
            case State.END:
                break;
            case State.ERROR:
                break;
            default:
                break;
        }
    }

    public static void startService(Context cxt) {
        if (cxt == null) {
            return;
        }
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
