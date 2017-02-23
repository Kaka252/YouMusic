package com.zhouyou.music.module;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;
import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.OkHttpSdk;

import java.util.HashMap;
import java.util.Map;

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

        String url = "https://api.douban.com/v2/music/search";
        Map<String, String> params = new HashMap<>();
        params.put("q", "银魂");
        params.put("start", "0");
        params.put("count", "10");
        ApiRequestCall call = OkHttpSdk.getInstance()
                .get()
                .url(url)
                .tag(this)
                .addParams(params)
                .build();






//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://api.douban.com/v2/music/search?q=银魂&start=0&count=10")
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, "onResponse");
//                String htmlString = response.body().string();
//            }
//        });
    }
}
