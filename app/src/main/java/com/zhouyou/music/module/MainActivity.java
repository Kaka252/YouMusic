package com.zhouyou.music.module;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhouyou.library.utils.T;
import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.entity.AudioLocalDataManager;
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.media.OnMusicPlayingActionListener;
import com.zhouyou.music.module.adapter.AudioAdapter;
import com.zhouyou.music.module.views.AudioPlayPanel;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.observer.IMusicProgressSubscriber;
import com.zhouyou.remote.client.observer.IMusicStateSubscriber;
import com.zhouyou.remote.client.observer.MusicManager;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener,
        IMusicStateSubscriber,
        IMusicProgressSubscriber,
        OnMusicPlayingActionListener {

    private AudioPlayPanel playPanel;
    private ClientCoreSDK sdk;
    private List<Audio> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isApplyingPermissions()) {
            sdk = ClientCoreSDK.get();
            MusicManager.get().createAudioStatePublisher().register(this);
            MusicManager.get().createProgressPublisher().register(this);
            initViews();
        }
    }

    private void initViews() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        playPanel = (AudioPlayPanel) findViewById(R.id.play_panel);
        playPanel.setOnMusicPlayingActionListener(this);
        listView.setOnItemClickListener(this);
        data = AudioLocalDataManager.get().getLocalAudioList();
        AudioAdapter adapter = new AudioAdapter(this, data);
        listView.setAdapter(adapter);
        findViewById(R.id.iv_search).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playPanel.updateAudioPlayingStatus(sdk.getCurrentPlayingMusicState());
        playPanel.loadAudioInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                T.ss("功能未实现");
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Audio audio = (Audio) parent.getItemAtPosition(position);
        if (audio == null || TextUtils.isEmpty(audio.path)) return;
        if (sdk.getPlayingMusic() == null || !sdk.isPlayingCurrentMusic(audio.path)) {
            sdk.playMusic(audio.path);
            sdk.savePlayList(data);
        } else {
            if (sdk.isMusicPlaying()) {
                playPanel.viewDetail();
            } else {
                sdk.resume(sdk.getCurrentPlayingMusicPosition());
            }
        }
    }

    @Override
    public void onUpdateChange(int state) {
        playPanel.updateAudioPlayingStatus(state);
        if (state == State.PREPARED || state == State.IDLE) {
            playPanel.loadAudioInfo();
        }
    }


    @Override
    public void onProgressChange(int currentPosition, int duration) {
        playPanel.updateProgress(currentPosition, duration);
    }

    @Override
    public void onMusicPlay(int playAction, int seekPosition) {
        Audio audio = null;
        if (playAction == 0) {
            audio = sdk.getPlayingMusic();
        } else if (playAction == 1) {
            audio = sdk.getNextOne();
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
}
