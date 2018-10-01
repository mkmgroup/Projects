package com.example.voices.view.home.hashtags;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.voices.R;
import com.example.voices.controller.PostController;
import com.example.voices.view.MainActivity;
import com.example.voices.view.home.people.FollowUserFragment;
import com.example.voices.view.home.people.PostsViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HashtagsFragment extends Fragment {

    private HashtagsOnFragmentInteractionListener mListener;
    private MainActivity containerActivity;

    ViewPager viewPager;
    List<Fragment> fragmentList;
    PostController postController;

    public HashtagsFragment() {
        // Required empty public constructor
    }


    public static HashtagsFragment newInstance() {
        HashtagsFragment fragment = new HashtagsFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_hashtags, container, false);


        fragmentList = new ArrayList<>();
        postController = new PostController();
        viewPager = view.findViewById(R.id.viewPagerFragmentHashtags);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(40);


        for (Map.Entry<String, Boolean> folUs : containerActivity.user.getFollowingHashes().entrySet()) {
            FollowHashtagFragment fragment = FollowHashtagFragment.newInstance(folUs.getKey(), this);
            fragmentList.add(fragment);
        }

        HashtagsViewPagerAdapter hashtagsViewPagerAdapter = new HashtagsViewPagerAdapter(getFragmentManager(), fragmentList);
        viewPager.setAdapter(hashtagsViewPagerAdapter);

       return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HashtagsOnFragmentInteractionListener) {
            mListener = (HashtagsOnFragmentInteractionListener) context;
            containerActivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HashtagsOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onHashClicked(String hash){
        mListener.hashtagsOnFragmentInteraction(hash);
    }


    public interface HashtagsOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void hashtagsOnFragmentInteraction(String hash);
    }
}
