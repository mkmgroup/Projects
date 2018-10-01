package com.example.voices.view.home.people;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.voices.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 11/06/2018.
 */

public class PostsViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentList;

    public PostsViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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
