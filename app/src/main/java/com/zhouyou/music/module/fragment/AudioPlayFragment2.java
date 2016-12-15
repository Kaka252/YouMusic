package com.zhouyou.music.module.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseFragment;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.music.module.views.AlbumImageView;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicServiceSDK;
import com.zhouyou.remote.client.observer.IMusicStateSubscriber;
import com.zhouyou.remote.client.observer.MusicManager;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class AudioPlayFragment2 extends BaseFragment implements IMusicStateSubscriber {

    public static AudioPlayFragment2 getInstance(Bundle data) {
        AudioPlayFragment2 f = new AudioPlayFragment2();
        f.setArguments(data);
        return f;
    }

    private TextView tvAudioTitle;
    private TextView tvAudioArtist;
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
        tvAudioTitle = (TextView) view.findViewById(R.id.tv_audio_title);
        tvAudioArtist = (TextView) view.findViewById(R.id.tv_audio_artist);
        ivAlbum = (AlbumImageView) view.findViewById(R.id.iv_album);
        ivAlbum.setCircle(true);
        ViewCompat.setTransitionName(ivAlbum, "album");
    }

    @Override
    public void onResume() {
        super.onResume();
        onUpdateChange();
    }

    @Override
    public void onPause() {
        super.onPause();
        ivAlbum.setSpanning(false);
    }

    @Override
    public void onUpdateChange() {
        Audio audio;
        int currState = MusicServiceSDK.get().getState();
        if (currState == State.PREPARED || currState == State.IDLE) {
            MediaUtils.clearCacheBitmap();
            ivAlbum.initSpanningDegree();
            audio = sdk.getPlayingMusic();
        } else {
            audio = sdk.getCurrAudio();
        }
        // 加载专辑图片
        Bitmap bm = MediaUtils.getCachedBitmap();
        if (bm == null) {
            bm = MediaUtils.getAlbumCoverImage(activity, audio.id, audio.albumId);
        }
        ivAlbum.setBitmap(bm);
        ivAlbum.setSpanning(currState == State.IN_PROGRESS);

        if (audio != null) {
            tvAudioTitle.setText(audio.title);
            tvAudioArtist.setText(audio.artist);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicManager.get().createAudioStatePublisher().unregister(this);
    }
}
