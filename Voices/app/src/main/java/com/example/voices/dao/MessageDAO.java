package com.example.voices.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.voices.model.Conversation;
import com.example.voices.model.LastMessage;
import com.example.voices.model.Message;
import com.example.voices.model.MessagesContainer;
import com.example.voices.util.ResultListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Juan on 22/06/2018.
 */

public class MessageDAO {
    FirebaseFirestore firestore;
    ListenerRegistration listenerRegistration;
    FirebaseStorage storage;

    public void getMessagesFromConv(String id, ResultListener<List<Message>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<Message> messages = new ArrayList<>();
        firestore.collection("messages").whereGreaterThan("conversationMap." + id, 0).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Message message = documentSnapshot.toObject(Message.class);
                        messages.add(message);
                    }
                    Collections.sort(messages);
                    listener.finish(messages);
                }
            }
        });
    }
    public void getMessagesFromConvRealtime(String id, ResultListener<List<Message>> listener) {
        firestore = FirebaseFirestore.getInstance();
        List<Message> messages = new ArrayList<>();
        listenerRegistration = firestore.collection("messages").whereGreaterThan("conversationMap." + id, 0).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Message message = documentSnapshot.toObject(Message.class);
                    messages.add(message);
                }
                Collections.sort(messages);
                listener.finish(messages);
            }
        });
    }
    public void addMessage(Message message, ResultListener<Boolean> listener){
        String convString = "";
        for (Map.Entry<String, Long> conv : message.getConversationMap().entrySet()) {
          convString = conv.getKey();
        }
        firestore = FirebaseFirestore.getInstance();
        /*LastMessage lastMessage = new LastMessage();
        lastMessage.setText(message.getText());
        lastMessage.setTimestamp(message.getTimestamp());*/

        DocumentReference docRef = firestore.collection("conversations").document(convString);
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(docRef);



                transaction.update(docRef, "lastMessage", message);

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
        });

        /*firestore.collection("conversations").document(convString).update("lastMessage", lastMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });*/

        firestore.collection("messages").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                listener.finish(true);
            }
        });
    }

    public void getNewMessageRealtime(String id, ResultListener<Message> listener){
        firestore = FirebaseFirestore.getInstance();

        listenerRegistration = firestore.collection("messages").whereGreaterThan("conversationMap." + id, 0).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d(TAG, "New Message: " + dc.getDocument().getData());
                            Message message = dc.getDocument().toObject(Message.class);
                            listener.finish(message);
                            break;
                        case MODIFIED:
                            Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                            break;
                    }
                }

            }
        });
    }

    public void removeMessagesListenerRegistration(){
        listenerRegistration.remove();
    }

}
