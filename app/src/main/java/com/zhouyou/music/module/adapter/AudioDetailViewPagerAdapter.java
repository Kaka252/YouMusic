package com.zhouyou.music.module.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.music.base.BaseFragment;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/7.
 */
public class AudioDetailViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> data;

    public AudioDetailViewPagerAdapter(FragmentManager fm, List<BaseFragment> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return ListUtils.getElement(data, position);
    }

    @Override
    public int getCount() {
        return ListUtils.getCount(data);
    }
}
