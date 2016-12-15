package com.zhouyou.remote;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 */
public class Music implements Parcelable {

    private int musicId;
    /*音乐播放的地址*/
    private String musicPath;
    /*音乐播放的进度*/
    private int currentPosition;

    public Music() {
    }

    protected Music(Parcel in) {
        this.musicId = in.readInt();
        this.musicPath = in.readString();
        this.currentPosition = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(musicId);
        dest.writeString(musicPath);
        dest.writeInt(currentPosition);
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
