package com.zhouyou.music.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhouyou.music.PlayingPanel;
import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.Audio;

/**
 * 作者：ZhouYou
 * 日期：2016/11/23.
 */
public class AudioDetailActivity extends BaseActivity {

    private PlayingPanel playingPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_detail);
        playingPanel = (PlayingPanel) findViewById(R.id.playing_panel);
    }

    @Override
    protected void onAudioStateChanged(Audio audio, int state) {
        playingPanel.updateAudio(audio, state);
    }
}
