package com.zhouyou.music.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.lang.reflect.Field;

/**
 * 作者：ZhouYou
 * 日期：2016/12/7.
 */
public class BaseFragment extends Fragment {

    protected Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
