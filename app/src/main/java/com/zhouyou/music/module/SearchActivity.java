package com.zhouyou.music.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    }

    private void request() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.douban.com/v2/music/search?q=银魂&start=0&count=10")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse");
                String htmlString = response.body().string();
//                handler.obtainMessage(0, htmlString).sendToTarget();
            }
        });
    }
}
