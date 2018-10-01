package com.example.voices.view.home.people;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voices.R;
import com.example.voices.controller.UserController;
import com.example.voices.model.Post;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.example.voices.view.MainActivity;

import java.util.Map;


public class PostFragment extends Fragment {

    private Post post;

    private PostFragmentOnFragmentInteractionListener mListener;
    private ImageView imageView;
    private TextView textViewUserName;
    private TextView hashtags;
    private TextView date;
    private TextView likesCount;
    private TextView duration;
    MainActivity containerActivity;
    UserController userController;
    public PostFragment() {
        // Required empty public constructor
    }


    public static PostFragment newInstance(Post post) {
        PostFragment fragment = new PostFragment();
        fragment.setPost(post);
        return fragment;
    }

    public void setPost(Post post) {
        this.post = post;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        imageView = view.findViewById(R.id.image_view_post_fragment);
        textViewUserName = view.findViewById(R.id.text_user_name_fragment_post);
        hashtags = view.findViewById(R.id.text_hashtags_fragment_post);
        date = view.findViewById(R.id.text_date_fragment_post);
        likesCount = view.findViewById(R.id.likes_fragment_post);
        duration = view.findViewById(R.id.text_duration_fragment_post);

        userController = new UserController();

        Glide.with(containerActivity).load(post.getImageURl()).into(imageView);
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

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PostFragmentOnFragmentInteractionListener) {
            mListener = (PostFragmentOnFragmentInteractionListener) context;
            containerActivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PostFragmentOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface PostFragmentOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void postFragmentOnFragmentInteraction(Post post);
    }
}
