package com.example.voices.view.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.voices.R;
import com.example.voices.controller.HashtagContoller;
import com.example.voices.controller.PostController;
import com.example.voices.controller.UserController;
import com.example.voices.model.Hashtag;
import com.example.voices.model.OtherUser;
import com.example.voices.model.Post;
import com.example.voices.model.PostAlgolia;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.example.voices.view.MainActivity;
import com.example.voices.view.StoryFromLinkActivity;
import com.example.voices.view.hashStories.HashStoriesActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class SearchHashtagsFragment extends Fragment {


    private SearchHashtagsOnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    HashtagAdapter hashtagAdapter;
    List<PostAlgolia> posts;
    PostController postController;
    HashtagContoller hashtagContoller;
    EditText editTextSearchHash;
    List<PostAlgolia> suggestedList;
    Button buttonFollowHash;
    MainActivity containerActtivity;
    UserController userController;
    public SearchHashtagsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_search_hashtags, container, false);
        postController = new PostController();
        userController = new UserController();
        hashtagContoller = new HashtagContoller();
        buttonFollowHash = view.findViewById(R.id.follow_button_hashtag);
        editTextSearchHash = view.findViewById(R.id.edit_text_search_hashtag);
        recyclerView = view.findViewById(R.id.recycler_view_search_hashtags);
        posts = new ArrayList<>();
        suggestedList = new ArrayList<>();
        User user = new User();
        hashtagAdapter = new HashtagAdapter(posts, getContext(), user, this);
        recyclerView.setAdapter(hashtagAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(hashtagAdapter);

        postController.getSuggestedPostsAlgolia(FirebaseAuth.getInstance().getUid(), new ResultListener<List<PostAlgolia>>() {
            @Override
            public void finish(List<PostAlgolia> resultado) {
                suggestedList.clear();
                suggestedList.addAll(resultado);
                posts.clear();
                posts.addAll(resultado);
                hashtagAdapter.notifyDataSetChanged();
            }
        });



        editTextSearchHash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    posts.clear();
                    posts.addAll(suggestedList);
                    hashtagAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")){


                    postController.getPostAlgoliaWithHashtag(editable.toString(), new ResultListener<List<PostAlgolia>>() {
                        @Override
                        public void finish(List<PostAlgolia> resultado) {
                            posts.clear();
                            posts.addAll(resultado);
                            hashtagAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        buttonFollowHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextSearchHash.getText().toString().equals("")){
                    if (!containerActtivity.user.getFollowingHashes().containsKey(editTextSearchHash.getText().toString())){
                        userController.followHashtag(editTextSearchHash.getText().toString(), FirebaseAuth.getInstance().getUid(), new ResultListener<Boolean>() {
                            @Override
                            public void finish(Boolean resultado) {
                                if (resultado){
                                    Toast.makeText(getContext(), "agregado", Toast.LENGTH_SHORT).show();
                                    containerActtivity.user.getFollowingHashes().put(editTextSearchHash.getText().toString(), true);
                                }
                            }
                        });
                    }
                }
            }
        });






       return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchHashtagsOnFragmentInteractionListener) {
            mListener = (SearchHashtagsOnFragmentInteractionListener) context;
            containerActtivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchHashtagsOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onClickCell(String postID){
        Intent intent = new Intent(getContext(), StoryFromLinkActivity.class);
        Uri myUri = Uri.parse("http://www.qchapp.com/post/" + postID);
        intent.setData(myUri);
        startActivity(intent);
    }

    public interface SearchHashtagsOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void SearchHashtagsOnFragmentInteraction(Uri uri);
    }
    public void onFollowClick(Hashtag hashtag){

    }
}
