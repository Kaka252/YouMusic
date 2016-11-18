package com.zhouyou.music.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;

import com.zhouyou.music.base.App;
import com.zhouyou.music.entity.Audio;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/18.
 */
public class MediaUtils {

    private static final String[] AUDIO_KEYS = new String[]{
            Media._ID,
            Media.TITLE,
            Media.TITLE_KEY,
            Media.ARTIST,
            Media.ARTIST_ID,
            Media.ARTIST_KEY,
            Media.COMPOSER,
            Media.ALBUM,
            Media.ALBUM_ID,
            Media.ALBUM_KEY,
            Media.DISPLAY_NAME,
            Media.DURATION,
            Media.SIZE,
            Media.YEAR,
            Media.TRACK,
            Media.IS_RINGTONE,
            Media.IS_PODCAST,
            Media.IS_ALARM,
            Media.IS_MUSIC,
            Media.IS_NOTIFICATION,
            Media.MIME_TYPE,
            Media.DATA
    };

    private volatile static MediaUtils MEDIA;

    public static MediaUtils get() {
        if (MEDIA == null) {
            synchronized (MediaUtils.class) {
                if (MEDIA == null) {
                    MEDIA = new MediaUtils();
                }
            }
        }
        return MEDIA;
    }

    private ContentResolver resolver;
    private Context context;

    private MediaUtils() {
        context = App.get().getApplicationContext();
        resolver = context.getContentResolver();
    }

    /**
     * 获取音频列表
     *
     * @return
     */
    public synchronized List<Audio> getAudioList() {
        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, AUDIO_KEYS, null, null, null);
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
}
