package com.example.voices.view.stories;

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
import com.example.voices.util.ResultListener;
import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;


public class UserStoriesFragment extends Fragment implements StoriesProgressView.StoriesListener {

    private StoriesActivity containerActivity;
    private UserStoriesOnFragmentInteractionListener mListener;
    private String userId;
    ViewPager viewPager;
    StoriesProgressView storiesProgressView;
    PostController postController;
    List<Fragment> fragmentList;
    List<Long> longList;
    Boolean loaded = false;


    public UserStoriesFragment() {
        // Required empty public constructor
    }


    public static UserStoriesFragment newInstance(String userId) {
        UserStoriesFragment fragment = new UserStoriesFragment();
        fragment.setUserId(userId);

        return fragment;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_stories, container, false);
        fragmentList = new ArrayList<>();
        viewPager = view.findViewById(R.id.viewPagerUserStories);
        storiesProgressView = view.findViewById(R.id.storiesProgress);
        postController = new PostController();
        longList = new ArrayList<>();


        StoriesProgressView.StoriesListener me = this;
        postController.getActivePostFromUser(userId, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {
                if (resultado.size() > 0){
                    for (Post post : resultado) {
                        longList.add(post.getDurLong() + 1000L);
                        StoryFragment fragment = StoryFragment.newInstance(post);
                        fragmentList.add(fragment);
                    }

                    long[] durations = ArrayUtils.toLongArray(longList);
                    storiesProgressView.setStoriesCount(resultado.size());
                    storiesProgressView.setStoriesCountWithDurations(durations);
                    storiesProgressView.setStoriesListener(me);
                    StoryViewPagerAdapter storyViewPagerAdapter = new StoryViewPagerAdapter(getChildFragmentManager(), fragmentList);
                    viewPager.setAdapter(storyViewPagerAdapter);
                    if (getUserVisibleHint()){
                        startStories();
                    }
                }
            }

        });



        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserStoriesOnFragmentInteractionListener) {
            mListener = (UserStoriesOnFragmentInteractionListener) context;
            containerActivity = (StoriesActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StoryOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onNext() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    @Override
    public void onPrev() {
        if (viewPager.getCurrentItem() < 0 ){
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onComplete() {
        mListener.onuserStoryCompleted();
    }

    public void startStories(){
        storiesProgressView.startStories();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && isVisible()) {
            try {
                startStories();

            }catch (Exception e){

            }

        }
        else {
        }
    }
    public interface UserStoriesOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void userStoriesOnFragmentInteraction(Uri uri);
        void onuserStoryCompleted();

    }
}
