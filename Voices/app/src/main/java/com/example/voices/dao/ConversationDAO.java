package com.example.voices.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.voices.model.Conversation;
import com.example.voices.model.Message;
import com.example.voices.util.ResultListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Juan on 22/06/2018.
 */

public class ConversationDAO {
    private static final String TAG = "CONVERSATIONDAO";
    FirebaseFirestore firestore;
    ListenerRegistration listenerRegistration;
    FirebaseStorage storage;
    public void getConversationsRealtimeWithUSerID(String id, ResultListener<List<Conversation>> listResultListener){
        firestore = FirebaseFirestore.getInstance();
        List<Conversation> conversations = new ArrayList<>();
        listenerRegistration = firestore.collection("conversations")
                .whereEqualTo("users." + id, true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                    Conversation conversation = documentSnapshot.toObject(Conversation.class);
                    conversation.setId(documentSnapshot.getId());
                    conversations.add(conversation);
                }
                Collections.sort(conversations);
                listResultListener.finish(conversations);
            }
        });
    }
    public void removeConversationListenerRegistration(){
        listenerRegistration.remove();
    }

    public void newConversationUpload(Conversation conversation, ResultListener<String> listener){
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("conversations").add(conversation).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                listener.finish(documentReference.getId());
                /*DocumentReference docRef = firestore.collection("users").document(id);
                firestore.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(docRef);
                        Map<String,Boolean> conversations = (Map<String, Boolean>) snapshot.get("conversations");
                        conversations.put(documentReference.getId(), true);
                        transaction.update(docRef, "conversations", conversations);
                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Transaction success!");
                        listener.finish(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                        listener.finish(false);
                    }
                });*/
            }
        });

    }


}
