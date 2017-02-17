package com.zhouyou.music.module;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhouyou.library.utils.Jump;
import com.zhouyou.library.utils.T;
import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.entity.AudioLocalDataManager;
import com.zhouyou.music.media.ClientCoreSDK;
import com.zhouyou.music.media.MusicLoadTask;
import com.zhouyou.music.media.OnMusicPlayingActionListener;
import com.zhouyou.music.module.adapter.AudioAdapter;
import com.zhouyou.music.module.utils.MediaUtils;
import com.zhouyou.music.module.views.AudioPlayPanel;
import com.zhouyou.music.notification.NotificationReceiver;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.observer.IMusicProgressSubscriber;
import com.zhouyou.remote.client.observer.IMusicStateSubscriber;
import com.zhouyou.remote.client.observer.MusicManager;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        AudioAdapter.OnItemClickListener,
        IMusicStateSubscriber,
        IMusicProgressSubscriber,
        OnMusicPlayingActionListener {

    private RecyclerView recyclerView;
    private AudioPlayPanel playPanel;
    private ClientCoreSDK sdk;
    private List<Audio> data;

    private ImageView ivAlbum;
    private TextView tvAudioTitle;
    private TextView tvAudioArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isApplyingPermissions()) {
            sdk = ClientCoreSDK.get();
            MusicManager.get().createAudioStatePublisher().register(this);
            MusicManager.get().createProgressPublisher().register(this);
            initViews();
            refreshAdapter();
        }
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.main_title);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        playPanel = (AudioPlayPanel) findViewById(R.id.play_panel);
        playPanel.setOnMusicPlayingActionListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        initNavigationView();
    }

    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderView = navigationView.getHeaderView(0);
        ivAlbum = (ImageView) navHeaderView.findViewById(R.id.iv_album);
        tvAudioTitle = (TextView) navHeaderView.findViewById(R.id.tv_audio_title);
        tvAudioArtist = (TextView) navHeaderView.findViewById(R.id.tv_audio_artist);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void refreshAdapter() {
        data = AudioLocalDataManager.get().getLocalAudioList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        AudioAdapter adapter = new AudioAdapter(this, data, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_action_search) {
            new Jump.Launcher(this)
                    .to(SearchActivity.class)
                    .setup()
                    .launch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playPanel.updateAudioPlayingStatus(sdk.getCurrentPlayingMusicState());
        playPanel.loadAudioInfo();
        updateNavigation();
    }

    private void updateNavigation() {
        MusicLoadTask task = new MusicLoadTask();
        task.setOnMusicLoadingListener(new MusicLoadTask.OnMusicLoadingListener() {
            @Override
            public void setupMusic(Audio audio, Bitmap bm) {
                ivAlbum.setImageBitmap(bm);
                tvAudioTitle.setText(audio.title);
                tvAudioArtist.setText(audio.artist);
            }
        });
        task.loadMusic(MediaUtils.COMPRESS_LEVEL_ORIGINAL);
    }

    @Override
    public void onItemClick(Audio audio) {
        if (audio == null || TextUtils.isEmpty(audio.path)) return;
        if (sdk.getPlayingMusic() == null || !sdk.isPlayingCurrentMusic(audio.path)) {
            sdk.playMusic(audio.path);
            sdk.savePlayList(data);
        } else {
            if (sdk.isMusicPlaying()) {
                playPanel.viewDetail();
            } else {
                sdk.playMusic(audio.path, sdk.getCurrentPlayingMusicPosition());
            }
        }
    }

    @Override
    public void onUpdateChange(int state, String path) {
        playPanel.updateAudioPlayingStatus(state);
        if (state == State.PREPARING) {
            playPanel.loadAudioInfo();
            updateNavigation();
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.get().createAudioStatePublisher().unregister(this);
        MusicManager.get().createProgressPublisher().unregister(this);
        NotificationReceiver.get().destroy();
    }
}
