package com.zhouyou.remote.server;

import android.os.RemoteException;

import com.zhouyou.remote.IMusicControlInterface;
import com.zhouyou.remote.Music;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 */
public class MPBinder extends IMusicControlInterface.Stub {

    @Override
    public void init() throws RemoteException {

    }

    @Override
    public void play(Music music) throws RemoteException {

    }
}
