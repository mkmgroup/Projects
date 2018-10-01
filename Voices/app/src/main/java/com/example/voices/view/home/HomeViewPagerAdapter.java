package com.example.voices.view.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.voices.view.home.hashtags.HashtagsFragment;
import com.example.voices.view.home.people.HomeFragment;
import com.example.voices.view.search.SearchHashtagsFragment;
import com.example.voices.view.search.SearchPeopleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 13/06/2018.
 */

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HashtagsFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "PERSONAS";
        } else{
            return "HASHTAGS";
        }
    }
}
