package com.zhouyou.music;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.music.adapter.AudioAdapter;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.AudioPlayState;
import com.zhouyou.music.media.MusicPlaySDK;
import com.zhouyou.music.media.OnAudioPlayCallback;
import com.zhouyou.music.service.AudioMediaService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnAudioPlayCallback {

    private ListView listView;

    private PlayingPanel playingPanel;

    private MusicPlaySDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        playingPanel = (PlayingPanel) findViewById(R.id.playing_panel);
        listView.setOnItemClickListener(this);
        sdk = MusicPlaySDK.get();
        sdk.setOnAudioPlayCallback(this);
        List<Audio> data = sdk.getAudioList();
        AudioAdapter adapter = new AudioAdapter(this, data);
        listView.setAdapter(adapter);

        Audio audio = ListUtils.getElement(data, 0);
        playingPanel.updateAudio(audio, false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Audio audio = (Audio) parent.getItemAtPosition(position);
        sdk.prepare(audio);
    }

    @Override
    public void onStateChanged(Audio audio, int state) {
        if (state == AudioPlayState.STARTED) {
            playingPanel.updateAudio(audio, true);
        }
    }
}
