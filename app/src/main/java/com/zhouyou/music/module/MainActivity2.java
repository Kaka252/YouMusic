package com.zhouyou.music.module;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhouyou.library.utils.T;
import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.media.OnMusicPlayingActionListener;
import com.zhouyou.music.module.adapter.AudioAdapter;
import com.zhouyou.music.module.views.AudioPlayPanel2;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.observer.IMusicProgressSubscriber;
import com.zhouyou.remote.client.observer.IMusicStateSubscriber;
import com.zhouyou.remote.client.observer.MusicManager;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class MainActivity2 extends BaseActivity implements AdapterView.OnItemClickListener,
        IMusicStateSubscriber,
        IMusicProgressSubscriber,
        OnMusicPlayingActionListener {

    private AudioPlayPanel2 playPanel;
    private ClientCoreSDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (!isApplyingPermissions()) {
            sdk = ClientCoreSDK.get();
            MusicManager.get().createAudioStatePublisher().register(this);
            MusicManager.get().createProgressPublisher().register(this);
            initViews();
        }
    }

    private void initViews() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        playPanel = (AudioPlayPanel2) findViewById(R.id.play_panel);
        playPanel.setOnMusicPlayingActionListener(this);
        listView.setOnItemClickListener(this);
        List<Audio> data = sdk.getPlayList();
        AudioAdapter adapter = new AudioAdapter(this, data);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onUpdateChange();
        onProgressChange(sdk.getCurrentPlayingMusicPosition(), sdk.getCurrentPlayingMusicDuration());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Audio audio = (Audio) parent.getItemAtPosition(position);
        if (audio == null) return;
        if (sdk.getCurrAudio() == null) {
            sdk.playMusic(sdk.getPlayList(), audio.path);
        } else {
            if (sdk.isPlayingCurrentMusic(audio.path) && sdk.isMusicPlaying()) {
                playPanel.viewDetail();
            } else {
                sdk.playMusic(sdk.getPlayList(), audio.path);
            }
        }
    }

    @Override
    public void onUpdateChange() {
        int currState = sdk.getCurrentPlayingMusicState();
        Audio audio;
        if (currState == State.PREPARED || currState == State.IDLE) {
            audio = sdk.getPlayingMusic();
        } else {
            audio = sdk.getCurrAudio();
        }
        playPanel.updateAudio(audio, currState);
    }


    @Override
    public void onProgressChange(int currentPosition, int duration) {
        playPanel.updateProgress(currentPosition, duration);
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
            sdk.playMusic(sdk.getPlayList(), audio.path, playAction, seekPosition);
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
