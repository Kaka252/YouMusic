package com.zhouyou.music.module;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.wonderkiln.blurkit.BlurKit;
import com.zhouyou.library.utils.T;
import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.base.BaseFragment;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.media.OnMusicPlayingActionListener;
import com.zhouyou.music.module.adapter.AudioDetailViewPagerAdapter;
import com.zhouyou.music.module.fragment.AudioPlayFragment2;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.music.module.views.AudioOperationPanel2;
import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicServiceSDK;
import com.zhouyou.remote.client.observer.IMusicProgressSubscriber;
import com.zhouyou.remote.client.observer.IMusicStateSubscriber;
import com.zhouyou.remote.client.observer.MusicManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class AudioDetailActivity2 extends BaseActivity implements IMusicStateSubscriber,
        IMusicProgressSubscriber,
        OnMusicPlayingActionListener {

    private ClientCoreSDK sdk;
    private ImageView ivBg;
    private AudioOperationPanel2 operationPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdk = ClientCoreSDK.get();
        MusicManager.get().createAudioStatePublisher().register(this);
        MusicManager.get().createProgressPublisher().register(this);
        setContentView(R.layout.activity_audio_detail2);
        initViews();
    }

    private void initViews() {
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        ivBg = (ImageView) findViewById(R.id.iv_bg);
        operationPanel = (AudioOperationPanel2) findViewById(R.id.operation_panel);
        operationPanel.setOnMusicPlayingActionListener(this);
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(AudioPlayFragment2.getInstance(null));
        AudioDetailViewPagerAdapter adapter = new AudioDetailViewPagerAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onUpdateChange();
        onProgressChange(sdk.getCurrentPlayingMusicPosition(), sdk.getCurrentPlayingMusicDuration());
    }

    /**
     * 通知状态改变
     */
    @Override
    public void onUpdateChange() {
        int currState = sdk.getCurrentPlayingMusicState();
        Audio audio;
        if (currState == State.PREPARED || currState == State.IDLE) {
            audio = sdk.getPlayingMusic();
            MediaUtils.clearCacheBitmap();
        } else {
            audio = sdk.getCurrAudio();
        }

        // 加载专辑图片
        Bitmap bm = MediaUtils.getCachedBitmap();
        if (bm == null) {
            bm = MediaUtils.getAlbumCoverImage(this, audio.id, audio.albumId);
        }
        if (bm != null) {
            bm = BlurKit.getInstance().blur(bm, 23);
        }
        ivBg.setImageBitmap(bm);
        operationPanel.updatePanel(currState);
    }

    @Override
    public void onProgressChange(int currentPosition, int duration) {
        operationPanel.updateProgress(currentPosition, duration);
    }

    @Override
    public void onMusicPlay(int playAction, int seekPosition) {
        Audio audio = sdk.getCurrAudio();
        if (audio == null) {
            T.ss("请选择歌曲进行播放");
            return;
        }
        if (sdk.hasPlayListInitiated()) {
            sdk.complete(playAction == 2);
        } else {
            sdk.playMusic(sdk.getPlayList(), audio.path, playAction);
        }
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
}
