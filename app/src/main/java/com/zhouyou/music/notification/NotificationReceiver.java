package com.zhouyou.music.notification;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
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
import com.zhouyou.music.media.MediaCoreSDK;
import com.zhouyou.music.media.state.AudioPlayState;
import com.zhouyou.music.module.AudioDetailActivity;
import com.zhouyou.music.module.MainActivity;
import com.zhouyou.music.module.utils.MediaUtils;

/**
 * 作者：ZhouYou
 * 日期：2016/12/12.
 */
public class NotificationReceiver {

    private static final int REQUEST_MAIN_ACTIVITY = 0;
    private static final int REQUEST_PAUSE = 1;
    private static final int REQUEST_PLAY = 2;
    private static final int REQUEST_NEXT = 3;

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
    public void sendNotification() {
        if (!setupRemoteViews()) return;
        setupAction();
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notify = builder.build();
        notify.contentView = remoteViews; // 设置下拉图标
        notify.bigContentView = remoteViews; // 防止显示不完全,需要添加apisupport
        notify.flags = Notification.FLAG_ONGOING_EVENT;
        notify.icon = R.mipmap.ic_app_logo;
        manager.notify(100, notify);
    }

    private boolean setupRemoteViews() {
        Audio audio = MediaCoreSDK.get().getCurrAudio();
        if (audio == null) return false;
        remoteViews = new RemoteViews(App.get().getPackageName(), R.layout.view_notification_bar);
        remoteViews.setTextViewText(R.id.tv_audio_title, audio.title);
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(audio.artist)) sb.append(audio.artist).append(" - ");
        if (!TextUtils.isEmpty(audio.album)) sb.append(audio.album);
        remoteViews.setTextViewText(R.id.tv_audio_info, sb.toString());
        Bitmap bm = MediaUtils.getAlbumCoverImage(context, audio.id, audio.albumId);
        if (bm != null) {
            int size = Scale.dp2px(context, 100);
            bm = MediaUtils.getAlbumCoverThumbnail(bm, size, size, true);
        }
        remoteViews.setImageViewBitmap(R.id.iv_album, bm);
        int state = MediaCoreSDK.get().getCurrState();
        if (state == AudioPlayState.IN_PROGRESS) {
            remoteViews.setImageViewResource(R.id.iv_play_now, R.mipmap.ic_pause);
        } else {
            remoteViews.setImageViewResource(R.id.iv_play_now, R.mipmap.ic_play);
        }
        return true;
    }

    /**
     * 设置点击事件
     */
    private void setupAction() {
        // 返回主页面
        Intent intentMain = new Intent(context, AudioDetailActivity.class);
        intentMain.putExtra(Constants.DATA_BOOLEAN, true);
        PendingIntent actionMain = PendingIntent.getActivity(context, REQUEST_MAIN_ACTIVITY, intentMain, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ll_notification, actionMain);

        // 暂停/播放
        int state = MediaCoreSDK.get().getCurrState();
        if (state == AudioPlayState.IN_PROGRESS) {
            Intent intentPause = new Intent();
            intentPause.setAction(Constants.RECEIVER_AUDIO_NOTIFICATION);
            intentPause.putExtra(Constants.DATA_INT, REQUEST_PAUSE);
            PendingIntent actionPause = PendingIntent.getBroadcast(context, REQUEST_PAUSE, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.iv_play_now, actionPause);
        } else if (state == AudioPlayState.PAUSED) {
            Intent intentPlay = new Intent();
            intentPlay.setAction(Constants.RECEIVER_AUDIO_NOTIFICATION);
            intentPlay.putExtra(Constants.DATA_INT, REQUEST_PLAY);
            PendingIntent actionPlay = PendingIntent.getBroadcast(context, REQUEST_PLAY, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.iv_play_now, actionPlay);
        }

        // 播放下一首
        Intent intentNext = new Intent();
        intentNext.setAction(Constants.RECEIVER_AUDIO_NOTIFICATION);
        intentNext.putExtra(Constants.DATA_INT, REQUEST_NEXT);
        PendingIntent actionNext = PendingIntent.getBroadcast(context, REQUEST_NEXT, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_play_next, actionNext);
    }

    private BroadcastReceiver notifyActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data) {
            if (TextUtils.equals(data.getAction(), Constants.RECEIVER_AUDIO_NOTIFICATION)) {
                int action = data.getIntExtra(Constants.DATA_INT, 0);
                if (action == REQUEST_PAUSE) {
                    MediaCoreSDK.get().changeState(AudioPlayState.PAUSED);
                } else if (action == REQUEST_PLAY) {
                    MediaCoreSDK.get().changeState(AudioPlayState.IN_PROGRESS);
                } else if (action == REQUEST_NEXT) {
                    MediaCoreSDK.get().changeState(AudioPlayState.COMPLETED);
                }
            }
        }
    };

    public void cancel() {
        context.unregisterReceiver(notifyActionReceiver);
        if (remoteViews != null) {
            manager.cancel(100);
        }
    }
}
