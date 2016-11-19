package com.zhouyou.music;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhouyou.music.entity.Audio;

/**
 * 作者：ZhouYou
 * 日期：2016/11/19.
 * 底部正在播放的音乐
 */
public class PlayingPanel extends LinearLayout implements View.OnClickListener {

    private Context context;

    public PlayingPanel(Context context) {
        this(context, null);
    }

    public PlayingPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayingPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private TextView tvAudioTitle;
    private TextView tvAudioArtist;

    private ImageView ivPlayNow;
    private ImageView ivPlayNext;

    private boolean isPlaying = false;

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_playing_audio, this);
        tvAudioTitle = (TextView) view.findViewById(R.id.tv_audio_title);
        tvAudioArtist = (TextView) view.findViewById(R.id.tv_audio_artist);
        ivPlayNow = (ImageView) view.findViewById(R.id.iv_play_now);
        ivPlayNext = (ImageView) view.findViewById(R.id.iv_play_next);
        view.findViewById(R.id.rl_playing_panel).setOnClickListener(this);
        ivPlayNow.setOnClickListener(this);
        ivPlayNext.setOnClickListener(this);
    }

    /**
     * 更新音乐播放的状态
     */
    public void updateAudioPlayingStatus() {
        ivPlayNow.setImageResource(isPlaying ? R.mipmap.ic_pause : R.mipmap.ic_play);
    }

    /**
     * 更新音频信息
     *
     * @param audio
     */
    public void updateAudio(Audio audio, boolean play) {
        if (audio == null) return;
        tvAudioTitle.setText(audio.title);
        tvAudioArtist.setText(audio.artist);
        isPlaying = play;
        updateAudioPlayingStatus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_playing_panel:

                break;
            case R.id.iv_play_now:
                isPlaying = !isPlaying;
                updateAudioPlayingStatus();
                break;
            case R.id.iv_play_next:

                break;
            default:
                break;
        }
    }
}
