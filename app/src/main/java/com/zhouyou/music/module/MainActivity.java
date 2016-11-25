package com.zhouyou.music.module;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zhouyou.music.R;
import com.zhouyou.music.adapter.AudioAdapter;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.AudioManagerFactory;
import com.zhouyou.music.media.state.AudioPlayState;
import com.zhouyou.music.media.state.IAudioStateSubscriber;
import com.zhouyou.music.module.views.AudioPlayPanel;

import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, IAudioStateSubscriber {

    private ListView listView;

    private AudioPlayPanel playingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AudioManagerFactory.get().createAudioStateManager().register(this);
        initViews();
    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.list_view);
        playingPanel = (AudioPlayPanel) findViewById(R.id.playing_panel);
        listView.setOnItemClickListener(this);
        List<Audio> data = sdk.getPlayList();
        AudioAdapter adapter = new AudioAdapter(this, data);
        listView.setAdapter(adapter);
    }

    /**
     * 监听音频播放的状态改变
     *
     * @param audio 音频
     * @param state 状态
     */
    @Override
    protected void onAudioStateChanged(Audio audio, int state) {
        onUpdateChange(audio, state);
    }

    /**
     * 通知状态改变
     *
     * @param audio 音频
     * @param state 状态
     */
    @Override
    public void onUpdateChange(Audio audio, int state) {
        playingPanel.updateAudio(audio, state);
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

    /**
     * 按下返回键回到后台
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioManagerFactory.get().createAudioStateManager().unregister(this);
    }
}
