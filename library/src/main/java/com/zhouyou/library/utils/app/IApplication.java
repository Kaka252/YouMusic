package com.zhouyou.library.utils.app;

import android.content.Context;

/**
 * Created by zhouyou on 17/2/7.
 */

public interface IApplication {

    Context getContext();

    IApplication getInstance();

    String getPackageName();
}
