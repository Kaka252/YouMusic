package com.zhouyou.music.module.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.zhouyou.library.utils.Scale;
import com.zhouyou.library.utils.T;
import com.zhouyou.music.R;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.module.AudioDetailActivity;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicServiceSDK;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class AudioPlayPanel2 extends LinearLayout implements View.OnClickListener {

    private Context context;

    public AudioPlayPanel2(Context context) {
        this(context, null);
    }

    public AudioPlayPanel2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioPlayPanel2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /*专辑图片*/
    private AlbumImageView ivAlbum;
    /*音乐信息*/
    private TextView tvAudioInfo;
    /*播放/暂停*/
    private ImageView ivPlayNow;
    /*播放进度*/
    private View viewProgress;

    /*当前播放的音乐*/
    private Audio audio;
    /*当前播放的状态*/
    private int currState;

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_audio_play_panel, this);
        ivAlbum = (AlbumImageView) view.findViewById(R.id.iv_album);
        ivAlbum.setCircle(true);
        ivAlbum.setThumbnail(true);
        tvAudioInfo = (TextView) view.findViewById(R.id.tv_audio_info);
        viewProgress = view.findViewById(R.id.view_progress);
        ivPlayNow = (ImageView) view.findViewById(R.id.iv_play_now);
        ivPlayNow.setOnClickListener(this);
        view.findViewById(R.id.rl_play_panel).setOnClickListener(this);
        view.findViewById(R.id.iv_play_next).setOnClickListener(this);
    }

    /**
     * 更新音乐播放的状态
     */
    private void updateAudioPlayingStatus(int state) {
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
     * 更新音频信息
     *
     * @param audio
     */
    public void updateAudio(Audio audio, int state) {
        updateAudioPlayingStatus(state);
        if (audio == null) return;
        this.audio = audio;
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(audio.title)) sb.append(audio.title).append("\n");
        if (!TextUtils.isEmpty(audio.artist)) sb.append(audio.artist);
        tvAudioInfo.setText(sb.toString());
        if (state == State.PREPARED || state == State.IDLE) {
            MediaUtils.clearCacheBitmap();
        }
        // 加载专辑图片
        Bitmap bm = MediaUtils.getCachedBitmap();
        if (bm == null) {
            bm = MediaUtils.getAlbumCoverImage(context, audio.id, audio.albumId, true);
        }
        ivAlbum.setBitmap(bm);
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
                doPlayAction();
                break;
            case R.id.iv_play_next:
                MusicServiceSDK.get().complete();
                break;
            default:
                break;
        }
    }

    public void viewDetail() {
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, new Pair<View, String>(ivAlbum, "album"));
        Intent intent = new Intent(context, AudioDetailActivity.class);
        ActivityCompat.startActivity(context, intent, activityOptions.toBundle());
    }

    private void doPlayAction() {
        if (currState == State.PAUSED) {
            MusicServiceSDK.get().resume();
        } else if (currState == State.IN_PROGRESS) {
            MusicServiceSDK.get().pause();
        } else {
            if (audio == null) {
                T.ss("请选择歌曲进行播放");
            } else {
                MusicServiceSDK.get().play(audio.id, audio.path, 0);
            }
        }
    }
}
