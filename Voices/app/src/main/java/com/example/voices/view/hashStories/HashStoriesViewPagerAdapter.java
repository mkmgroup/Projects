package com.example.voices.view.hashStories;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Juan on 18/06/2018.
 */

public class HashStoriesViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentList;


    public HashStoriesViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;

    }
    @Override
    public Fragment getItem(int position) {



        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}