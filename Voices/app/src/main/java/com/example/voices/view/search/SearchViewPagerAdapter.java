package com.example.voices.view.search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 29/05/2018.
 */

public class SearchViewPagerAdapter extends FragmentStatePagerAdapter{
    private List<Fragment> fragmentList;

    public SearchViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentList.add(new SearchPeopleFragment());
        fragmentList.add(new SearchHashtagsFragment());
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
