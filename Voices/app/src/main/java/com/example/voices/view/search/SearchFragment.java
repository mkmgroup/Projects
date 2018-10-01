package com.example.voices.view.search;

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


public class SearchFragment extends Fragment {


    private SearchOnFragmentInteractionListener mListener;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerFragmentSearch);
        SearchViewPagerAdapter adapterViewPager = new SearchViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabsFragmentSearch);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.searchOnFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchOnFragmentInteractionListener) {
            mListener = (SearchOnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface SearchOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void searchOnFragmentInteraction(Uri uri);
    }
}
