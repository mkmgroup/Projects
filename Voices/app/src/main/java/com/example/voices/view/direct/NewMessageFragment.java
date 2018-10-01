package com.example.voices.view.direct;

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
import com.example.voices.view.upload.SelectUserAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class NewMessageFragment extends Fragment implements SelectUserAdapter.SelectUsersRecylerListener{


    private NewMessageOnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    SelectUserAdapter selectUserAdapter;
    List<OtherUser> peopleList;
    UserController userController;
    EditText editText;
    User theUser;
    MainActivity containerActtivity;
    List<OtherUser> suggestedList;

    public NewMessageFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_message, container, false);

        editText = view.findViewById(R.id.edit_text_new_message);
        userController = new UserController();
        recyclerView = view.findViewById(R.id.recycler_view_new_message);
        peopleList = new ArrayList<>();
        suggestedList = new ArrayList<>();
        selectUserAdapter = new SelectUserAdapter(peopleList, getContext(),  this);
        recyclerView.setAdapter(selectUserAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(selectUserAdapter);
        userController.getSuggestedUsers(FirebaseAuth.getInstance().getUid(), new ResultListener<List<OtherUser>>() {
            @Override
            public void finish(List<OtherUser> resultado) {
                suggestedList.clear();
                suggestedList.addAll(resultado);
                peopleList.clear();
                peopleList.addAll(resultado);
                selectUserAdapter.notifyDataSetChanged();
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
                            selectUserAdapter.notifyDataSetChanged();
                        }
                    });
                }else {
                    peopleList.clear();
                    peopleList.addAll(suggestedList);
                    selectUserAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewMessageOnFragmentInteractionListener) {
            mListener = (NewMessageOnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NewMessageOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void userclicked(OtherUser otherUser) {
        mListener.newMessageOnFragmentInteraction(otherUser);
    }


    public interface NewMessageOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void newMessageOnFragmentInteraction(OtherUser otherUser);
    }
}
