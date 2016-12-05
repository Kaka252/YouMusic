package com.zhouyou.music.module;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.AudioManagerFactory;
import com.zhouyou.music.media.state.AudioPlayState;
import com.zhouyou.music.media.state.IAudioProgressSubscriber;
import com.zhouyou.music.media.state.IAudioStateSubscriber;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.music.module.views.AlbumImageView;
import com.zhouyou.music.module.views.AudioOperationPanel;

/**
 * 作者：ZhouYou
 * 日期：2016/11/23.
 */
public class AudioDetailActivity extends BaseActivity implements IAudioStateSubscriber,
        IAudioProgressSubscriber {

    private TextView tvAudioTitle;
    private TextView tvAudioArtist;
    private AlbumImageView ivAlbum;

    private AudioOperationPanel operationPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AudioManagerFactory.get().createAudioStatePublisher().register(this);
        AudioManagerFactory.get().createProgressPublisher().register(this);
        setContentView(R.layout.activity_audio_detail);
        initViews();
    }

    private void initViews() {
        tvAudioTitle = (TextView) findViewById(R.id.tv_audio_title);
        tvAudioArtist = (TextView) findViewById(R.id.tv_audio_artist);
        ivAlbum = (AlbumImageView) findViewById(R.id.iv_album);
        ivAlbum.setCircle(true);
        operationPanel = (AudioOperationPanel) findViewById(R.id.operation_panel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onUpdateChange(sdk.getCurrAudio(), sdk.getCurrState());
        onProgressChange(sdk.getCurrentAudioProgress(), sdk.getCurrentAudioDuration());
    }

    /**
     * 通知状态改变
     *
     * @param audio 音频
     * @param state 状态
     */
    @Override
    public void onUpdateChange(Audio audio, int state) {
        operationPanel.updatePanel(audio, state);
        if (audio == null) return;
        tvAudioTitle.setText(audio.title);
        tvAudioArtist.setText(audio.artist);
        if (state == AudioPlayState.PREPARED || state == AudioPlayState.IDLE) {
            MediaUtils.clearCacheBitmap();
            ivAlbum.initSpanningDegree();
        }
        // 加载专辑图片
        Bitmap bm = MediaUtils.getCachedBitmap();
        if (bm == null) {
            bm = MediaUtils.getAlbumCoverImage(this, audio.id, audio.albumId);
        }
        ivAlbum.setBitmap(bm);
        ivAlbum.setSpanning(state == AudioPlayState.IN_PROGRESS);
    }

    /**
     * 播放进度改变
     *
     * @param currentPosition 播放进度
     * @param duration        音频时长
     */
    @Override
    public void onProgressChange(int currentPosition, int duration) {
        operationPanel.updateProgress(currentPosition, duration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioManagerFactory.get().createAudioStatePublisher().unregister(this);
        AudioManagerFactory.get().createProgressPublisher().unregister(this);
    }
}
