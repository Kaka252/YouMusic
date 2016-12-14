package com.zhouyou.remote.client;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import com.zhouyou.remote.IMusicReceiver;
import com.zhouyou.remote.Music;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 * 信号接受者
 */
public class Receiver extends IMusicReceiver.Stub {

    @Override
    public void onReceive(int currState) throws RemoteException {
//        Message msg = Message.obtain();
//        Bundle b = new Bundle();
//        b.putInt("state", currState);
//        msg.setData(b);
//        handler.sendMessage(msg);



    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return true;
        }
    });
}
