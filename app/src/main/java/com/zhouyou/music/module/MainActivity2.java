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
import com.zhouyou.music.module.adapter.AudioAdapter;
import com.zhouyou.music.module.views.AudioPlayPanel;
import com.zhouyou.music.module.views.AudioPlayPanel2;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicServiceSDK;
import com.zhouyou.remote.client.observer.IMusicStateSubscriber;
import com.zhouyou.remote.client.observer.MusicManager;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class MainActivity2 extends BaseActivity implements AdapterView.OnItemClickListener,
        IMusicStateSubscriber {

    private AudioPlayPanel2 playPanel;
    private ClientCoreSDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (!isApplyingPermissions()) {
            sdk = ClientCoreSDK.get();
            MusicManager.get().createAudioStatePublisher().register(this);
            initViews();
        }
    }

    private void initViews() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        playPanel = (AudioPlayPanel2) findViewById(R.id.play_panel);
        listView.setOnItemClickListener(this);
        List<Audio> data = sdk.getPlayList();
        AudioAdapter adapter = new AudioAdapter(this, data);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onUpdateChange();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Audio audio = (Audio) parent.getItemAtPosition(position);
        if (audio == null) return;
        if (sdk.getCurrAudio() == null) {
            MusicServiceSDK.get().play(audio.id, audio.path, 0);
        } else {
            if (audio.id == MusicServiceSDK.get().getMusicId() && MusicServiceSDK.get().getState() == State.IN_PROGRESS) {
                T.ss("正在播放");
//                playPanel.viewDetail();
            } else {
                MusicServiceSDK.get().play(audio.id, audio.path, 0);
            }
        }
    }

    @Override
    public void onUpdateChange() {
        int currState = MusicServiceSDK.get().getState();
        Audio audio;
        if (currState == State.IDLE || currState == State.PREPARED) {
            audio = ClientCoreSDK.get().getPlayingMusic();
        } else {
            audio = ClientCoreSDK.get().getCurrAudio();
        }
        playPanel.updateAudio(audio, currState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.get().createAudioStatePublisher().unregister(this);
    }
}
