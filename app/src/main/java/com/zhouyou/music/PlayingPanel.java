package com.zhouyou.music;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.AudioPlayState;
import com.zhouyou.music.media.MusicPlaySDK;
import com.zhouyou.music.service.AudioMediaService;

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

    /*音乐名*/
    private TextView tvAudioTitle;
    /*艺术家*/
    private TextView tvAudioArtist;
    /*播放/暂停*/
    private ImageView ivPlayNow;

    private Audio audio;

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_playing_audio, this);
        tvAudioTitle = (TextView) view.findViewById(R.id.tv_audio_title);
        tvAudioArtist = (TextView) view.findViewById(R.id.tv_audio_artist);
        ivPlayNow = (ImageView) view.findViewById(R.id.iv_play_now);
        view.findViewById(R.id.rl_playing_panel).setOnClickListener(this);
        ivPlayNow.setOnClickListener(this);
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
            case AudioPlayState.STARTED:
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
        if (audio == null) return;
        this.audio = audio;
        tvAudioTitle.setText(audio.title);
        tvAudioArtist.setText(audio.artist);
        updateAudioPlayingStatus(state);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_playing_panel:
                // TODO 查看音乐
                break;
            case R.id.iv_play_now:
                doPlayAction();
                break;
            case R.id.iv_play_next:
                // TODO 下一首
                break;
            default:
                break;
        }
    }

    private void doPlayAction() {
        int state = MusicPlaySDK.get().getCurrState();
        if (state == AudioPlayState.PAUSED) {
            MusicPlaySDK.get().play();
        } else if (state == AudioPlayState.STARTED) {
            MusicPlaySDK.get().pause();
        } else {
            MusicPlaySDK.get().prepare(audio);
        }
    }
}
