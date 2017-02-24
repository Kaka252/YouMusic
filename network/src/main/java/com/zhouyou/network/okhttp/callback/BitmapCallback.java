package com.zhouyou.network.okhttp.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

import okhttp3.Response;

/**
 * 作者：ZhouYou
 * 日期：2017/2/24.
 */
public abstract class BitmapCallback extends AbsCallback<Bitmap> {

    @Override
    public Bitmap parseResponse(Response resp) throws IOException {
        return BitmapFactory.decodeStream(resp.body().source().inputStream());
    }
}
