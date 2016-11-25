package com.zhouyou.music.module.views;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhouyou.music.R;
import com.zhouyou.music.module.AudioDetailActivity;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.state.AudioPlayState;
import com.zhouyou.music.media.MusicPlaySDK;

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

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_audio_play_panel, this);
        tvAudioTitle = (TextView) view.findViewById(R.id.tv_audio_title);
        tvAudioArtist = (TextView) view.findViewById(R.id.tv_audio_artist);
        ivPlayNow = (ImageView) view.findViewById(R.id.iv_play_now);
        ivPlayNow.setOnClickListener(this);
        view.findViewById(R.id.rl_playing_panel).setOnClickListener(this);
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
            case AudioPlayState.PLAYING:
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_playing_panel:
                Intent intent = new Intent(context, AudioDetailActivity.class);
                context.startActivity(intent);
                break;
            case R.id.iv_play_now:
                int state = MusicPlaySDK.get().getCurrState();
                if (state == AudioPlayState.PAUSED) {
                    MusicPlaySDK.get().changeState(AudioPlayState.PLAYING);
                } else if (state == AudioPlayState.PLAYING) {
                    MusicPlaySDK.get().changeState(AudioPlayState.PAUSED);
                } else {
                    Audio audio = MusicPlaySDK.get().getCurrAudio();
                    MusicPlaySDK.get().prepare(audio);
                }
                break;
            case R.id.iv_play_next:
                MusicPlaySDK.get().changeState(AudioPlayState.COMPLETED);
                break;
            default:
                break;
        }
    }
}
