package com.zhouyou.music.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.zhouyou.library.utils.PoolUtils;
import com.zhouyou.music.base.App;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.module.utils.MediaUtils;

/**
 * 作者：ZhouYou
 * 日期：2016/12/27.
 */
public class MusicLoadTask {

    private Context context;
    private OnMusicLoadingListener listener;

    public void setOnMusicLoadingListener(OnMusicLoadingListener listener) {
        this.listener = listener;
    }

    public MusicLoadTask() {
        context = App.get().getApplicationContext();
    }

    /**
     * 读取音乐信息
     */
    public void loadMusic(final boolean isThumbnail) {
        PoolUtils.POOL.submit(new Runnable() {
            @Override
            public void run() {
                Audio audio = ClientCoreSDK.get().getPlayingMusic();
                if (audio == null) return;
                Bitmap bm = MediaUtils.getAlbumCoverImage(context, audio.id, audio.albumId, isThumbnail);
                Message msg = Message.obtain();
                Bundle b = new Bundle();
                if (bm != null) {
                    b.putParcelable("album", bm);
                }
                b.putParcelable("audio", audio);
                msg.setData(b);
                handler.sendMessage(msg);
            }
        });
    }

    public void loadMusic() {
        loadMusic(false);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle data = msg.getData();
            Audio audio = data.getParcelable("audio");
            Bitmap bm = data.getParcelable("album");
            if (listener != null) listener.setupMusic(audio, bm);
            return true;
        }
    });

    public interface OnMusicLoadingListener {
        void setupMusic(Audio audio, Bitmap bm);
    }
}
