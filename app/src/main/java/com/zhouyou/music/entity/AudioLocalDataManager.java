package com.zhouyou.music.entity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.library.utils.PrefUtils;
import com.zhouyou.music.base.App;
import com.zhouyou.music.config.Constants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/22.
 */
public class AudioLocalDataManager {
    private static final String TAG = "AudioLocalDataManager";

    private static class DataManagerHolder {
        private static final AudioLocalDataManager MANAGER = new AudioLocalDataManager();
    }

    public static AudioLocalDataManager get() {
        return DataManagerHolder.MANAGER;
    }

    private ContentResolver resolver;
    private List<Audio> audioList;
    private Dao<Audio, Integer> dao;

    private AudioLocalDataManager() {
        Context context = App.get().getApplicationContext();
        try {
            context.grantUriPermission(App.get().getPackageName(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } catch (Exception e) {
            Log.e("Permission", e.toString());
        }
        resolver = context.getContentResolver();
        audioList = getAudioList();
        try {
            dao = App.get().getHelper().getMusicDao();
            saveAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String[] AUDIO_KEYS = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.TITLE_KEY,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ARTIST_KEY,
            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM_KEY,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_PODCAST,
            MediaStore.Audio.Media.IS_ALARM,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.DATA
    };

    /**
     * 获取音频列表
     *
     * @return
     */
    private synchronized List<Audio> getAudioList() {
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, AUDIO_KEYS, null, null, null);
        if (cursor == null) return null;
        List<Audio> audioList = new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            for (final String key : AUDIO_KEYS) {
                final int columnIndex = cursor.getColumnIndex(key);
                final int type = cursor.getType(columnIndex);
                switch (type) {
                    case Cursor.FIELD_TYPE_BLOB:
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        float floatValue = cursor.getFloat(columnIndex);
                        bundle.putFloat(key, floatValue);
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        int intValue = cursor.getInt(columnIndex);
                        bundle.putInt(key, intValue);
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        String strValue = cursor.getString(columnIndex);
                        bundle.putString(key, strValue);
                        break;
                }
            }
            Audio audio = new Audio(bundle);
            audioList.add(audio);
        }
        cursor.close();
        return audioList;
    }

    /**
     * 保存全部
     */
    private void saveAll() {
        if (ListUtils.isNull(audioList)) return;
        try {
            for (Audio audio : audioList) {
                dao.createIfNotExists(audio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询全部
     *
     * @return
     */
    public List<Audio> queryAll() {
        List<Audio> data = null;
        try {
            data = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 根据路径查询音乐
     *
     * @param path
     * @return
     */
    public Audio queryByPath(String path) {
        if (TextUtils.isEmpty(path)) return null;
        Audio audio = null;
        QueryBuilder qb = dao.queryBuilder();
        try {
            audio = (Audio) qb.where().eq("path", path).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return audio;
    }

    public void update(Audio audio) {
        try {
            dao.createOrUpdate(audio);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
