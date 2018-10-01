package com.example.voices.view.stories;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.voices.R;
import com.example.voices.controller.PostController;
import com.example.voices.controller.UserController;
import com.example.voices.model.Post;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.example.voices.view.home.people.FollowUserFragment;
import com.example.voices.view.home.people.PostsViewPagerAdapter;
import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StoriesActivity extends AppCompatActivity implements StoryFragment.StoryOnFragmentInteractionListener, UserStoriesFragment.UserStoriesOnFragmentInteractionListener {

    public static final String POSTSCLIKED = "postsClicked";
    public static final String USERIDCLIKED = "userIdClicked";
    public static final String USER = "user";
    public static final String POSITION = "posiion";

    int position;
    User user;
    ViewPager viewPager;
    List<Fragment> fragmentList;
    PostController postController;
    UserController userController;
    RxAudioPlayer rxAudioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        fragmentList = new ArrayList<>();
        postController = new PostController();
        userController = new UserController();
        rxAudioPlayer = RxAudioPlayer.getInstance();

        //Obtener el Intent
        Intent unIntent = getIntent();

        //Obtener el Bundle
        Bundle unBundle = unIntent.getExtras();

        //Obtener el mensaje

        String userIdStart = unBundle.getString(USERIDCLIKED);
        String positionSting = unBundle.getString(POSITION);
        position = Integer.parseInt(positionSting);
        Gson gson = new Gson();
        user = gson.fromJson(unBundle.getString(USER), User.class);

        viewPager = findViewById(R.id.viewPagerActivityStories);


        userController.getFollowing(user.getId(), new ResultListener<List<User>>() {
            @Override
            public void finish(List<User> resultado) {
                for (User user1 : resultado) {
                    if (user1.getLastActive() > System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)){
                        UserStoriesFragment fragment = UserStoriesFragment.newInstance(user1.getId());
                        fragmentList.add(fragment);
                    }
                }

                PostsViewPagerAdapter postsViewPagerAdapter = new PostsViewPagerAdapter(getSupportFragmentManager(), fragmentList);
                viewPager.setAdapter(postsViewPagerAdapter);
                viewPager.setCurrentItem(getUserPosition(resultado, userIdStart));
            }
        });

    }

    @Override
    public void storyOnFragmentInteraction(Uri uri) {

    }

    @Override
    public void playPost(Post post) {
        postController.getAudioFromURL(post.getAudioUrl(), new ResultListener<File>() {
            @Override
            public void finish(File resultado) {
                rxAudioPlayer.play(PlayConfig.file(resultado).looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {


                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!

                            }
                        });
            }
        });
    }

    @Override
    public void userStoriesOnFragmentInteraction(Uri uri) {

    }

    @Override
    public void onuserStoryCompleted() {
        if (viewPager.getCurrentItem() < fragmentList.size() - 1){
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
        else {
            finish();
        }
    }

    private int getUserPosition(List<User> userList, String id){
        int pos = 0;
        for (int i = 0; i < userList.size(); i++){
            if(userList.get(i).getId().equals(id)){
                pos = i;
                break;
            }
        }
        return pos;
    }
}
