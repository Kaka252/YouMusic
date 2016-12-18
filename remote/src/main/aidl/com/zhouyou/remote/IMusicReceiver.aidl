// IMusicReceiver.aidl
package com.zhouyou.remote;

interface IMusicReceiver {

    void onReceive(String currMusicPath, int currState);
}
