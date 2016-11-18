package com.zhouyou.music;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.zhouyou.music.adapter.AudioAdapter;
import com.zhouyou.music.utils.MediaUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.list_view);

        AudioAdapter adapter = new AudioAdapter(this, MediaUtils.getAudioList(this));
        listView.setAdapter(adapter);
    }
}
