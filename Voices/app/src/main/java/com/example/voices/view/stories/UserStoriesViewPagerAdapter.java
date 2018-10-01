package com.example.voices.view.stories;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.voices.controller.PostController;
import com.example.voices.dao.PostDAO;
import com.example.voices.model.Post;
import com.example.voices.model.User;
import com.example.voices.util.FrUserStoriesResultListener;
import com.example.voices.util.ResultListener;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * Created by Juan on 13/06/2018.
 */

public class UserStoriesViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentList;


    public UserStoriesViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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