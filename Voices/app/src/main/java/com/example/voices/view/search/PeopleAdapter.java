package com.example.voices.view.search;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voices.R;
import com.example.voices.model.OtherUser;
import com.example.voices.model.User;
import com.google.zxing.client.result.VINParsedResult;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Juan on 29/05/2018.
 */

public class PeopleAdapter  extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>{

    // Store a member variable for the contacts
    private List<OtherUser> otherUsers;
    // Store the context for easy access
    private Context mContext;

    private User user;


    private SearchPeopleInteractionListener searchPeopleInteractionListener;

    public PeopleAdapter(List<OtherUser> otherUsers, Context mContext, User user, SearchPeopleInteractionListener searchPeopleInteractionListener) {
        this.otherUsers = otherUsers;
        this.mContext = mContext;
        this.user = user;
        this.searchPeopleInteractionListener = searchPeopleInteractionListener;
    }


    public void setOtherUsers(List<OtherUser> otherUsers) {
        this.otherUsers = otherUsers;
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_people_cell, parent, false);

        PeopleViewHolder peopleViewHolder = new PeopleViewHolder(itemView);
        return peopleViewHolder;
    }


    @Override
    public void onBindViewHolder(PeopleViewHolder holder, int position) {
        OtherUser otherUser = otherUsers.get(position);
        PeopleViewHolder peopleViewHolder = (PeopleViewHolder) holder;
        peopleViewHolder.bindPeople(otherUser, mContext);
    }

    @Override
    public int getItemCount() {
        return otherUsers.size();
    }


    public class PeopleViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewName;
        private CircleImageView imageView;
        private Button followButton;
        View view;

        public PeopleViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.name_text_view_cell);
            imageView = itemView.findViewById(R.id.profile_image_cell);
            followButton = itemView.findViewById(R.id.follow_button_cell);
            view = itemView;


        }

        public void bindPeople(final OtherUser otherUser, final Context context){

            textViewName.setText(otherUser.getName());

            Glide.with(context).load(otherUser.getAvatarUriString()).into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchPeopleInteractionListener.onPersonClick(otherUser);
                }
            });

            if (user.getFollowingIds() != null){
                if (user.getFollowingIds().containsKey(otherUser.getId())){
                    followButton.setTextColor(Color.parseColor("#95989A"));
                    followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            searchPeopleInteractionListener.onUnFollowClick(otherUser);
                        }
                    });
                }
                else {
                    followButton.setTextColor(Color.parseColor("#AB3A7A"));
                    followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            searchPeopleInteractionListener.onFollowClick(otherUser);
                        }
                    });
                }
            }else {
                followButton.setTextColor(Color.parseColor("#AB3A7A"));
                followButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchPeopleInteractionListener.onFollowClick(otherUser);
                    }
                });
            }
        }
    }
    public interface SearchPeopleInteractionListener {
        // TODO: Update argument type and name
        void onPersonClick(OtherUser otherUser);
        void onFollowClick(OtherUser otherUser);
        void onUnFollowClick(OtherUser otherUser);
    }

}