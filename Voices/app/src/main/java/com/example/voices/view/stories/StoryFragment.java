package com.example.voices.view.stories;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voices.R;
import com.example.voices.controller.PostController;
import com.example.voices.controller.UserController;
import com.example.voices.model.Post;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class StoryFragment extends Fragment {


    private StoriesActivity containerActivity;
    private StoryOnFragmentInteractionListener mListener;
    private Post post;

    private ImageView imageView;
    private TextView textViewUserName;
    private TextView hashtags;
    private TextView date;
    private TextView likesCount;
    private TextView duration;

    private LinearLayout like;
    private ImageView likeImage;
    private TextView likeText;
    private LinearLayout comment;
    private ImageView commentImage;
    private TextView commentText;
    private LinearLayout share;
    private ImageView shareImage;
    private TextView shareText;

    UserController userController;
    PostController postController;
    RxAudioPlayer rxAudioPlayer;
    public StoryFragment() {
        // Required empty public constructor
    }


    public static StoryFragment newInstance(Post post) {
        StoryFragment fragment = new StoryFragment();
        fragment.setPost(post);
        return fragment;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);
        userController = new UserController();
        postController = new PostController();
        rxAudioPlayer = RxAudioPlayer.getInstance();
        imageView = view.findViewById(R.id.image_view_story);
        textViewUserName = view.findViewById(R.id.text_user_name_fragment_story);
        hashtags = view.findViewById(R.id.text_hashtags_fragment_story);
        date = view.findViewById(R.id.text_date_fragment_story);
        likesCount = view.findViewById(R.id.likes_fragment_story);
        duration = view.findViewById(R.id.text_duration_story);

        like = view.findViewById(R.id.like_post_story);
        likeImage = view.findViewById(R.id.like_post_story_image);
        likeText = view.findViewById(R.id.like_post_story_text);

        comment = view.findViewById(R.id.comment_post_story);
        commentImage = view.findViewById(R.id.comment_post_story_image);
        commentText = view.findViewById(R.id.comment_post_story_text);

        share = view.findViewById(R.id.share_post_story);
        shareImage = view.findViewById(R.id.share_post_story_image);
        shareText = view.findViewById(R.id.share_post_story_text);


        Glide.with(getContext()).load(post.getImageURl()).into(imageView);
        userController.getUser(post.getUserId(), new ResultListener<User>() {
            @Override
            public void finish(User resultado) {
                textViewUserName.setText(resultado.getNickname());
            }
        });

        String hashes = "";
        long i = 0;
        for (Map.Entry<String, Long> hash : post.getHashtags().entrySet()) {
            if (i == 0){
                hashes = hashes + "#" + hash.getKey();
            }else {
                hashes = hashes + " #" + hash.getKey();
            }
            i++;
        }
        hashtags.setText(hashes);

        String dateString = "";
        String orderDay = (String) DateFormat.format("dd", post.getDate());
        String orderMonth = (String) DateFormat.format("MM", post.getDate());
        String orderYear = (String) DateFormat.format("yyyy", post.getDate());
        dateString = orderDay + "/" + orderMonth + "/" + orderYear;
        date.setText(dateString);
        likesCount.setText(Integer.toString(post.getLikes().size()));
        duration.setText(post.getDuration());

        if (getUserVisibleHint()){
            mListener.playPost(post);
        }

        if (post.getLikes().containsKey(FirebaseAuth.getInstance().getUid())){
            likeImage.setImageResource(R.drawable.ic_microphone_fill);
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!post.getLikes().containsKey(FirebaseAuth.getInstance().getUid())){
                    likeImage.setImageResource(R.drawable.ic_microphone_fill);
                    post.getLikes().put(FirebaseAuth.getInstance().getUid(), System.currentTimeMillis());
                    likesCount.setText(Integer.toString(post.getLikes().size()));
                    postController.likePost(FirebaseAuth.getInstance().getUid(), post, System.currentTimeMillis(), new ResultListener<Boolean>() {
                        @Override
                        public void finish(Boolean resultado) {
                            if (resultado){
                            }
                        }
                    });
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Escucha este audio! " + "http://www.qchapp.com/post/" + post.getId());
                try {
                   startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {

                }
            }
        });


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StoryOnFragmentInteractionListener) {
            mListener = (StoryOnFragmentInteractionListener) context;

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isResumed()){
               mListener.playPost(post);
            }
        }
        else {
        }
    }




    public interface StoryOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void storyOnFragmentInteraction(Uri uri);
        void playPost(Post post);
    }
}
