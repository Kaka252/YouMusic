package com.zhouyou.music.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.zhouyou.library.utils.Scale;
import com.zhouyou.music.R;
import com.zhouyou.music.base.App;
import com.zhouyou.music.config.Constants;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.module.AudioDetailActivity;
import com.zhouyou.music.module.MainActivity;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.remote.State;

/**
 * 作者：ZhouYou
 * 日期：2016/12/12.
 */
public class NotificationReceiver {

    private static final String TAG = "NotificationReceiver";

    private static final int REQUEST_MAIN_ACTIVITY = 0;
    private static final int REQUEST_PAUSE = 1;
    private static final int REQUEST_RESUME = 2;
    private static final int REQUEST_NEXT = 3;
    private static final int REQUEST_SHUT_DOWN = 999;

    private static class NotificationHelper {
        private static final NotificationReceiver HELPER = new NotificationReceiver();
    }

    public static NotificationReceiver get() {
        return NotificationHelper.HELPER;
    }

    private Context context;

    private NotificationReceiver() {
        context = App.get().getApplicationContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.RECEIVER_AUDIO_NOTIFICATION);
        context.registerReceiver(notifyActionReceiver, filter);
    }

    private RemoteViews remoteViews;
    private NotificationManager manager;

    @SuppressLint("NewApi")
    public void sendNotification(int state) {
        if (!setupRemoteViews(state)) return;
        setupAction(state);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notify = builder.build();
        notify.contentView = remoteViews; // 设置下拉图标
        notify.bigContentView = remoteViews; // 防止显示不完全,需要添加apisupport
        notify.flags = Notification.FLAG_ONGOING_EVENT;
        notify.icon = R.mipmap.ic_app_logo;
        manager.notify(100, notify);
    }

    private boolean setupRemoteViews(int state) {
        Audio audio = ClientCoreSDK.get().getPlayingMusic();
        if (audio == null) return false;
        remoteViews = new RemoteViews(App.get().getPackageName(), R.layout.view_notification_bar);
        remoteViews.setTextViewText(R.id.tv_audio_title, audio.title);
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(audio.artist)) sb.append(audio.artist).append(" - ");
        if (!TextUtils.isEmpty(audio.album)) sb.append(audio.album);
        remoteViews.setTextViewText(R.id.tv_audio_info, sb.toString());
        Bitmap bm = MediaUtils.getThumbnail(context, audio.id, audio.albumId, MediaUtils.COMPRESS_LEVEL_SMALL);
        remoteViews.setImageViewBitmap(R.id.iv_album, bm);
        if (state == State.PAUSED) {
            remoteViews.setImageViewResource(R.id.iv_play_now, R.mipmap.ic_play);
        } else {
            remoteViews.setImageViewResource(R.id.iv_play_now, R.mipmap.ic_pause);
        }
        return true;
    }

    /**
     * 设置点击事件
     */
    private void setupAction(int state) {
        // 返回主页面
        Intent intentMain = new Intent(context, MainActivity.class);
        intentMain.putExtra(Constants.DATA_BOOLEAN, true);
        PendingIntent actionMain = PendingIntent.getActivity(context, REQUEST_MAIN_ACTIVITY, intentMain, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ll_notification, actionMain);
        // 暂停/播放
        if (state == State.PAUSED) {
            Intent intentPlay = new Intent();
            intentPlay.setAction(Constants.RECEIVER_AUDIO_NOTIFICATION);
            intentPlay.putExtra(Constants.DATA_INT, REQUEST_RESUME);
            PendingIntent actionPlay = PendingIntent.getBroadcast(context, REQUEST_RESUME, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.iv_play_now, actionPlay);
        } else {
            Intent intentPause = new Intent();
            intentPause.setAction(Constants.RECEIVER_AUDIO_NOTIFICATION);
            intentPause.putExtra(Constants.DATA_INT, REQUEST_PAUSE);
            PendingIntent actionPause = PendingIntent.getBroadcast(context, REQUEST_PAUSE, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.iv_play_now, actionPause);
        }

        // 播放下一首
        Intent intentNext = new Intent();
        intentNext.setAction(Constants.RECEIVER_AUDIO_NOTIFICATION);
        intentNext.putExtra(Constants.DATA_INT, REQUEST_NEXT);
        PendingIntent actionNext = PendingIntent.getBroadcast(context, REQUEST_NEXT, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_play_next, actionNext);

        // 关闭
        Intent intentClose = new Intent();
        intentClose.setAction(Constants.RECEIVER_AUDIO_NOTIFICATION);
        intentClose.putExtra(Constants.DATA_INT, REQUEST_SHUT_DOWN);
        PendingIntent actionClose = PendingIntent.getBroadcast(context, REQUEST_SHUT_DOWN, intentClose, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_shut_down, actionClose);
    }

    private BroadcastReceiver notifyActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data) {
            if (TextUtils.equals(data.getAction(), Constants.RECEIVER_AUDIO_NOTIFICATION)) {
                int action = data.getIntExtra(Constants.DATA_INT, 0);
                if (action == REQUEST_PAUSE) {
                    ClientCoreSDK.get().pause();
                } else if (action == REQUEST_RESUME) {
                    ClientCoreSDK.get().resume(-1);
                } else if (action == REQUEST_NEXT) {
                    Audio audio = ClientCoreSDK.get().getNextOne();
                    if (audio != null) {
                        ClientCoreSDK.get().playMusic(audio.path);
                    }
                } else if (action == REQUEST_SHUT_DOWN) {
                    cancel();
                }
            }
        }
    };


    private void cancel() {
        if (remoteViews != null) {
            manager.cancel(100);
            remoteViews = null;
        }
    }

    public void destroy() {
        cancel();
        if (context != null && notifyActionReceiver != null) {
            try {
                context.unregisterReceiver(notifyActionReceiver);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "destroy: 广播未注册");
                e.printStackTrace();
            }

        }
    }
}
