package com.zhouyou.music.module.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhouyou.library.utils.PoolUtils;
import com.zhouyou.library.utils.Scale;
import com.zhouyou.music.R;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.media.MusicLoadTask;
import com.zhouyou.music.media.OnMusicPlayingActionListener;
import com.zhouyou.music.module.AudioDetailActivity;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.remote.State;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class AudioPlayPanel extends LinearLayout implements View.OnClickListener {

    private Context context;

    public AudioPlayPanel(Context context) {
        this(context, null);
    }

    public AudioPlayPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioPlayPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /*专辑图片*/
    private AlbumImageView ivAlbum;
    /*音乐标题*/
    private TextView tvAudioTitle;
    /*音乐人*/
    private TextView tvAudioArtist;
    /*播放/暂停*/
    private ImageView ivPlayNow;
    /*播放进度*/
    private View viewProgress;

    /*当前播放的状态*/
    private int currState;

    private OnMusicPlayingActionListener listener;

    public void setOnMusicPlayingActionListener(OnMusicPlayingActionListener listener) {
        this.listener = listener;
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_audio_play_panel, this);
        ivAlbum = (AlbumImageView) view.findViewById(R.id.iv_album);
        ivAlbum.setCircle(true);
        ivAlbum.setThumbnail(true);
        tvAudioTitle = (TextView) view.findViewById(R.id.tv_audio_title);
        tvAudioArtist = (TextView) view.findViewById(R.id.tv_audio_artist);
        viewProgress = view.findViewById(R.id.view_progress);
        ivPlayNow = (ImageView) view.findViewById(R.id.iv_play_now);
        ivPlayNow.setOnClickListener(this);
        view.findViewById(R.id.rl_play_panel).setOnClickListener(this);
        view.findViewById(R.id.iv_play_next).setOnClickListener(this);
    }

    /**
     * 更新音乐播放的状态
     */
    public void updateAudioPlayingStatus(int state) {
        currState = state;
        switch (state) {
            case State.IDLE:
            case State.INITIALIZED:
            case State.PREPARED:
            case State.PREPARING:
            case State.PAUSED:
            case State.STOPPED:
            case State.COMPLETED:
            case State.END:
            case State.ERROR:
                ivPlayNow.setImageResource(R.mipmap.ic_play);
                break;
            case State.IN_PROGRESS:
                ivPlayNow.setImageResource(R.mipmap.ic_pause);
                break;
            default:
                break;
        }
    }

    /**
     * 读取专辑图片
     */
    public synchronized void loadAudioInfo() {
        MusicLoadTask task = new MusicLoadTask();
        task.setOnMusicLoadingListener(new MusicLoadTask.OnMusicLoadingListener() {
            @Override
            public void setupMusic(Audio audio, Bitmap bm) {
                ivAlbum.setBitmap(bm);
                tvAudioTitle.setText(audio.title);
                tvAudioArtist.setText(audio.artist);
            }
        });
        task.loadMusic(MediaUtils.COMPRESS_LEVEL_SMALL);
    }

    /**
     * 更新歌曲进度
     *
     * @param currentPosition
     * @param duration
     */
    public void updateProgress(int currentPosition, int duration) {
        float f = currentPosition * 1.0f / duration;
        int progress = (int) (f * Scale.getDisplayWidth(context));
        int height = Scale.dp2px(context, 2);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewProgress.getLayoutParams();
        if (params == null) {
            params = new RelativeLayout.LayoutParams(progress, height);
        } else {
            params.width = progress;
            params.height = height;
        }
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        viewProgress.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_play_panel:
                viewDetail();
                break;
            case R.id.iv_play_now:
                if (listener == null) return;
                if (currState == State.PAUSED) {
                    listener.onMusicResume(-1);
                } else if (currState == State.IN_PROGRESS) {
                    listener.onMusicPause();
                } else {
                    listener.onMusicPlay(0, -1);
                }
                break;
            case R.id.iv_play_next:
                if (listener != null) listener.onMusicPlay(1, -1);
                break;
            default:
                break;
        }
    }

    public void viewDetail() {
        Intent intent = new Intent(context, AudioDetailActivity.class);
        context.startActivity(intent);
//        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, new Pair<View, String>(ivAlbum, "album"));
//        Intent intent = new Intent(context, AudioDetailActivity.class);
//        ActivityCompat.startActivity(context, intent, activityOptions.toBundle());
    }
}
