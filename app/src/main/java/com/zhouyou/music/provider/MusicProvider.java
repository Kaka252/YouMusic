package com.zhouyou.music.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.zhouyou.music.entity.Audio;

/**
 * 作者：ZhouYou
 * 日期：2017/1/11.
 */
public class MusicProvider {

    private ContentResolver resolver = null;

    public MusicProvider(Context context) {
        resolver = context.getContentResolver();
    }

    public long insertAudio(Audio audio) {
        if (audio == null) return -1;
        ContentValues values = new ContentValues();
        values.put("id", audio.id);
        values.put("path", audio.path);
        values.put("title", audio.title);
        values.put("artist", audio.artist);
        values.put("albumId", audio.albumId);

        Uri uri = resolver.insert(MediaContentProvider.CONTENT_URI, values);
        String itemId = null;
        if (uri != null) {
            itemId = uri.getPathSegments().get(1);
        }
        if (TextUtils.isEmpty(itemId)) {
            return -1;
        }
        return Integer.valueOf(itemId).longValue();
    }

    public boolean updateArticle(Audio audio) {
        if (audio == null) return false;
        Uri uri = ContentUris.withAppendedId(MediaContentProvider.CONTENT_URI, audio.id);

        ContentValues values = new ContentValues();
        values.put("id", audio.id);
        values.put("path", audio.path);
        values.put("title", audio.title);
        values.put("artist", audio.artist);
        values.put("albumId", audio.albumId);
        int count = resolver.update(uri, values, null, null);
        return count > 0;
    }

    public Audio getAudioById(long id) {
        Uri uri = ContentUris.withAppendedId(MediaContentProvider.CONTENT_URI, id);
        String[] projection = new String[] { "id", "path"};
        Cursor cursor = resolver.query(uri, projection, null, null, "id asc");
        if (cursor != null) {
            if (!cursor.moveToFirst()) {
                return null;
            }

            String title = cursor.getString(2);
            String path = cursor.getString(3);
            cursor.close();
        }
        return null;
    }
}
