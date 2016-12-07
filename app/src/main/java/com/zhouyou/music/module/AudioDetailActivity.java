package com.zhouyou.music.module;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.wonderkiln.blurkit.BlurKit;
import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.base.BaseFragment;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.AudioManagerFactory;
import com.zhouyou.music.media.state.AudioPlayState;
import com.zhouyou.music.media.state.IAudioProgressSubscriber;
import com.zhouyou.music.media.state.IAudioStateSubscriber;
import com.zhouyou.music.module.adapter.AudioDetailViewPagerAdapter;
import com.zhouyou.music.module.fragment.AudioPlayFragment;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.music.module.views.AudioOperationPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/23.
 */
public class AudioDetailActivity extends BaseActivity implements IAudioStateSubscriber,
        IAudioProgressSubscriber {

    private ImageView ivBg;
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
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        ivBg = (ImageView) findViewById(R.id.iv_bg);
        operationPanel = (AudioOperationPanel) findViewById(R.id.operation_panel);
        List<BaseFragment> fragments = new ArrayList<>();
        AudioPlayFragment playFragment = AudioPlayFragment.getInstance(null);
        fragments.add(playFragment);
        AudioDetailViewPagerAdapter adapter = new AudioDetailViewPagerAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);
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
        // 加载专辑图片
        if (state == AudioPlayState.PREPARED || state == AudioPlayState.IDLE) {
            MediaUtils.clearCacheBitmap();
        }
        Bitmap bm = MediaUtils.getCachedBitmap();
        if (bm == null) {
            bm = MediaUtils.getAlbumCoverImage(this, audio.id, audio.albumId);
        }
        if (bm != null) {
            bm = BlurKit.getInstance().blur(bm, 23);
        }
        ivBg.setImageBitmap(bm);
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
