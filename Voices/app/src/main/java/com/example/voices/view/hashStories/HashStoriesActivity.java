package com.example.voices.view.hashStories;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.voices.R;
import com.example.voices.controller.PostController;
import com.example.voices.model.Post;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.example.voices.view.stories.StoryFragment;
import com.example.voices.view.stories.StoryViewPagerAdapter;
import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class HashStoriesActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener, StoryFragment.StoryOnFragmentInteractionListener {

    public static final String USER = "user";
    public static final String HASHTAG = "hashtag";
    User user;
    RxAudioPlayer rxAudioPlayer;
    ViewPager viewPager;
    StoriesProgressView storiesProgressView;
    PostController postController;
    List<Fragment> fragmentList;
    List<Long> longList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_stories);

        rxAudioPlayer = RxAudioPlayer.getInstance();


        //Obtener el Intent
        Intent unIntent = getIntent();

        //Obtener el Bundle
        Bundle unBundle = unIntent.getExtras();

        //Obtener el mensaje

        String hashtag = unBundle.getString(HASHTAG);

        Gson gson = new Gson();
        user = gson.fromJson(unBundle.getString(USER), User.class);

        fragmentList = new ArrayList<>();
        viewPager = findViewById(R.id.viewPagerHashStories);
        storiesProgressView = findViewById(R.id.storiesProgressHash);
        postController = new PostController();
        longList = new ArrayList<>();

        StoriesProgressView.StoriesListener me = this;
        postController.getActivePostsFromHashtag(hashtag, new ResultListener<List<Post>>() {
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
                    StoryViewPagerAdapter storyViewPagerAdapter = new StoryViewPagerAdapter(getSupportFragmentManager(), fragmentList);
                    viewPager.setAdapter(storyViewPagerAdapter);
                    storiesProgressView.startStories();
                }
            }

        });



    }

    @Override
    public void onNext() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    @Override
    public void onPrev() {

    }

    @Override
    public void onComplete() {
        finish();
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
}
