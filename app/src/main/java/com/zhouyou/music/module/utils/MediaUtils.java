package com.zhouyou.music.module.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
        return getAlbumCoverImage(context, audioId, albumId, false);
    }

    public static Bitmap getAlbumCoverImage(Context context, long audioId, long albumId, boolean isThumbnail) {
        Bitmap bm = null;
        if (albumId < 0) {
            // This is something that is not in the database, so get the album art directly
            // from the file.
            if (audioId >= 0) {
                bm = getAlbumCoverImageFromFile(context, audioId, -1);
            } else {
                bm = getDefaultCoverImage(context);
            }
        } else {
            ContentResolver res = context.getContentResolver();
            Uri uri = ContentUris.withAppendedId(ALBUM_URI, albumId);
            if (uri != null) {
                InputStream in;
                try {
                    in = res.openInputStream(uri);
                    return BitmapFactory.decodeStream(in, null, sBitmapOptions);
                } catch (FileNotFoundException ex) {
                    // The album art thumbnail does not actually exist. Maybe the user deleted it, or
                    // maybe it never existed to begin with.
                    bm = getAlbumCoverImageFromFile(context, audioId, albumId);
                    if (bm != null) {
                        if (bm.getConfig() == null) {
                            bm = bm.copy(Bitmap.Config.RGB_565, false);
                            if (bm == null) {
                                bm = getDefaultCoverImage(context);
                            }
                        }
                    }
                }
            }
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(ALBUM_URI, albumId);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                bm = BitmapFactory.decodeStream(in, null, sBitmapOptions);
            } catch (FileNotFoundException ex) {
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or
                // maybe it never existed to begin with.
                bm = getAlbumCoverImageFromFile(context, audioId, albumId);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if (bm == null) {
                            bm = getDefaultCoverImage(context);
                        }
                    } else {
                        bm = getDefaultCoverImage(context);
                    }
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (isThumbnail) {
            bm = getAlbumCoverThumbnail(bm, 120, 120, true);
        }
        return bm;
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

    /**
     * 获得专辑图片的缩略图
     *
     * @param bitMap
     * @param needRecycle
     * @return
     */
    public static Bitmap getAlbumCoverThumbnail(Bitmap bitMap, int w, int h, boolean needRecycle) {
        if (bitMap == null) return null;
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
//        int newWidth = w;
//        int newHeight = h;
        // 计算缩放比例
        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,
                matrix, true);
        if (needRecycle) bitMap.recycle();
        return newBitMap;
    }

    public static Bitmap getCachedBitmap() {
        return mCachedBit;
    }

    public static void clearCacheBitmap() {
        mCachedBit = null;
    }
}