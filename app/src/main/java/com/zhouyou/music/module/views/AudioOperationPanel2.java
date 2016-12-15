package com.zhouyou.music.module.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhouyou.library.utils.T;
import com.zhouyou.music.R;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.media.MediaCoreSDK;
import com.zhouyou.music.media.state.AudioPlayState;
import com.zhouyou.music.module.utils.StringUtils;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicServiceSDK;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class AudioOperationPanel2 extends LinearLayout implements View.OnClickListener {

    private Context context;
    private LayoutInflater inflater;

    public AudioOperationPanel2(Context context) {
        this(context, null);
    }

    public AudioOperationPanel2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioOperationPanel2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        inflater = LayoutInflater.from(context);
        init();
    }

    private TextView tvStartTime;
    private TextView tvEndTime;
    private SeekBar seekBar;

    private ImageView ivPlayNow;

    private Audio audio;

    private void init() {
        View view = inflater.inflate(R.layout.view_audio_operation_panel, this);
        tvStartTime = (TextView) view.findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        view.findViewById(R.id.iv_play_back).setOnClickListener(this);
        view.findViewById(R.id.iv_play_next).setOnClickListener(this);
        ivPlayNow = (ImageView) view.findViewById(R.id.iv_play_now);
        ivPlayNow.setOnClickListener(this);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            MediaCoreSDK.get().setProgressControlledByUser(fromUser);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            MediaCoreSDK.get().setProgressControlledByUser(true);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            MediaCoreSDK.get().setProgressControlledByUser(false);
            MediaCoreSDK.get().updateProgress(seekBar.getProgress());
            updateProgress(MediaCoreSDK.get().getCurrentAudioProgress(), MediaCoreSDK.get().getCurrentAudioDuration());
        }
    };

    /**
     * 更新音乐播放的状态
     */
    private void updateAudioPlayingStatus(int state) {
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
                ivPlayNow.setImageResource(R.mipmap.ic_play_music);
                break;
            case State.IN_PROGRESS:
                ivPlayNow.setImageResource(R.mipmap.ic_pause_music);
                break;
            default:
                break;
        }
    }

    /**
     * 更新操作板信息
     *
     * @param audio
     * @param state
     */
    public void updatePanel(Audio audio, int state) {
        this.audio = audio;
        updateAudioPlayingStatus(state);
    }

    /**
     * 更新音频播放的起始时间
     *
     * @param currentPosition 进度
     * @param duration        时长
     */
    public void updateProgress(int currentPosition, int duration) {
        if (duration <= 0) {
            tvStartTime.setText(StringUtils.formatTime(0));
            tvEndTime.setText(StringUtils.formatTime(0));
            seekBar.setProgress(0);
            return;
        }
        float f = currentPosition * 1.0f / duration;
        int progress = (int) (f * 100);
        Log.d("progress", "updateProgress: " + progress);
        if (progress <= 0) {
            tvStartTime.setText(StringUtils.formatTime(0));
            tvEndTime.setText(StringUtils.formatTime(duration));
            seekBar.setProgress(0);
        } else if (progress >= 100) {
            tvStartTime.setText(StringUtils.formatTime(duration));
            tvEndTime.setText(StringUtils.formatTime(0));
            seekBar.setProgress(duration);
        } else {
            int hasNotPlayed = duration - currentPosition;
            tvStartTime.setText(StringUtils.formatTime(currentPosition));
            tvEndTime.setText(StringUtils.formatTime(hasNotPlayed));
            seekBar.setProgress(progress);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play_back:
                ClientCoreSDK.get().setPlayBack(true);
                MusicServiceSDK.get().complete();
                break;
            case R.id.iv_play_next:
                MusicServiceSDK.get().complete();
                break;
            case R.id.iv_play_now:
                doPlayAction();
                break;
            default:
                break;
        }
    }

    private void doPlayAction() {
        int state = MusicServiceSDK.get().getState();
        if (state == State.PAUSED) {
            MusicServiceSDK.get().resume();
        } else if (state == State.IN_PROGRESS) {
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
