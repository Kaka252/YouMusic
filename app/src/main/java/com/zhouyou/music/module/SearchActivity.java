package com.zhouyou.music.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.music.entity.NetMusic;
import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.OkHttpSdk;
import com.zhouyou.network.okhttp.callback.GsonCallback;
import com.zhouyou.network.okhttp.param.Params;

import java.io.IOException;

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
        try {
            request();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void request() throws IOException {
        String url = "https://api.douban.com/v2/music/search";
        Params params = new Params();
        params.put("q", "银魂");
        params.put("start", "0");
        params.put("count", "1");
        final ApiRequestCall call = OkHttpSdk.getInstance()
                .get()
                .url(url)
                .addParams(params)
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
}
