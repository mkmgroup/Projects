package com.example.voices.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.example.voices.model.Hashtag;
import com.example.voices.model.OtherUser;
import com.example.voices.util.ResultListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 30/05/2018.
 */

public class HashtagDAO {
    private static final String TAG = "HASHTAGDAO";
    FirebaseFirestore firestore;
    public void getHashtagsPlacheHolder(ResultListener<List<Hashtag>> listener){
        List<Hashtag> hashtags = new ArrayList<>();

        for (int i = 0; i < 8 ; i++){
            hashtags.add(getPlaceHolderHashtag());
        }
        listener.finish(hashtags);
    }
    private Hashtag getPlaceHolderHashtag(){
        Hashtag hashtag = new Hashtag();
        hashtag.setName("#buenosaires");
        hashtag.setAvatarString("https://www.cronista.com/__export/1512394972732/sites/diarioelcronista/img/2017/12/04/buenos_aires_hd_2_2.jpg_258117318.jpg");
        return hashtag;
    }
    public void createHashtag(Hashtag hashtag, ResultListener<Boolean> listener){
        firestore = FirebaseFirestore.getInstance();
        Client client = new Client("3YB8F1QGW4", "276ab65203b6812a73bb0c8b5e8127b7");
        Index index = client.getIndex("voices_hash");
        firestore.collection("hashtags")
                .add(hashtag)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Gson gson = new Gson();
                        String json = gson.toJson(hashtag);
                        try {
                            JSONObject jsonObject = new JSONObject(json);

                            index.addObjectAsync(jsonObject, documentReference.getId(), new CompletionHandler() {
                                @Override
                                public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                                    listener.finish(true);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void checkIfHashtagExists(String hashName, ResultListener<Boolean> listener){
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("hashtags").whereEqualTo("name", hashName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().size() < 0){
                        listener.finish(true);
                    }
                    else {listener.finish(false);}
                }

            }
        });
    }
}
