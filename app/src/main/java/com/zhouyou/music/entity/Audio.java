package com.zhouyou.music.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

/**
 * 作者：ZhouYou
 * 日期：2016/11/18.
 */
public class Audio implements Parcelable {

    public String title;
    public String titleKey;
    public String artist;
    public String artistKey;
    public String composer;
    public String album;
    public String albumKey;
    public String displayName;
    public String mimeType;
    public String path;

    public int id;
    public int artistId;
    public int albumId;
    public int year;
    public int track;

    public int duration = 0;
    public int currentPosition = 0;
    public int size = 0;

    public boolean isRingtone = false;
    public boolean isPodcast = false;
    public boolean isAlarm = false;
    public boolean isMusic = false;
    public boolean isNotification = false;

    public Audio(Bundle bundle) {
        id = bundle.getInt(MediaStore.Audio.Media._ID);
        title = bundle.getString(MediaStore.Audio.Media.TITLE);
        titleKey = bundle.getString(MediaStore.Audio.Media.TITLE_KEY);
        artist = bundle.getString(MediaStore.Audio.Media.ARTIST);
        artistKey = bundle.getString(MediaStore.Audio.Media.ARTIST_KEY);
        composer = bundle.getString(MediaStore.Audio.Media.COMPOSER);
        album = bundle.getString(MediaStore.Audio.Media.ALBUM);
        albumKey = bundle.getString(MediaStore.Audio.Media.ALBUM_KEY);
        displayName = bundle.getString(MediaStore.Audio.Media.DISPLAY_NAME);
        year = bundle.getInt(MediaStore.Audio.Media.YEAR);
        mimeType = bundle.getString(MediaStore.Audio.Media.MIME_TYPE);
        path = bundle.getString(MediaStore.Audio.Media.DATA);

        artistId = bundle.getInt(MediaStore.Audio.Media.ARTIST_ID);
        albumId = bundle.getInt(MediaStore.Audio.Media.ALBUM_ID);
        track = bundle.getInt(MediaStore.Audio.Media.TRACK);
        duration = bundle.getInt(MediaStore.Audio.Media.DURATION);
        size = bundle.getInt(MediaStore.Audio.Media.SIZE);
        isRingtone = bundle.getInt(MediaStore.Audio.Media.IS_RINGTONE) == 1;
        isPodcast = bundle.getInt(MediaStore.Audio.Media.IS_PODCAST) == 1;
        isAlarm = bundle.getInt(MediaStore.Audio.Media.IS_ALARM) == 1;
        isMusic = bundle.getInt(MediaStore.Audio.Media.IS_MUSIC) == 1;
        isNotification = bundle.getInt(MediaStore.Audio.Media.IS_NOTIFICATION) == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.titleKey);
        dest.writeString(this.artist);
        dest.writeString(this.artistKey);
        dest.writeString(this.composer);
        dest.writeString(this.album);
        dest.writeString(this.albumKey);
        dest.writeString(this.displayName);
        dest.writeString(this.mimeType);
        dest.writeString(this.path);
        dest.writeInt(this.id);
        dest.writeInt(this.artistId);
        dest.writeInt(this.albumId);
        dest.writeInt(this.year);
        dest.writeInt(this.track);
        dest.writeInt(this.duration);
        dest.writeInt(this.currentPosition);
        dest.writeInt(this.size);
        dest.writeByte(this.isRingtone ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPodcast ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAlarm ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isMusic ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isNotification ? (byte) 1 : (byte) 0);
    }

    protected Audio(Parcel in) {
        this.title = in.readString();
        this.titleKey = in.readString();
        this.artist = in.readString();
        this.artistKey = in.readString();
        this.composer = in.readString();
        this.album = in.readString();
        this.albumKey = in.readString();
        this.displayName = in.readString();
        this.mimeType = in.readString();
        this.path = in.readString();
        this.id = in.readInt();
        this.artistId = in.readInt();
        this.albumId = in.readInt();
        this.year = in.readInt();
        this.track = in.readInt();
        this.duration = in.readInt();
        this.currentPosition = in.readInt();
        this.size = in.readInt();
        this.isRingtone = in.readByte() != 0;
        this.isPodcast = in.readByte() != 0;
        this.isAlarm = in.readByte() != 0;
        this.isMusic = in.readByte() != 0;
        this.isNotification = in.readByte() != 0;
    }

    public static final Creator<Audio> CREATOR = new Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel source) {
            return new Audio(source);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };
}
