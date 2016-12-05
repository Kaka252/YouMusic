package com.zhouyou.music.module.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhouyou.library.utils.Scale;
import com.zhouyou.music.R;
import com.zhouyou.music.base.App;
import com.zhouyou.music.module.AudioDetailActivity;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.state.AudioPlayState;
import com.zhouyou.music.media.MediaCoreSDK;

/**
 * 作者：ZhouYou
 * 日期：2016/11/19.
 * 底部正在播放的音乐
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

    /*音乐名*/
    private TextView tvAudioTitle;
    /*艺术家*/
    private TextView tvAudioArtist;
    /*播放/暂停*/
    private ImageView ivPlayNow;
    /*播放进度*/
    private View viewProgress;

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_audio_play_panel, this);
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
    private void updateAudioPlayingStatus(int state) {
        switch (state) {
            case AudioPlayState.IDLE:
            case AudioPlayState.INITIALIZED:
            case AudioPlayState.PREPARED:
            case AudioPlayState.PREPARING:
            case AudioPlayState.PAUSED:
            case AudioPlayState.STOPPED:
            case AudioPlayState.COMPLETED:
            case AudioPlayState.END:
            case AudioPlayState.ERROR:
                ivPlayNow.setImageResource(R.mipmap.ic_play);
                break;
            case AudioPlayState.IN_PROGRESS:
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
        tvAudioTitle.setText(audio.title);
        tvAudioArtist.setText(audio.artist);
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
                Intent intent = new Intent(context, AudioDetailActivity.class);
                context.startActivity(intent);
                break;
            case R.id.iv_play_now:
                doPlayAction();
                break;
            case R.id.iv_play_next:
                MediaCoreSDK.get().changeState(AudioPlayState.COMPLETED);
                break;
            default:
                break;
        }
    }

    private void doPlayAction() {
        int state = MediaCoreSDK.get().getCurrState();
        if (state == AudioPlayState.PAUSED) {
            MediaCoreSDK.get().changeState(AudioPlayState.PREPARED);
        } else if (state == AudioPlayState.IN_PROGRESS) {
            MediaCoreSDK.get().changeState(AudioPlayState.PAUSED);
        } else {
            Audio audio = MediaCoreSDK.get().getCurrAudio();
            MediaCoreSDK.get().prepare(audio);
        }
    }
}
