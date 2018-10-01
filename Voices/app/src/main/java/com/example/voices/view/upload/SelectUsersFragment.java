package com.example.voices.view.upload;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.voices.R;
import com.example.voices.controller.UserController;
import com.example.voices.model.Message;
import com.example.voices.model.OtherUser;
import com.example.voices.util.ResultListener;
import com.example.voices.view.search.PeopleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.List;


public class SelectUsersFragment extends Fragment implements SelectUserAdapter.SelectUsersRecylerListener{

    private SelectUsersFragmentInteractionListener mListener;
    UploadActivity uploadActivity;
    ChipsInput userSelect;
    UserController userController;
    List<OtherUser> otherUsers;
    List<OtherUser> suggestedList;
    RecyclerView recyclerView;
    SelectUserAdapter selectUserAdapter;
    public SelectUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_select_users, container, false);
        userController = new UserController();
        Button buttonSelect = view.findViewById(R.id.select_users_button);
        userSelect = view.findViewById(R.id.tags_users_input_select);
        recyclerView = view.findViewById(R.id.recycler_view_select_users);

        otherUsers = new ArrayList<>();
        suggestedList = new ArrayList<>();

        Fragment fragment = this;
        selectUserAdapter = new SelectUserAdapter(otherUsers, getContext(), this);
        recyclerView.setAdapter(selectUserAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(selectUserAdapter);


        userController.getSuggestedUsers(FirebaseAuth.getInstance().getUid(), new ResultListener<List<OtherUser>>() {
            @Override
            public void finish(List<OtherUser> resultado) {
                suggestedList.clear();
                suggestedList.addAll(resultado);
                otherUsers.clear();
                otherUsers.addAll(resultado);
                selectUserAdapter.notifyDataSetChanged();
            }
        });

        userSelect.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chipInterface, int i) {

            }

            @Override
            public void onChipRemoved(ChipInterface chipInterface, int i) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence) {
                userController.searchOtherUsers(charSequence.toString(), new ResultListener<List<OtherUser>>() {
                    @Override
                    public void finish(List<OtherUser> resultado) {

                        if (!charSequence.toString().equals("")){
                            userController.searchOtherUsers(charSequence.toString(), new ResultListener<List<OtherUser>>() {
                                @Override
                                public void finish(List<OtherUser> resultado) {
                                    otherUsers.clear();
                                    otherUsers.addAll(resultado);
                                    selectUserAdapter.notifyDataSetChanged();
                                }
                            });
                        }else {
                            otherUsers.clear();
                            otherUsers.addAll(suggestedList);
                            selectUserAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectUsers();
            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.selectUserFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectUsersFragmentInteractionListener) {
            mListener = (SelectUsersFragmentInteractionListener) context;
            uploadActivity = (UploadActivity) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SelectLocationsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void userclicked(OtherUser otherUser) {
        userSelect.addChip(otherUser);
    }

    public interface SelectUsersFragmentInteractionListener {
        // TODO: Update argument type and name
        void selectUserFragmentInteraction(Uri uri);
        void onUsersSelected();
    }

    public void onSelectUsers(){
        uploadActivity.usersSelected = (List<OtherUser>) userSelect.getSelectedChipList();
        mListener.onUsersSelected();
    }


}
