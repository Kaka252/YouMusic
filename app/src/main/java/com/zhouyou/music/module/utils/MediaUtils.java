package com.zhouyou.music.module.utils;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * 作者：ZhouYou
 * 日期：2016/11/24.
 */
public class MediaUtils {

    private static final String TAG = MediaUtils.class.getSimpleName();

    private static final Uri ALBUM_URI = Uri.parse("content://media/external/audio/albumart");

    /**
     * 从文件当中获取专辑封面位图
     *
     * @param context
     * @param audioId
     * @param albumId
     * @return
     */
    public static Bitmap getAlbumCoverFromFile(Context context, long audioId, long albumId) {
        Bitmap bm = null;
        if (albumId < 0 && audioId < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            FileDescriptor fd = null;
            if (albumId < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + audioId + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                Uri uri = ContentUris.withAppendedId(ALBUM_URI, albumId);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            }
            options.inSampleSize = 1;
            // 只进行大小判断
            options.inJustDecodeBounds = true;
            // 调用此方法得到options得到图片大小
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            // 我们的目标是在800pixel的画面上显示
            // 所以需要调用computeSampleSize得到图片缩放的比例
            options.inSampleSize = 100;
            // 我们得到了缩放的比例，现在开始正式读入Bitmap数据
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            //根据options参数，减少所需要的内存
            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
            Log.d(TAG, "getAlbumCoverFromFile = " + bm.getByteCount() + " bytes");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bm;
    }
}
