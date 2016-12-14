// IMusicReceiver.aidl
package com.zhouyou.remote;

// Declare any non-default types here with import statements
import com.zhouyou.remote.Music;

interface IMusicReceiver {

    void onReceive(in Music intent, int currState);
}
