package com.example.voices.view.direct;

import android.content.Context;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.voices.R;
import com.example.voices.controller.ConversationController;
import com.example.voices.model.Conversation;
import com.example.voices.util.ResultListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DirectFragment extends Fragment implements ConversationAdapter.ConversationInteractionListener {


    private DirecOnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    ConversationAdapter conversationAdapter;
    ImageButton imageButton;
    List<Conversation> conversations;
    ConversationController conversationController;
    public DirectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_direct, container, false);
        imageButton = view.findViewById(R.id.button_new_message);
        recyclerView = view.findViewById(R.id.recycler_view_conversations);
        conversations = new ArrayList<>();
        conversationController = new ConversationController();
        conversationAdapter = new ConversationAdapter(this, conversations, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(conversationAdapter);

        conversationController.getConversationsRealtimeWithUSerID(FirebaseAuth.getInstance().getUid(), new ResultListener<List<Conversation>>() {
            @Override
            public void finish(List<Conversation> resultado) {
                conversations.clear();
                conversations.addAll(resultado);
                conversationAdapter.notifyDataSetChanged();
                mListener.updateConvList(resultado);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNewMessageButtonClick();
            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.direcOnFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DirecOnFragmentInteractionListener) {
            mListener = (DirecOnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DirecOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        conversationController.removeConversationListenerRegistration();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMicClick() {

    }

    @Override
    public void onConversationClick(Conversation conversation) {
        mListener.onConvClick(conversation);
    }

    public interface DirecOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void direcOnFragmentInteraction(Uri uri);
        void onNewMessageButtonClick();
        void updateConvList(List<Conversation> conversations);
        void onConvClick(Conversation conversation);
    }
}
