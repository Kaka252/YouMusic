package com.zhouyou.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.music.adapter.AudioAdapter;
import com.zhouyou.music.config.Constants;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.AudioPlayState;
import com.zhouyou.music.media.MusicPlaySDK;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;

    private PlayingPanel playingPanel;

    private MusicPlaySDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReceiver();
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        playingPanel = (PlayingPanel) findViewById(R.id.playing_panel);
        listView.setOnItemClickListener(this);
        sdk = MusicPlaySDK.get();
        List<Audio> data = sdk.getAudioList();
        AudioAdapter adapter = new AudioAdapter(this, data);
        listView.setAdapter(adapter);

        Audio audio = ListUtils.getElement(data, 0);
        playingPanel.updateAudio(audio, false);
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.RECEIVER_AUDIO_STATE_CHANGE);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Audio audio = (Audio) parent.getItemAtPosition(position);
        if (audio == null) return;
        if (sdk.getCurrAudio() == null) {
            sdk.prepare(audio);
        } else {
            if (audio.id == sdk.getCurrAudio().id && sdk.getCurrState() == AudioPlayState.STARTED) {
                Toast.makeText(this, "正在播放", Toast.LENGTH_SHORT).show();
            } else {
                sdk.prepare(audio);
            }
        }
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!TextUtils.equals(intent.getAction(), Constants.RECEIVER_AUDIO_STATE_CHANGE)) return;
            int state = intent.getIntExtra(Constants.DATA_INT, 0);
            Audio audio = intent.getParcelableExtra(Constants.DATA_ENTITY);
            if (state == AudioPlayState.STARTED) {
                playingPanel.updateAudio(audio, true);
            } else if (state == AudioPlayState.PAUSED) {
                playingPanel.updateAudio(audio, false);
            }
        }
    };
}
