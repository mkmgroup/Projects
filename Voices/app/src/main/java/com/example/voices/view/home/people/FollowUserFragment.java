package com.example.voices.view.home.people;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import com.example.voices.controller.PostController;
import com.example.voices.controller.UserController;
import com.example.voices.model.Post;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.example.voices.view.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FollowUserFragment extends Fragment {

    private FollowUserOnFragmentInteractionListener mListener;

    private ImageView imageView;
    private TextView textViewUserName;
    private TextView hashtags;
    private TextView date;
    private TextView likesCount;
    private ImageView waveImageView;

    List<Post> posts;
    String userId;
    HomeFragment homeFragment;

    PostController postController;
    UserController userController;

    public FollowUserFragment() {
        // Required empty public constructor
    }


    public static FollowUserFragment newInstance(String userID, Fragment homeFrag) {
        FollowUserFragment fragment = new FollowUserFragment();
        fragment.setUserId(userID);
        fragment.setHomeFragment((HomeFragment) homeFrag);
        return fragment;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_follow_user, container, false);

        posts = new ArrayList<>();

        userController = new UserController();
        postController = new PostController();


        imageView = view.findViewById(R.id.image_view_follow_user_fragment);
        textViewUserName = view.findViewById(R.id.text_user_name_fragment_follow_user);
        hashtags = view.findViewById(R.id.text_hashtags_fragment_follow_user);
        date = view.findViewById(R.id.text_date_fragment_follow_user);
        likesCount = view.findViewById(R.id.likes_fragment_follow_user);
        waveImageView = view.findViewById(R.id.wave_follow_user_fragment);



        userController.getUser(userId, new ResultListener<User>() {
            @Override
            public void finish(User resultado) {
                textViewUserName.setText(resultado.getNickname());
                if(isAdded()){
                    Glide.with(getContext()).load(resultado.getAvatarUrl()).into(imageView);
                }
            }
        });
        postController.getActivePostFromUser(userId, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {
                posts = resultado;
                if (resultado.size() > 0){

                    String hashes = "";
                    long j = 0;
                    for (Post post : resultado) {
                        long i = 0;
                        for (Map.Entry<String, Long> hash : post.getHashtags().entrySet()) {
                            if (hashes.equals("")){
                                hashes = hashes + "#" + hash.getKey();
                            }else {
                                hashes = hashes + " #" + hash.getKey();
                            }
                            i++;
                        }
                        j++;
                    }
                    hashtags.setText(hashes);
                    /*String dateString = "";
                    String orderDay = (String) DateFormat.format("dd", resultado.get(0).getDate());
                    String orderMonth = (String) DateFormat.format("MM", resultado.get(0).getDate());
                    String orderYear = (String) DateFormat.format("yyyy", resultado.get(0).getDate());
                    dateString = orderDay + "/" + orderMonth + "/" + orderYear;*/
                    date.setText("");
                    likesCount.setText(Integer.toString(resultado.size()));
                    com.github.florent37.viewanimator.ViewAnimator.animate(view).pulse().duration(2000).repeatCount(android.view.animation.Animation.INFINITE).start();

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            homeFragment.onPostClicked(posts, userId);
                        }
                    });


                }
                else {
                    hashtags.setText("");
                    date.setText("");
                    likesCount.setText("0");
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
        if (context instanceof FollowUserOnFragmentInteractionListener) {
            mListener = (FollowUserOnFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FollowUserOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface FollowUserOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void followUserOnFragmentInteraction(List<Post> posts, String userId);
    }
}
