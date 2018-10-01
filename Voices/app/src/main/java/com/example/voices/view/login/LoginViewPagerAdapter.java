package com.example.voices.view.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.voices.view.search.SearchHashtagsFragment;
import com.example.voices.view.search.SearchPeopleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 30/05/2018.
 */

public class LoginViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;

    public LoginViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentList.add(new LoginFragment());
        fragmentList.add(new RegisterFragment());
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
            return "LOGIN";
        } else{
            return "REGISTRARSE";
        }
    }
}