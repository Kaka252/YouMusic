// IMusicReceiver.aidl
package com.zhouyou.remote;

import com.zhouyou.remote.MusicConfig;

interface IMusicReceiver {

    void onReceive(in MusicConfig config);
}
