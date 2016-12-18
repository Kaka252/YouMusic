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
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.module.adapter.AudioDetailViewPagerAdapter;
import com.zhouyou.music.module.fragment.AudioPlayFragment2;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.music.module.views.AudioOperationPanel2;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicServiceSDK;
import com.zhouyou.remote.client.observer.IMusicStateSubscriber;
import com.zhouyou.remote.client.observer.MusicManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class AudioDetailActivity2 extends BaseActivity implements IMusicStateSubscriber {

    private ClientCoreSDK sdk;
    private ImageView ivBg;
    private AudioOperationPanel2 operationPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdk = ClientCoreSDK.get();
        MusicManager.get().createAudioStatePublisher().register(this);
        setContentView(R.layout.activity_audio_detail2);
        initViews();
    }

    private void initViews() {
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        ivBg = (ImageView) findViewById(R.id.iv_bg);
        operationPanel = (AudioOperationPanel2) findViewById(R.id.operation_panel);
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
    }

    /**
     * 通知状态改变
     */
    @Override
    public void onUpdateChange() {
        int currState = MusicServiceSDK.get().getState();
        Audio audio;
        if (currState == State.PREPARED || currState == State.IDLE) {
            MediaUtils.clearCacheBitmap();
            audio = sdk.getPlayingMusic();
        } else if (currState == State.COMPLETED) {
            boolean isPlayBack = sdk.isPlayBack();
            if (isPlayBack) {
                audio = sdk.getLast();
                sdk.setPlayBack(false);
            } else {
                audio = sdk.getNext();
            }
        } else if (currState == State.ERROR) {
            audio = sdk.getNext();
//            MusicServiceSDK.get().play(audio.id, audio.path, 0);
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
        operationPanel.updatePanel(audio, currState);
    }

//    /**
//     * 播放进度改变
//     *
//     * @param currentPosition 播放进度
//     * @param duration        音频时长
//     */
//    @Override
//    public void onProgressChange(int currentPosition, int duration) {
//        operationPanel.updateProgress(currentPosition, duration);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.get().createAudioStatePublisher().unregister(this);
    }
}
