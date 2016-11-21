package com.zhouyou.music;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.music.adapter.AudioAdapter;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.MusicPlaySDK;
import com.zhouyou.music.service.AudioMediaService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;

    private PlayingPanel playingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        playingPanel = (PlayingPanel) findViewById(R.id.playing_panel);
        listView.setOnItemClickListener(this);
        List<Audio> data = MusicPlaySDK.get().getAudioList();
        AudioAdapter adapter = new AudioAdapter(this, data);
        listView.setAdapter(adapter);

        Audio audio = ListUtils.getElement(data, 0);
        playingPanel.updateAudio(audio, false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Audio audio = (Audio) parent.getItemAtPosition(position);
        boolean b = MusicPlaySDK.get().prepare(audio);
        if (b) playingPanel.updateAudio(audio, true);
    }
}
