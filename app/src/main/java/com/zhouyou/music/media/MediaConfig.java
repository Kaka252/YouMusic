package com.zhouyou.music.media;

import com.zhouyou.music.media.state.AudioPlayState;

/**
 * 作者：ZhouYou
 * 日期：2016/11/25.
 */
public class MediaConfig {

    /**
     * 当前播放状态
     */
    public static int CURRENT_STATE;
    /**
     * 当前播放的音频毫秒数
     */
    public static int CURRENT_POSITION;

    /**
     * 进度是否由用户控制
     */
    public static boolean IS_PROGRESS_CONTROLLED_BY_USER = false;

    /**
     * 是否播放上一首
     */
    public static boolean IS_PLAY_BACK;

    public static void initConfig() {
        CURRENT_STATE = AudioPlayState.IDLE;
    }

    public void changeState(int currState) {
        CURRENT_STATE = currState;
    }

}
