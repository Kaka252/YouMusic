package com.zhouyou.music.module.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.music.R;
import com.zhouyou.music.entity.Audio;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/18.
 */
public class AudioAdapter extends BaseAdapter {

    private Activity activity;
    private List<Audio> data;
    private LayoutInflater inflater;

    public AudioAdapter(Activity activity, List<Audio> data) {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ListUtils.getCount(data);
    }

    @Override
    public Audio getItem(int position) {
        return ListUtils.getElement(data, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_audio, null);
            holder.tvAudioTitle = (TextView) convertView.findViewById(R.id.tv_audio_title);
            holder.tvAudioArtist = (TextView) convertView.findViewById(R.id.tv_audio_artist);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Audio audio = getItem(position);
        if (audio != null) {
            holder.tvAudioTitle.setText(audio.title);
            holder.tvAudioArtist.setText(audio.artist);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tvAudioTitle;
        TextView tvAudioArtist;
    }
}
