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

    public static Intent createMusicControlMessage(int state, boolean isPlayBack) {
        Intent intent = new Intent();
        intent.putExtra(MusicConstants.MUSIC_STATE, state);
        intent.putExtra(MusicConstants.MUSIC_PLAY_BACK, isPlayBack);
        return intent;
    }

    public static Intent createMusicStateMessage(int state, int seekPosition) {
        Intent intent = new Intent();
        intent.putExtra(MusicConstants.MUSIC_PLAYING_POSITION, seekPosition);
        intent.putExtra(MusicConstants.MUSIC_STATE, state);
        return intent;
    }

    public static Intent createMusicModeMessage(int mode) {
        Intent intent = new Intent();
        intent.putExtra(MusicConstants.MUSIC_MODE, mode);
        return intent;
    }
}
