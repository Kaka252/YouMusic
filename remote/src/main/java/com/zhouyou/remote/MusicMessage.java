package com.zhouyou.remote;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyou on 16/12/18.
 */
public class MusicMessage implements Parcelable {

    public int musicId;

    public List<Music> playList = new ArrayList<>();

    public MusicMessage() {
    }

    protected MusicMessage(Parcel in) {
        musicId = in.readInt();
        playList = in.createTypedArrayList(Music.CREATOR);
    }

    public static final Creator<MusicMessage> CREATOR = new Creator<MusicMessage>() {
        @Override
        public MusicMessage createFromParcel(Parcel in) {
            return new MusicMessage(in);
        }

        @Override
        public MusicMessage[] newArray(int size) {
            return new MusicMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(musicId);
        dest.writeTypedList(playList);
    }
}
