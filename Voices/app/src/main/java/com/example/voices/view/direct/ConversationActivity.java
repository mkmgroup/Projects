package com.example.voices.view.direct;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;

import com.example.voices.R;
import com.example.voices.controller.ConversationController;
import com.example.voices.controller.MessageControler;
import com.example.voices.model.Conversation;
import com.example.voices.model.LastMessage;
import com.example.voices.model.Message;
import com.example.voices.util.ResultListener;
import com.google.firebase.auth.FirebaseAuth;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.nio.channels.NonReadableChannelException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationActivity extends AppCompatActivity {
    public static final String CONVERSATIONID = "conversationId";
    public static final String OTHERUSERID = "otherUserId";

    String convString;
    String oUserIdString;
    MessagesList messagesList;
    MessageInput messageInput;
    MessageControler messageControler;
    ConversationController conversationController;
    ImageButton imageButtonRecord;
    Chronometer chronometer;
    Boolean chronActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        messageControler = new MessageControler();
        conversationController = new ConversationController();

        chronActive = false;

        //Obtener el Intent
        Intent unIntent = getIntent();

        //Obtener el Bundle
        Bundle unBundle = unIntent.getExtras();

        //Obtener el mensaje
        convString = unBundle.getString(CONVERSATIONID);
        oUserIdString = unBundle.getString(OTHERUSERID);
        messagesList = findViewById(R.id.messagesList);
        messageInput = findViewById(R.id.inputNewMessageConvActivity);
        imageButtonRecord = findViewById(R.id.conversation_record_audio_button);
        chronometer = findViewById(R.id.chronometer_conversation);


        MessagesListAdapter<Message> adapter = new MessagesListAdapter<>(FirebaseAuth.getInstance().getUid(), null);
        messagesList.setAdapter(adapter);

        /*messageControler.getMessagesFromConv(convString, new ResultListener<List<Message>>() {
            @Override
            public void finish(List<Message> resultado) {

                adapter.addToEnd(resultado, true);
            }
        });*/
        messageControler.getNewMessageRealtime(convString, new ResultListener<Message>() {
            @Override
            public void finish(Message resultado) {
                adapter.addToStart(resultado,true);
            }
        });

        Context me = this;

        imageButtonRecord.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!chronActive) {
                    chronometer.setVisibility(View.VISIBLE);
                    chronometer.start();
                    chronActive = true;
                }
                return true;
            }
        });

        imageButtonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chronActive){
                    chronometer.stop();
                    chronActive = false;
                    chronometer.setVisibility(View.INVISIBLE);
                }
            }
        });


        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                if (input.length() > 0){
                    Message message = new Message();
                    message.setMessageText(input.toString());
                    Map<String, Long> userMap = new HashMap<>();
                    userMap.put(FirebaseAuth.getInstance().getUid(), System.currentTimeMillis());
                    message.setUserMap(userMap);
                    message.setTimestamp(System.currentTimeMillis());

                    if (convString.equals("0")){
                        Conversation conversation = new Conversation();
                        Map<String, Boolean> users = new HashMap<>();
                        users.put(FirebaseAuth.getInstance().getUid(), true);
                        users.put(oUserIdString, true);
                        conversation.setUsers(users);
                        conversation.setLastMessage(message);
                        final ProgressDialog progress = new ProgressDialog(me);
                        progress.setMessage("Creando conversacion");
                        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                        progress.show();
                        conversationController.newConversationUpload(conversation, new ResultListener<String>() {
                            @Override
                            public void finish(String resultado) {
                                convString = resultado;
                                messageControler.removeMessagesListenerRegistration();
                                messageControler.getNewMessageRealtime(convString, new ResultListener<Message>() {
                                    @Override
                                    public void finish(Message resultado) {
                                        adapter.addToStart(resultado, true);
                                    }
                                });
                                progress.dismiss();
                                Map<String, Long> conversationMap = new HashMap<>();
                                conversationMap.put(convString, System.currentTimeMillis());
                                message.setConversationMap(conversationMap);
                                messageControler.addMessage(message, new ResultListener<Boolean>() {
                                    @Override
                                    public void finish(Boolean resultado) {

                                    }
                                });
                                /*adapter.addToStart(message, true);*/
                            }
                        });
                        return true;
                    }
                    else {
                        Map<String, Long> conversationMap = new HashMap<>();
                        conversationMap.put(convString, System.currentTimeMillis());
                        message.setConversationMap(conversationMap);
                        messageControler.addMessage(message, new ResultListener<Boolean>() {
                            @Override
                            public void finish(Boolean resultado) {

                            }
                        });
                        /*adapter.addToStart(message, true);*/
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messageControler.removeMessagesListenerRegistration();
    }
}
