package com.zhouyou.music;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.music.adapter.AudioAdapter;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.utils.MediaUtils;

import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView listView;

    private PlayingPanel playingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        playingPanel = (PlayingPanel) findViewById(R.id.playing_panel);
        listView.setOnItemClickListener(this);
        List<Audio> data = MediaUtils.get().getAudioList();
        AudioAdapter adapter = new AudioAdapter(this, data);
        listView.setAdapter(adapter);

        Audio audio = ListUtils.getElement(data, 0);
        playingPanel.updateAudio(audio, false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Audio audio = (Audio) parent.getItemAtPosition(position);
        if (audio != null) {
            playingPanel.updateAudio(audio, true);
        }
    }
}
