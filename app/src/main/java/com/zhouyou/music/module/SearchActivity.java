package com.zhouyou.music.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.OkHttpSdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
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
        try {
            request();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void request() throws IOException {
        String url = "https://api.douban.com/v2/music/search";
        Map<String, String> params = new HashMap<>();
        params.put("q", "银魂");
        params.put("start", "0");
        params.put("count", "10");
        final ApiRequestCall call = OkHttpSdk.getInstance()
                .get()
                .url(url)
                .tag(this)
                .addParams(params)
                .build();

        call.async(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse");
                String htmlString = response.body().string();
                Log.d(TAG, htmlString);
            }
        });


    }
}
