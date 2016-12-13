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
import com.zhouyou.music.media.AudioManagerFactory;
import com.zhouyou.music.media.MediaCoreSDK;
import com.zhouyou.music.media.state.AudioPlayState;
import com.zhouyou.music.media.state.IAudioStateSubscriber;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.music.module.views.AlbumImageView;

/**
 * 作者：ZhouYou
 * 日期：2016/12/7.
 */
public class AudioPlayFragment extends BaseFragment implements IAudioStateSubscriber {

    public static AudioPlayFragment getInstance(Bundle data) {
        AudioPlayFragment f = new AudioPlayFragment();
        f.setArguments(data);
        return f;
    }

    private TextView tvAudioTitle;
    private TextView tvAudioArtist;
    private AlbumImageView ivAlbum;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AudioManagerFactory.get().createAudioStatePublisher().register(this);
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
        onUpdateChange(MediaCoreSDK.get().getCurrAudio(), MediaCoreSDK.get().getCurrState());
    }

    @Override
    public void onPause() {
        super.onPause();
        ivAlbum.setSpanning(false);
    }

    @Override
    public void onUpdateChange(Audio audio, int state) {
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
            bm = MediaUtils.getAlbumCoverImage(activity, audio.id, audio.albumId);
        }
        ivAlbum.setBitmap(bm);
        ivAlbum.setSpanning(state == AudioPlayState.IN_PROGRESS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AudioManagerFactory.get().createAudioStatePublisher().unregister(this);
    }
}