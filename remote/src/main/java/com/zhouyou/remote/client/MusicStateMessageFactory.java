package com.zhouyou.remote.client;

import android.content.Intent;

import com.zhouyou.remote.constants.MusicConstants;

/**
 * Created by zhouyou on 16/12/19.
 */

public class MusicStateMessageFactory {

    public static Intent createMusicStateMessage(int state) {
        Intent intent = new Intent();
        intent.putExtra(MusicConstants.MUSIC_STATE, state);
        return intent;
    }

    public static Intent createMusicStateMessage(int state, int seekPosition) {
        Intent intent = new Intent();
        intent.putExtra(MusicConstants.MUSIC_PLAYING_POSITION, seekPosition);
        intent.putExtra(MusicConstants.MUSIC_STATE, state);
        return intent;
    }
}
