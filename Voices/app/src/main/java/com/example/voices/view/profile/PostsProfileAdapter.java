package com.example.voices.view.profile;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voices.R;
import com.example.voices.model.OtherUser;
import com.example.voices.model.Post;
import com.example.voices.model.User;
import com.example.voices.view.search.PeopleAdapter;
import com.example.voices.view.search.SearchPeopleFragment;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Juan on 05/06/2018.
 */

public class PostsProfileAdapter extends RecyclerView.Adapter<PostsProfileAdapter.PostProfileViewHolder>{


    // Store a member variable for the contacts
    private List<Post> posts;
    // Store the context for easy access
    private Context mContext;




    private ProfileFragment fragmentContainer;

    public PostsProfileAdapter(List<Post> posts, Context mContext, ProfileFragment fragmentContainer) {
        this.posts = posts;
        this.mContext = mContext;

        this.fragmentContainer = fragmentContainer;
    }

    @NonNull
    @Override
    public PostProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_profile_cell, parent, false);

        PostProfileViewHolder postProfileViewHolder = new PostProfileViewHolder(itemView);
        return postProfileViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostProfileViewHolder holder, int position) {
        Post post = posts.get(position);
        PostProfileViewHolder postProfileViewHolder = (PostProfileViewHolder) holder;
        postProfileViewHolder.bindPost(post, mContext);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class PostProfileViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewHash;
        private TextView textViewDuration;
        private TextView textViewLikes;
        private TextView textViewDate;
        private FloatingActionButton playButton;
        private AVLoadingIndicatorView avi;



        public PostProfileViewHolder(View itemView) {
            super(itemView);

            textViewHash = itemView.findViewById(R.id.profile_post_cell_hashtag_text_view);
            textViewDuration = itemView.findViewById(R.id.profile_post_cell_duration_text_view);
            textViewLikes = itemView.findViewById(R.id.profile_post_cell_likes_text_view);
            textViewDate = itemView.findViewById(R.id.profile_post_cell_date_text_view);
            playButton = itemView.findViewById(R.id.floatingActionButtonPostProfileCell);
            avi = itemView.findViewById(R.id.playPostAviProfile);
        }

        public void bindPost(Post post, final Context context){
            String date = "";
            String orderDay = (String) DateFormat.format("dd", post.getDate());
            String orderMonth = (String) DateFormat.format("MM", post.getDate());
            String orderYear = (String) DateFormat.format("yyyy", post.getDate());
            date = orderDay + "/" + orderMonth + "/" + orderYear;

            String hashes = "";
            long i = 0;
            for (Map.Entry<String, Long> hash : post.getHashtags().entrySet()) {
                if (i == 0){
                    hashes = hashes + hash.getKey();
                }else {
                    hashes = hashes + " " + hash.getKey();
                }
                i++;
            }

            textViewHash.setText(hashes);

            textViewDuration.setText(post.getDuration());

            textViewDate.setText(date);

            textViewLikes.setText(Integer.toString(post.getLikes().size()));

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentContainer.playPost(post, avi);
                }
            });



        }
    }
}
