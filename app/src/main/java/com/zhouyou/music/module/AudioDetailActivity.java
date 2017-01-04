package com.zhouyou.music.module;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhouyou.library.utils.T;
import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.base.BaseFragment;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.media.MusicLoadTask;
import com.zhouyou.music.media.OnMusicPlayingActionListener;
import com.zhouyou.music.module.adapter.AudioDetailViewPagerAdapter;
import com.zhouyou.music.module.fragment.AudioPlayFragment;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.music.module.views.AudioOperationPanel;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.observer.IMusicProgressSubscriber;
import com.zhouyou.remote.client.observer.IMusicStateSubscriber;
import com.zhouyou.remote.client.observer.MusicManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class AudioDetailActivity extends BaseActivity implements IMusicStateSubscriber,
        IMusicProgressSubscriber,
        OnMusicPlayingActionListener,
        View.OnClickListener {

    private ClientCoreSDK sdk;
    private ImageView ivBg;
    private AudioOperationPanel operationPanel;
    private TextView tvAudioTitle;
    private TextView tvAudioArtist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdk = ClientCoreSDK.get();
        MusicManager.get().createAudioStatePublisher().register(this);
        MusicManager.get().createProgressPublisher().register(this);
        setContentView(R.layout.activity_audio_detail);
        initViews();
    }

    private void initViews() {
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        ivBg = (ImageView) findViewById(R.id.iv_bg);
        tvAudioTitle = (TextView) findViewById(R.id.tv_audio_title);
        tvAudioArtist = (TextView) findViewById(R.id.tv_audio_artist);
        operationPanel = (AudioOperationPanel) findViewById(R.id.operation_panel);
        operationPanel.setOnMusicPlayingActionListener(this);
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(AudioPlayFragment.getInstance(null));
        AudioDetailViewPagerAdapter adapter = new AudioDetailViewPagerAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int currState = sdk.getCurrentPlayingMusicState();
        operationPanel.updatePanel(currState);
        loadAlbumImage();
        onProgressChange(sdk.getCurrentPlayingMusicPosition(), sdk.getCurrentPlayingMusicDuration());
    }

    /**
     * 读取专辑图片
     */
    private synchronized void loadAlbumImage() {
        MusicLoadTask task = new MusicLoadTask();
        task.setOnMusicLoadingListener(new MusicLoadTask.OnMusicLoadingListener() {
            @Override
            public void setupMusic(Audio audio, Bitmap bm) {
                ivBg.setImageBitmap(bm);
                tvAudioTitle.setText(audio.title);
                tvAudioArtist.setText(audio.artist);
            }
        });
        task.loadMusic(MediaUtils.COMPRESS_LEVEL_SMALL, true);
    }

    /**
     * 通知状态改变
     */
    @Override
    public void onUpdateChange(int state) {
        operationPanel.updatePanel(state);
        if (state == State.PREPARED || state == State.IDLE) {
            loadAlbumImage();
        }
    }

    @Override
    public void onProgressChange(int currentPosition, int duration) {
        operationPanel.updateProgress(currentPosition, duration);
    }

    @Override
    public void onMusicPlay(int playAction, int seekPosition) {
        Audio audio = null;
        if (playAction == 0) {
            audio = sdk.getPlayingMusic();
        } else if (playAction == 1) {
            audio = sdk.getNextOne();
        } else if (playAction == 2) {
            audio = sdk.getLastOne();
        }
        if (audio == null) {
            T.ss("请选择歌曲进行播放");
            return;
        }
        sdk.playMusic(audio.path, seekPosition);
    }

    @Override
    public void onMusicPause() {
        sdk.pause();
    }

    @Override
    public void onMusicResume(int seekPosition) {
        sdk.resume(seekPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.get().createAudioStatePublisher().unregister(this);
        MusicManager.get().createProgressPublisher().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
