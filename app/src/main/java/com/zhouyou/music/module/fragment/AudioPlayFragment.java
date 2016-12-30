package com.zhouyou.music.module.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhouyou.library.utils.PoolUtils;
import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseFragment;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.media.MusicLoadTask;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.music.module.views.AlbumImageView;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.observer.IMusicStateSubscriber;
import com.zhouyou.remote.client.observer.MusicManager;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class AudioPlayFragment extends BaseFragment implements IMusicStateSubscriber {

    public static AudioPlayFragment getInstance(Bundle data) {
        AudioPlayFragment f = new AudioPlayFragment();
        f.setArguments(data);
        return f;
    }

    private AlbumImageView ivAlbum;
    private ClientCoreSDK sdk;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicManager.get().createAudioStatePublisher().register(this);
        sdk = ClientCoreSDK.get();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio_play, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivAlbum = (AlbumImageView) view.findViewById(R.id.iv_album);
        ivAlbum.setCircle(true);
//        ViewCompat.setTransitionName(ivAlbum, "album");
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAudioInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
        ivAlbum.setSpanning(false);
    }

    @Override
    public void onUpdateChange() {
        int currState = sdk.getCurrentPlayingMusicState();
        if (currState == State.PREPARED || currState == State.IDLE) {
            ivAlbum.initSpanningDegree();
        }
        loadAudioInfo();
    }

    /**
     * 加载歌曲信息
     */
    private void loadAudioInfo() {
        MusicLoadTask task = new MusicLoadTask();
        task.setOnMusicLoadingListener(new MusicLoadTask.OnMusicLoadingListener() {
            @Override
            public void setupMusic(Audio audio, Bitmap bm) {
                ivAlbum.setBitmap(bm);
                ivAlbum.setSpanning(sdk.isMusicPlaying());
            }
        });
        task.loadMusic();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicManager.get().createAudioStatePublisher().unregister(this);
    }
}
