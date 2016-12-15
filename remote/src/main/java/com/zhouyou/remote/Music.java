package com.zhouyou.remote;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 */
public class Music implements Parcelable {

    private int audioId;
    /*音乐播放的地址*/
    private String audioPath;
    /*音乐播放的进度*/
    private int currentPosition;

    public Music() {
    }

    protected Music(Parcel in) {
        this.audioId = in.readInt();
        this.audioPath = in.readString();
        this.currentPosition = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(audioId);
        dest.writeString(audioPath);
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

    public int getAudioId() {
        return audioId;
    }

    public void setAudioId(int audioId) {
        this.audioId = audioId;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
