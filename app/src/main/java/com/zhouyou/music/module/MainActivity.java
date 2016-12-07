package com.zhouyou.music.module;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhouyou.library.utils.PermissionUtils;
import com.zhouyou.music.R;
import com.zhouyou.music.module.adapter.AudioAdapter;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.AudioManagerFactory;
import com.zhouyou.music.media.state.AudioPlayState;
import com.zhouyou.music.media.state.IAudioProgressSubscriber;
import com.zhouyou.music.media.state.IAudioStateSubscriber;
import com.zhouyou.music.module.views.AudioPlayPanel;
import com.zhouyou.music.net.GetAlbumListRequest;
import com.zhouyou.music.net.bean.MusicResultBean;
import com.zhouyou.music.net.bean.SongBean;
import com.zhouyou.music.net.response.GetAlbumListResponse;
import com.zhouyou.network.IRespCallback;
import com.zhouyou.network.NetCoreApi;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        IAudioStateSubscriber,
        IAudioProgressSubscriber {

    private static final int PERMISSION_REQUEST_CODE = 0x1;

    private AudioPlayPanel playPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String[] permissions = getPermissions();
//        if (!ListUtils.isEmpty(permissions)) {
//            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
//        } else {
//
//        }
        AudioManagerFactory.get().createAudioStatePublisher().register(this);
        AudioManagerFactory.get().createProgressPublisher().register(this);
        initViews();
        testRequest();
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
                Intent intent = new Intent(this, AudioDetailActivity.class);
                startActivity(intent);
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


    private void testRequest() {
        GetAlbumListRequest request = new GetAlbumListRequest(new IRespCallback<GetAlbumListResponse>() {
            @Override
            public void onSuccess(int what, GetAlbumListResponse resp) {
                if (resp == null) return;
                MusicResultBean bean = resp.getResult();
                if (bean == null) return;
                int count = bean.songCount;
                List<SongBean> list = bean.songs;
            }

            @Override
            public void onError(int what, GetAlbumListResponse error) {

            }
        });
        request.type = 1;
        request.limit = 10;
        request.s = "银魂";
        request.offset = 0;
        NetCoreApi.doGet(request);
    }

    private String[] getPermissions() {
        List<String> list = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // 读取扩展卡数据权限
            if (!PermissionUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        if (!list.isEmpty()) {
            return list.toArray(new String[list.size()]);
        }
        return null;
    }
}
