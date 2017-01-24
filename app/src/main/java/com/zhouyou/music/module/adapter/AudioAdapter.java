package com.zhouyou.music.module.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {

    private Activity activity;
    private List<Audio> data;
    private OnItemClickListener listener;

    public AudioAdapter(Activity activity, List<Audio> data, OnItemClickListener listener) {
        this.activity = activity;
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_audio, parent, false);
        return new ViewHolder(view);
    }

    private Audio getItem(int position) {
        return ListUtils.getElement(data, position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Audio audio = getItem(position);
        if (audio != null) {
            holder.tvAudioTitle.setText(audio.title);
            holder.tvAudioArtist.setText(audio.artist);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onItemClick(audio);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ListUtils.getCount(data);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAudioTitle;
        TextView tvAudioArtist;

        ViewHolder(View itemView) {
            super(itemView);
            tvAudioTitle = (TextView) itemView.findViewById(R.id.tv_audio_title);
            tvAudioArtist = (TextView) itemView.findViewById(R.id.tv_audio_artist);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Audio audio);
    }
}
