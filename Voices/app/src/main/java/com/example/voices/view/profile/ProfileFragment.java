package com.example.voices.view.profile;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.usb.UsbRequest;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.voices.R;
import com.example.voices.controller.PostController;
import com.example.voices.controller.UserController;
import com.example.voices.model.Post;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.example.voices.view.MainActivity;
import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ProfileFragment extends Fragment {

    UserController userController;
    PostController postController;
    private ProfileOnFragmentInteractionListener mListener;
    private String fromUserId;
    FloatingActionButton followButton;
    ImageView userProfileImage;
    TextView textViewUserName;
    TextView textViewFollowers;
    TextView textViewFollowing;
    TextView textViewPosts;
    TextView textViewPoints;
    RecyclerView recyclerView;
    List<Post> posts;
    RxAudioPlayer rxAudioPlayer;
    MainActivity mainActivity;
    PostsProfileAdapter postsProfileAdapter;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment getProfileFragment(String fromUserId){
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setFromUserId(fromUserId);
        return profileFragment;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        rxAudioPlayer = RxAudioPlayer.getInstance();
        posts = new ArrayList<>();
        userController = new UserController();
        postController = new PostController();
        followButton = view.findViewById(R.id.floatingActionButtonProfileFollow);
        userProfileImage = view.findViewById(R.id.profile_image_view);
        textViewUserName = view.findViewById(R.id.profile_user_name);
        textViewFollowers = view.findViewById(R.id.followersCountProfile);
        textViewFollowing = view.findViewById(R.id.followingCountProfile);
        textViewPosts = view.findViewById(R.id.postsCountProfile);
        textViewPoints = view.findViewById(R.id.points_count_profile);
        recyclerView = view.findViewById(R.id.profilePostRecycler);
        Fragment me = this;
        postsProfileAdapter = new PostsProfileAdapter(posts, getContext(), this);
        recyclerView.setAdapter(postsProfileAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);



        if (mainActivity.user.getFollowingIds() != null){
            if (mainActivity.user.getFollowingIds().containsKey(fromUserId)){
                followButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.follow_profile_followed));
                followButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.follow_profile_followed)));

            }
            else {

            }
        }else {
            followButton.setBackgroundColor(Color.parseColor("#AB3A7A"));

        }

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainActivity.user.getFollowingIds().containsKey(fromUserId)){

                }
                else {
                    mainActivity.user.getFollowingIds().put(fromUserId, true);
                    followButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.follow_profile_followed));
                    followButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.follow_profile_followed)));
                    userController.followUser(fromUserId, FirebaseAuth.getInstance().getUid(), new ResultListener<Boolean>() {
                        @Override
                        public void finish(Boolean resultado) {

                        }
                    });


                }
            }
        });

        userController.getUser(fromUserId, new ResultListener<User>() {
            @Override
            public void finish(User resultado) {
                Glide.with(getContext()).load(resultado.getAvatarUrl()).into(userProfileImage);
                textViewUserName.setText(resultado.getNickname());
                textViewFollowing.setText(Integer.toString(resultado.getFollowingIds().size() - 1));
            }

        });
        userController.getFollowers(fromUserId, new ResultListener<List<User>>() {
            @Override
            public void finish(List<User> resultado) {
                textViewFollowers.setText(Integer.toString(resultado.size() - 1));



            }
        });
        postController.getPostsWithID(fromUserId, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {
                textViewPosts.setText(Integer.toString(resultado.size()));
                posts.clear();
                posts.addAll(resultado);
                postsProfileAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileOnFragmentInteractionListener) {
            mListener = (ProfileOnFragmentInteractionListener) context;
            mainActivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProfileOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ProfileOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void profileOnFragmentInteraction(Uri uri);
    }
    public void playPost(Post post, AVLoadingIndicatorView avLoadingIndicatorView){
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
