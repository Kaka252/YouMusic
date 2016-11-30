package com.zhouyou.music.module.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.zhouyou.music.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：ZhouYou
 * 日期：2016/11/24.
 */
public class MediaUtils {

    private static final String TAG = MediaUtils.class.getSimpleName();

    private static final Uri ALBUM_URI = Uri.parse("content://media/external/audio/albumart");
    private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private static Bitmap mCachedBit = null;

    public static Bitmap getAlbumCoverImage(Context context, long audioId, long albumId) {
        if (albumId < 0) {
            // This is something that is not in the database, so get the album art directly
            // from the file.
            if (audioId >= 0) {
                Bitmap bm = getAlbumCoverImageFromFile(context, audioId, -1);
                if (bm != null) {
                    return bm;
                }
            }
            return getDefaultCoverImage(context);
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(ALBUM_URI, albumId);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, sBitmapOptions);
            } catch (FileNotFoundException ex) {
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or
                // maybe it never existed to begin with.
                Bitmap bm = getAlbumCoverImageFromFile(context, audioId, albumId);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if (bm == null) {
                            return getDefaultCoverImage(context);
                        }
                    }
                } else {
                    bm = getDefaultCoverImage(context);
                }
                return bm;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static Bitmap getDefaultCoverImage(Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_music_default_bg, opts);
    }

    private static Bitmap getAlbumCoverImageFromFile(Context context, long audioId, long albumId) {
        Bitmap bm = null;
        if (albumId < 0 && audioId < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumId < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + audioId + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(ALBUM_URI, albumId);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bm != null) {
            mCachedBit = bm;
        }
        return bm;
    }

    public static Bitmap getCachedBitmap() {
        return mCachedBit;
    }

    public static void clearCacheBitmap() {
        mCachedBit = null;
    }
}
