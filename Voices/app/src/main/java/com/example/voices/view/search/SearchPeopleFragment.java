package com.example.voices.view.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.voices.R;
import com.example.voices.controller.UserController;
import com.example.voices.model.OtherUser;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.example.voices.view.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class SearchPeopleFragment extends Fragment implements PeopleAdapter.SearchPeopleInteractionListener{

    private SearchPeopleOnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    PeopleAdapter peopleAdapter;
    List<OtherUser> peopleList;
    UserController userController;
    EditText editText;
    User theUser;
    MainActivity containerActtivity;
    List<OtherUser> suggestedList;

    public SearchPeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seatch_people, container, false);
        editText = view.findViewById(R.id.edit_text_search_people);
        userController = new UserController();
        recyclerView = view.findViewById(R.id.recycler_view_search_people);
        peopleList = new ArrayList<>();
        suggestedList = new ArrayList<>();
        Fragment fragment = this;
        peopleAdapter = new PeopleAdapter(peopleList, getContext(), containerActtivity.user , this);
        recyclerView.setAdapter(peopleAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(peopleAdapter);
        userController.getSuggestedUsers(containerActtivity.user.getId(), new ResultListener<List<OtherUser>>() {
            @Override
            public void finish(List<OtherUser> resultado) {
                suggestedList.clear();
                suggestedList.addAll(resultado);
                peopleList.clear();
                peopleList.addAll(resultado);
                peopleAdapter.notifyDataSetChanged();
            }
        });




        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")){
                    userController.searchOtherUsers(editable.toString(), new ResultListener<List<OtherUser>>() {
                        @Override
                        public void finish(List<OtherUser> resultado) {
                            peopleList.clear();
                            peopleList.addAll(resultado);
                            peopleAdapter.notifyDataSetChanged();
                        }
                    });
                }else {
                    peopleList.clear();
                    peopleList.addAll(suggestedList);
                    peopleAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchPeopleOnFragmentInteractionListener) {
            mListener = (SearchPeopleOnFragmentInteractionListener) context;
            containerActtivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement s");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface SearchPeopleOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void searchPeopleOnFragmentInteractionPersonClicked(OtherUser otherUser);
        void searchPeopleOnFragmentInteractionFollowClick(OtherUser otherUser);
        void searchPeopleOnFragmentInteractionUnfollowClick(OtherUser otherUser);

    }

    @Override
    public void onPersonClick(OtherUser otherUser) {
        mListener.searchPeopleOnFragmentInteractionPersonClicked(otherUser);
    }
    @Override
    public void onFollowClick(OtherUser user){
        peopleAdapter.notifyDataSetChanged();
        mListener.searchPeopleOnFragmentInteractionFollowClick(user);
    }
    @Override
    public void onUnFollowClick(OtherUser user){
        mListener.searchPeopleOnFragmentInteractionUnfollowClick(user);
    }


}
