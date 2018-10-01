package com.example.voices.view.upload;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.voices.R;
import com.example.voices.model.OtherUser;
import com.example.voices.model.User;
import com.example.voices.view.search.PeopleAdapter;
import com.example.voices.view.search.SearchPeopleFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Juan on 18/06/2018.
 */

public class SelectUserAdapter extends RecyclerView.Adapter<SelectUserAdapter.UserViewHolder>{

    // Store a member variable for the contacts
    private List<OtherUser> otherUsers;
    // Store the context for easy access
    private Context mContext;


    private SelectUsersRecylerListener selectUsersRecylerListener;


    public SelectUserAdapter(List<OtherUser> otherUsers, Context mContext, SelectUsersRecylerListener selectUsersRecylerListener) {
        this.otherUsers = otherUsers;
        this.mContext = mContext;
        this.selectUsersRecylerListener = selectUsersRecylerListener;
    }


    public void setOtherUsers(List<OtherUser> otherUsers) {
        this.otherUsers = otherUsers;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_people_cell, parent, false);

        UserViewHolder userViewHolder = new UserViewHolder(itemView);
        return userViewHolder;
    }


    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        OtherUser otherUser = otherUsers.get(position);
        UserViewHolder userViewHolder = (UserViewHolder) holder;
        userViewHolder.bindPeople(otherUser, mContext);
    }

    @Override
    public int getItemCount() {
        return otherUsers.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewName;
        private CircleImageView imageView;
        View view;

        public UserViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.name_text_view_cell_select);
            imageView = itemView.findViewById(R.id.profile_image_cell_select);
            view = itemView;


        }

        public void bindPeople(final OtherUser otherUser, final Context context){

            textViewName.setText(otherUser.getName());

            Glide.with(context).load(otherUser.getAvatarUriString()).into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectUsersRecylerListener.userclicked(otherUser);
                }
            });
        }
    }

    public interface SelectUsersRecylerListener {
        // TODO: Update argument type and name
        void userclicked(OtherUser otherUser);
    }

}