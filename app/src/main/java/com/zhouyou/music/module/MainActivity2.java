package com.zhouyou.music.module;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhouyou.library.utils.T;
import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.MediaCoreSDK;
import com.zhouyou.music.module.adapter.AudioAdapter;
import com.zhouyou.music.module.views.AudioPlayPanel;
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

    private AudioPlayPanel playPanel;
    private MediaCoreSDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isApplyingPermissions()) {
            sdk = MediaCoreSDK.get();
            MusicManager.get().createAudioStatePublisher().register(this);
            initViews();
        }
    }

    private void initViews() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        playPanel = (AudioPlayPanel) findViewById(R.id.play_panel);
        listView.setOnItemClickListener(this);
        List<Audio> data = sdk.getPlayList();
        AudioAdapter adapter = new AudioAdapter(this, data);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onUpdateChange(MusicServiceSDK.get().getState());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Audio audio = (Audio) parent.getItemAtPosition(position);
        if (audio == null) return;
        if (sdk.getCurrAudio() == null) {
            MusicServiceSDK.get().play(audio.path, 0);
        } else {
            if (audio.id == sdk.getCurrAudio().id && MusicServiceSDK.get().getState() == State.IN_PROGRESS) {
                T.ss("正在播放");
//                playPanel.viewDetail();
            } else {
                MusicServiceSDK.get().play(audio.path, 0);
            }
        }
    }

    @Override
    public void onUpdateChange(int state) {
//        playPanel.updateAudio(audio, state);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.get().createAudioStatePublisher().unregister(this);
    }
}
