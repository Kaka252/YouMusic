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
import com.zhouyou.music.entity.AudioLocalDataManager;
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.media.Mode;
import com.zhouyou.music.media.OnMusicPlayingActionListener;
import com.zhouyou.music.module.utils.StringUtils;
import com.zhouyou.remote.State;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class AudioOperationPanel extends LinearLayout implements View.OnClickListener {

    private Context context;
    private LayoutInflater inflater;

    public AudioOperationPanel(Context context) {
        this(context, null);
    }

    public AudioOperationPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioOperationPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        inflater = LayoutInflater.from(context);
        init();
    }

    private TextView tvStartTime;
    private TextView tvEndTime;
    private SeekBar seekBar;
    private ImageView ivPlayNow;
    private ImageView ivLikeSwitch;
    private ImageView ivPlayModeSwitch;
    private OnMusicPlayingActionListener listener;

    private int seekPosition;
    private int duration;
    private Audio audio;

    public void setOnMusicPlayingActionListener(OnMusicPlayingActionListener listener) {
        this.listener = listener;
    }

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

        ivLikeSwitch = (ImageView) view.findViewById(R.id.iv_like_switch);
        ivLikeSwitch.setOnClickListener(this);
        ivPlayModeSwitch = (ImageView) view.findViewById(R.id.iv_play_mode_switch);
        ivPlayModeSwitch.setOnClickListener(this);

        initFavor();

        int mode = ClientCoreSDK.get().getPlayMode();
        switch (mode) {
            case Mode.MODE_CYCLE_ALL_PLAY:
                ivPlayModeSwitch.setImageResource(R.mipmap.ic_play_mode_cycle);
                break;
            case Mode.MODE_RANDOM_PLAY:
                ivPlayModeSwitch.setImageResource(R.mipmap.ic_play_mode_random);
                break;
            case Mode.MODE_SINGLE_PLAY:
                ivPlayModeSwitch.setImageResource(R.mipmap.ic_play_mode_single);
                break;
            default:
                break;
        }
    }

    private void initFavor() {
        audio = ClientCoreSDK.get().getPlayingMusic();
        if (audio != null) {
            boolean isFavor = audio.isFavor;
            ivLikeSwitch.setImageResource(isFavor ? R.mipmap.ic_like : R.mipmap.ic_dislike);
        }
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            float percent = seekBar.getProgress() * 1.0f / 100;
            seekPosition = (int) (percent * duration);
            if (ClientCoreSDK.get().isMusicPlaying()) {
                ClientCoreSDK.get().resume(seekPosition);
            } else {
                updateProgress(seekPosition, duration);
            }
        }
    };

    /**
     * 更新音乐播放的状态
     */
    private void updateAudioPlayingStatus(int state) {
        switch (state) {
            case State.IDLE:
                initFavor();
                break;
            case State.INITIALIZED:
            case State.PREPARING:
                break;
            case State.PREPARED:
                initFavor();
                break;
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
     */
    public void updatePanel(int currState) {
        updateAudioPlayingStatus(currState);
    }

    /**
     * 更新音频播放的起始时间
     *
     * @param currentPosition 进度
     * @param duration        时长
     */
    public void updateProgress(int currentPosition, int duration) {
        this.seekPosition = currentPosition;
        this.duration = duration;
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
        if (listener == null) return;
        switch (v.getId()) {
            case R.id.iv_play_back:
                listener.onMusicPlay(2, -1);
                break;
            case R.id.iv_play_next:
                listener.onMusicPlay(1, -1);
                break;
            case R.id.iv_play_now:
                int state = ClientCoreSDK.get().getCurrentPlayingMusicState();
                if (state == State.PAUSED) {
                    listener.onMusicResume(seekPosition);
                } else if (state == State.IN_PROGRESS) {
                    listener.onMusicPause();
                } else {
                    listener.onMusicPlay(0, seekPosition);
                }
                break;
            case R.id.iv_like_switch:
                switchFavor();
                break;
            case R.id.iv_play_mode_switch:
                switchMode();
                break;
            default:
                break;
        }
    }

    public void switchFavor() {
        if (audio == null) {
            T.ss("还没有选择歌曲哟~");
            return;
        }
        if (audio.isFavor) {
            T.ss("已从收藏列表中移除");
            ivLikeSwitch.setImageResource(R.mipmap.ic_dislike);
            audio.isFavor = false;
        } else {
            T.ss("已添加到收藏列表");
            ivLikeSwitch.setImageResource(R.mipmap.ic_like);
            audio.isFavor = true;
        }
        AudioLocalDataManager.get().update(audio);
    }

    public void switchMode() {
        int mode = ClientCoreSDK.get().getPlayMode();
        switch (mode) {
            case Mode.MODE_CYCLE_ALL_PLAY:
                ivPlayModeSwitch.setImageResource(R.mipmap.ic_play_mode_random);
                ClientCoreSDK.get().savePlayMode(Mode.MODE_RANDOM_PLAY);
                T.ss("随机播放");
                break;
            case Mode.MODE_RANDOM_PLAY:
                ivPlayModeSwitch.setImageResource(R.mipmap.ic_play_mode_single);
                ClientCoreSDK.get().savePlayMode(Mode.MODE_SINGLE_PLAY);
                T.ss("单曲循环");
                break;
            case Mode.MODE_SINGLE_PLAY:
                ivPlayModeSwitch.setImageResource(R.mipmap.ic_play_mode_cycle);
                ClientCoreSDK.get().savePlayMode(Mode.MODE_CYCLE_ALL_PLAY);
                T.ss("循环播放");
                break;
            default:
                break;
        }
    }
}
