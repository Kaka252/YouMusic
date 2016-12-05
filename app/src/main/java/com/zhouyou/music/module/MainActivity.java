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
import com.zhouyou.music.media.state.IAudioProgressSubscriber;
import com.zhouyou.music.media.state.IAudioStateSubscriber;
import com.zhouyou.music.module.views.AudioPlayPanel;

import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        IAudioStateSubscriber,
        IAudioProgressSubscriber {

    private AudioPlayPanel playPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AudioManagerFactory.get().createAudioStatePublisher().register(this);
        AudioManagerFactory.get().createProgressPublisher().register(this);
        initViews();
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
        onUpdateChange(sdk.getCurrAudio(), sdk.getCurrState());
        onProgressChange(sdk.getCurrentAudioProgress(), sdk.getCurrentAudioDuration());
    }

    /**
     * 通知状态改变
     *
     * @param audio 音频
     * @param state 状态
     */
    @Override
    public void onUpdateChange(Audio audio, int state) {
        playPanel.updateAudio(audio, state);
    }

    /**
     * 播放进度改变
     *
     * @param currentPosition 播放进度
     * @param duration        音频时长
     */
    @Override
    public void onProgressChange(int currentPosition, int duration) {
        playPanel.updateProgress(currentPosition, duration);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Audio audio = (Audio) parent.getItemAtPosition(position);
        if (audio == null) return;
        if (sdk.getCurrAudio() == null) {
            sdk.prepare(audio);
        } else {
            if (audio.id == sdk.getCurrAudio().id && sdk.getCurrState() == AudioPlayState.IN_PROGRESS) {
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
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioManagerFactory.get().createAudioStatePublisher().unregister(this);
        AudioManagerFactory.get().createProgressPublisher().unregister(this);
    }
}
