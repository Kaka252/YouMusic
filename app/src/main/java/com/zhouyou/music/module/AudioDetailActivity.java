package com.zhouyou.music.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.AudioManagerFactory;
import com.zhouyou.music.media.state.IAudioStateSubscriber;
import com.zhouyou.music.module.views.AudioOperationPanel;

/**
 * 作者：ZhouYou
 * 日期：2016/11/23.
 */
public class AudioDetailActivity extends BaseActivity implements IAudioStateSubscriber {

    private TextView tvAudioTitle;
    private TextView tvAudioArtist;

    private AudioOperationPanel operationPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AudioManagerFactory.get().createAudioStateManager().register(this);
        setContentView(R.layout.activity_audio_detail);
        initViews();
    }

    private void initViews() {
        tvAudioTitle = (TextView) findViewById(R.id.tv_audio_title);
        tvAudioArtist = (TextView) findViewById(R.id.tv_audio_artist);
        operationPanel = (AudioOperationPanel) findViewById(R.id.operation_panel);
    }

    @Override
    protected void onAudioStateChanged(Audio audio, int state) {
        onUpdateChange(audio, state);
    }

    /**
     * 通知状态改变
     *
     * @param audio 音频
     * @param state 状态
     */
    @Override
    public void onUpdateChange(Audio audio, int state) {
        if (audio != null) {
            tvAudioTitle.setText(audio.title);
            tvAudioArtist.setText(audio.artist);
        }
        operationPanel.updatePanel(audio, state);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioManagerFactory.get().createAudioStateManager().unregister(this);
    }
}
