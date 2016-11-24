package com.zhouyou.music.module.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhouyou.music.R;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.AudioPlayState;
import com.zhouyou.music.media.MusicPlaySDK;
import com.zhouyou.music.module.utils.StringUtils;

/**
 * 作者：ZhouYou
 * 日期：2016/11/24.
 * 音乐播放的操作面板
 */
public class AudioOperationPanel extends LinearLayout {

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

    private void init() {
        View view = inflater.inflate(R.layout.view_audio_operation_panel, this);
        tvStartTime = (TextView) view.findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
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

        }
    };

    /**
     * 更新操作板信息
     *
     * @param audio
     * @param state
     */
    public void updatePanel(Audio audio, int state) {
        if (state == AudioPlayState.ERROR) {
            Toast.makeText(context, "音频文件出错", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(1, 2000);
        }
        if (audio != null) {
            tvStartTime.setText(StringUtils.formatTime(0));
            tvEndTime.setText(StringUtils.formatTime(audio.duration));
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MusicPlaySDK.get().playNext();
                    break;
                default:
                    break;
            }
            return true;
        }
    });

}
