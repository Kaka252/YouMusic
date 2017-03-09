package com.zhouyou.music.module;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.NetMusic;
import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.OkHttpSdk;
import com.zhouyou.network.okhttp.callback.BitmapCallback;
import com.zhouyou.network.okhttp.callback.FileCallback;
import com.zhouyou.network.okhttp.callback.GsonCallback;
import com.zhouyou.network.okhttp.callback.StringCallback;
import com.zhouyou.network.okhttp.method.GetRequestBuilder;
import com.zhouyou.network.okhttp.param.Params;

import java.io.File;

import okhttp3.Call;

/**
 * 作者：ZhouYou
 * 日期：2017/1/17.
 */
public class SearchActivity extends BaseActivity {

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        request();
        download();
        getBitmap();
        getBatch();
    }

    private void request() {
        String url = "https://api.douban.com/v2/music/search";
        Params params = new Params();
        params.put("q", "银魂");
        params.put("start", "0");
        params.put("count", "1");
        final ApiRequestCall call = OkHttpSdk.getInstance()
                .get(url, params)
                .tag(this)
                .build();
        call.async(new GsonCallback<NetMusic>() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(NetMusic music) {
                Log.d(TAG, music.toString());
            }
        });
    }

    private void download() {
        String url = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1487925706&di=d5eafb6a2bf83796ffb88a91322af3f4&src=http://i0.hdslb.com/bfs/face/b7246b976ee6225da7258dc604683af258d69709.jpg";
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "gakki.jpg";
        OkHttpSdk.getInstance().get().url(url).tag(this).build().async(new FileCallback(dir, fileName) {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(File resp) {
                Log.d(TAG, resp.toString());
            }
        });
    }

    private void getBitmap() {
        String url = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1487925706&di=d5eafb6a2bf83796ffb88a91322af3f4&src=http://i0.hdslb.com/bfs/face/b7246b976ee6225da7258dc604683af258d69709.jpg";
        OkHttpSdk.getInstance().get().url(url).tag(this).build().async(new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(Bitmap resp) {
                ((ImageView) findViewById(R.id.iv_image)).setImageBitmap(resp);
            }
        });
    }

    private void getBatch() {
        String url = "http://api.weizhipin.com/api/batch/batchRun";
        String batchKey = "batch_method_feed";

        GetRequestBuilder profile = new GetRequestBuilder();
        profile.url("geek/getBossProfile");
        profile.addParam("bossId", "1823");
//        profile.addParam("lid", );

        GetRequestBuilder list = new GetRequestBuilder();
        list.url("geek/getBossProfileJobList");
        list.addParam("bossId", "1823");
        list.addParam("page", "1");
//        list.addParam("lid", )

        ApiRequestCall call = OkHttpSdk.getInstance()
                .batch(url, batchKey)
                .addRequest(profile)
                .addRequest(list)
                .build();
        call.async(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String resp) {
                Log.d(TAG, resp);
            }
        });
    }
}
