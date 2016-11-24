package com.zhouyou.music.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.AudioPlayState;
import com.zhouyou.music.media.MusicPlaySDK;

/**
 * 作者：ZhouYou
 * 日期：2016/11/23.
 */
public class AudioDetailActivity extends BaseActivity {

    private TextView tvAudioTitle;
    private TextView tvAudioArtist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_detail);
        initViews();
    }

    private void initViews() {
        tvAudioTitle = (TextView) findViewById(R.id.tv_audio_title);
        tvAudioArtist = (TextView) findViewById(R.id.tv_audio_artist);
    }

    @Override
    protected void onAudioStateChanged(Audio audio, int state) {
        if (state == AudioPlayState.ERROR) {
            Toast.makeText(this, "音频文件出错", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(1, 2000);
        }
        if (audio != null) {
            tvAudioTitle.setText(audio.title);
            tvAudioArtist.setText(audio.artist);
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            MusicPlaySDK.get().playNext();
            return true;
        }
    });
}
