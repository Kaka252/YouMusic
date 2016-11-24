package com.zhouyou.music.module.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhouyou.music.R;
import com.zhouyou.music.entity.Audio;

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
    }

    private void updatePanel(Audio audio) {

    }


}
