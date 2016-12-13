package com.zhouyou.remote;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 */
public class Music implements Parcelable {

    private Bundle mExtra;

    protected Music(Parcel in) {
        ClassLoader classLoader = getClass().getClassLoader();
        mExtra = in.readBundle(classLoader);
    }

    public Bundle getExtras() {
        if (mExtra == null) {
            mExtra = new Bundle();
        }
        return mExtra;
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeBundle(mExtra);
    }
}
