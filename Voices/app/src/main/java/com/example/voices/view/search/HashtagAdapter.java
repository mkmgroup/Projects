package com.example.voices.view.search;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voices.R;
import com.example.voices.controller.UserController;
import com.example.voices.model.Post;
import com.example.voices.model.PostAlgolia;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Juan on 30/05/2018.
 */

public class HashtagAdapter extends RecyclerView.Adapter<HashtagAdapter.HashtagViewHolder>{

    // Store a member variable for the contacts
    private List<PostAlgolia> posts;
    // Store the context for easy access
    private Context mContext;

    private User user;

    private SearchHashtagsFragment fragmentContainer;

    public HashtagAdapter(List<PostAlgolia> posts, Context mContext, User user, Fragment fragmentContainer) {
        this.posts = posts;
        this.mContext = mContext;
        this.user = user;
        this.fragmentContainer = (SearchHashtagsFragment) fragmentContainer;
    }


    @Override
    public HashtagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_hashtag_cell, parent, false);

        HashtagViewHolder hashtagViewHolder = new HashtagViewHolder(itemView);
        return hashtagViewHolder;
    }


    @Override
    public void onBindViewHolder(HashtagViewHolder holder, int position) {
        PostAlgolia post = posts.get(position);
        HashtagViewHolder hashtagViewHolder = (HashtagViewHolder) holder;
        hashtagViewHolder.bindPost(post, mContext);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class HashtagViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewName;
        private CircleImageView imageView;

        private TextView textViewUserName;

        public HashtagViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.hash_name_text_view_cell);
            imageView = itemView.findViewById(R.id.hash_image_cell);

            textViewUserName = itemView.findViewById(R.id.hash_username_cell);
        }

        public void bindPost(final PostAlgolia post, final Context context){

            UserController userController = new UserController();
            userController.getUser(post.getUserId(), new ResultListener<User>() {
                @Override
                public void finish(User resultado) {
                    textViewUserName.setText(resultado.getNickname());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentContainer.onClickCell(post.getId());
                }
            });

            String hashes = "";
            long i = 0;

            for (String s : post.getHashtags()) {
                if (i == 0){
                    hashes = hashes + "#" + s;
                }else if (i < 3){
                    hashes = hashes + " #" + s;
                }
                i++;
            }


            textViewName.setText(hashes);


            if (post.getImageURL() == null){
                Glide.with(fragmentContainer).load("https://firebasestorage.googleapis.com/v0/b/voices-ad267.appspot.com/o/dj.png?alt=media&token=dcefe0dd-329d-40dc-b86a-5de04135ddae").into(imageView);
            }
            else {
                Glide.with(fragmentContainer).load(post.getImageURL()).into(imageView);
            }
        }
    }



}