package com.example.voices.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.voices.R;
import com.example.voices.controller.PostController;
import com.example.voices.model.Post;
import com.example.voices.util.ResultListener;
import com.example.voices.view.home.HomeTabsFragment;
import com.example.voices.view.stories.StoryFragment;
import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.google.android.gms.common.util.ArrayUtils;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryFromLinkActivity extends AppCompatActivity implements StoryFragment.StoryOnFragmentInteractionListener,
        StoriesProgressView.StoriesListener{

    PostController postController;
    RxAudioPlayer rxAudioPlayer;
    List<Long> longList;
    StoriesProgressView storiesProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxAudioPlayer = RxAudioPlayer.getInstance();

        setContentView(R.layout.activity_story_from_link);
        postController = new PostController();
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        String postId = appLinkData.getLastPathSegment();

        storiesProgressView = findViewById(R.id.storiesProgressActivityFromLink);

        longList = new ArrayList<>();

        StoriesProgressView.StoriesListener me = this;


        postController.getAPostWithID(postId, new ResultListener<Post>() {
            @Override
            public void finish(Post resultado) {
                longList.add(resultado.getDurLong() + 1000L);
                Fragment nuevoFragment = StoryFragment.newInstance(resultado);
                FragmentManager unFragmentManager = getSupportFragmentManager();
                FragmentTransaction unFragmentTransaction = unFragmentManager.beginTransaction();
                unFragmentTransaction.replace(R.id.story_container, nuevoFragment, "STORY");
        /*unFragmentTransaction.commit();*/
                unFragmentTransaction.commitAllowingStateLoss();
                long[] durations = ArrayUtils.toLongArray(longList);
                storiesProgressView.setStoriesCount(1);
                storiesProgressView.setStoriesCountWithDurations(durations);
                storiesProgressView.setStoriesListener(me);
                storiesProgressView.startStories();
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
    public void onNext() {

    }

    @Override
    public void onPrev() {

    }

    @Override
    public void onComplete() {
        finish();
    }
}
