package com.zhouyou.music.module;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhouyou.music.R;
import com.zhouyou.music.base.BaseActivity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：ZhouYou
 * 日期：2017/1/17.
 */
public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl()
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

    }
}
