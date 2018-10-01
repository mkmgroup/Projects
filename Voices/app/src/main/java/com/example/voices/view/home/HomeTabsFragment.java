package com.example.voices.view.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.voices.R;
import com.example.voices.view.MainActivity;
import com.example.voices.view.search.SearchViewPagerAdapter;


public class HomeTabsFragment extends Fragment {


    private HomeTabsOnFragmentInteractionListener mListener;
    private MainActivity containerActivity;

    public HomeTabsFragment() {
        // Required empty public constructor
    }


    public static HomeTabsFragment newInstance() {
        HomeTabsFragment fragment = new HomeTabsFragment();


        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_tabs, container, false);


        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerHomeTabs);
        HomeViewPagerAdapter adapterViewPager = new HomeViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.homeTabs);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeTabsOnFragmentInteractionListener) {
            mListener = (HomeTabsOnFragmentInteractionListener) context;
            containerActivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HomeTabsOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface HomeTabsOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void homeTabsOnFragmentInteraction(Uri uri);
    }
}
