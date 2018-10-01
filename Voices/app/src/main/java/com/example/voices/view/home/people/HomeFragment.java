package com.example.voices.view.home.people;

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
import com.example.voices.model.Post;
import com.example.voices.view.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {


    private HomeOnFragmentInteractionListener mListener;
    MainActivity containerActivity;
    ViewPager viewPager;
    List<Post> posts;
    List<Fragment> fragmentList;
    PostController postController;
    int count;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        count = 0;
        posts = new ArrayList<>();
        fragmentList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        postController = new PostController();
        viewPager = view.findViewById(R.id.viewPagerFragmentHome);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(40);


        for (Map.Entry<String, Boolean> folUs : containerActivity.user.getFollowingIds().entrySet()) {
            FollowUserFragment fragment = FollowUserFragment.newInstance(folUs.getKey(), this);
            fragmentList.add(fragment);
        }

        PostsViewPagerAdapter postsViewPagerAdapter = new PostsViewPagerAdapter(getFragmentManager(), fragmentList);
        viewPager.setAdapter(postsViewPagerAdapter);


        /*count = postController.getPostsFromIFollowToBFollow(containerActivity.user, count, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {

                for (Post post : resultado) {
                    PostFragment postFragment = PostFragment.newInstance(post);
                    fragmentList.add(postFragment);
                }

                postsViewPagerAdapter.notifyDataSetChanged();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (fragmentList.size() - position < 2 && count < containerActivity.user.getFollowingIds().size()){
                    count = postController.getPostsFromIFollowToBFollow(containerActivity.user, count, new ResultListener<List<Post>>() {
                        @Override
                        public void finish(List<Post> resultado) {
                            for (Post post : resultado) {
                                PostFragment postFragment = PostFragment.newInstance(post);
                                fragmentList.add(postFragment);
                            }
                            postsViewPagerAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeOnFragmentInteractionListener) {
            mListener = (HomeOnFragmentInteractionListener) context;
            containerActivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HomeOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void onPostClicked(List<Post> posts, String userId) {

        mListener.homeOnFragmentInteraction(posts, userId, viewPager.getCurrentItem());


    }


    public interface HomeOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void homeOnFragmentInteraction(List<Post> posts, String userId, int position);
    }
}
