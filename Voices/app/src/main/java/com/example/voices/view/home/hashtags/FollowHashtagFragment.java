package com.example.voices.view.home.hashtags;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voices.R;
import com.example.voices.controller.PostController;
import com.example.voices.model.Hashtag;
import com.example.voices.model.Post;
import com.example.voices.util.ResultListener;
import com.example.voices.view.MainActivity;

import java.util.List;


public class FollowHashtagFragment extends Fragment {

    private String hashtag;
    private FollowHashOnFragmentInteractionListener mListener;
    private MainActivity containerActivity;

    private ImageView imageView;
    private TextView textViewHashtag;
    private TextView postsCount;
    private ImageView waveImageView;
    HashtagsFragment hashtagsFragment;

    PostController postController;

    public FollowHashtagFragment() {
        // Required empty public constructor
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public void setHashtagsFragment(HashtagsFragment hashtagsFragment) {
        this.hashtagsFragment = hashtagsFragment;
    }

    public static FollowHashtagFragment newInstance(String hashtag, Fragment hashFragment) {
        FollowHashtagFragment fragment = new FollowHashtagFragment();
        HashtagsFragment hashtagsFragment = (HashtagsFragment) hashFragment;
        fragment.setHashtagsFragment(hashtagsFragment);
        fragment.setHashtag(hashtag);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow_hashtag, container, false);

        PostController postController = new PostController();

        imageView = view.findViewById(R.id.image_view_follow_hashtag_fragment);
        textViewHashtag = view.findViewById(R.id.text_hashtag_fragment_follow_hashtag);
        postsCount = view.findViewById(R.id.posts_fragment_follow_hashtag);
        waveImageView = view.findViewById(R.id.wave_follow_hashtag_fragment);

        textViewHashtag.setText("#" + hashtag);

        postController.getActivePostsFromHashtag(hashtag, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {
                postsCount.setText(Integer.toString(resultado.size()));
                if (resultado.size() > 0){
                    Glide.with(containerActivity).load(resultado.get(0).getImageURl()).into(imageView);
                    com.github.florent37.viewanimator.ViewAnimator.animate(view).pulse().duration(2000).repeatCount(android.view.animation.Animation.INFINITE).start();

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hashtagsFragment.onHashClicked(hashtag);
                        }
                    });

                }else {
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setImageResource(R.drawable.ic_sleepy);
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    imageView.setColorFilter(filter);
                    waveImageView.setVisibility(View.INVISIBLE);
                }
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FollowHashOnFragmentInteractionListener) {
            mListener = (FollowHashOnFragmentInteractionListener) context;
            containerActivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FollowHashOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface FollowHashOnFragmentInteractionListener {

        void followHashOnFragmentInteraction(Uri uri);
    }
}
