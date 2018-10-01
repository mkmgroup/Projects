package com.example.voices.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.example.voices.model.OtherUser;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.example.voices.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

/**
 * Created by Juan on 30/05/2018.
 */

public class UserDAO {
    FirebaseFirestore firestore;
    private static final String TAG = "USERDAO";
    public void getOtherUsersPlacheHolder(ResultListener<List<OtherUser>> listener){
        List<OtherUser> otherUsers = new ArrayList<>();

        for (int i = 0; i < 8 ; i++){
            otherUsers.add(getPlaceHolderOtherUser());
        }
        listener.finish(otherUsers);
    }

    private OtherUser getPlaceHolderOtherUser(){
        OtherUser otherUser = new OtherUser();
        otherUser.setId("12345");
        otherUser.setNickname("steinjuan");
        otherUser.setName("Juan Stein");
        otherUser.setAvatarUriString("https://firebasestorage.googleapis.com/v0/b/voices-ad267.appspot.com/o/dj.png?alt=media&token=dcefe0dd-329d-40dc-b86a-5de04135ddae");
        return otherUser;
    }

    public void getSuggestedOttherUsers(String id, ResultListener<List<OtherUser>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<OtherUser> otherUsers = new ArrayList<>();
        firestore.collection("users").limit(8).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        otherUsers.add(user.toOtherUser());
                    }
                    listener.finish(otherUsers);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public static String fromCharCode(int... codePoints) {
        return new String(codePoints, 0, codePoints.length);
    }

    public void getOtherUsersWithQuery(String q, ResultListener<List<OtherUser>> listener){
        Gson gson = new Gson();
        Client client = new Client("3YB8F1QGW4", "276ab65203b6812a73bb0c8b5e8127b7");
        Index index = client.getIndex("voices");
        com.algolia.search.saas.Query query = new com.algolia.search.saas.Query(q).setHitsPerPage(8);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                List<OtherUser> otherUsers = new ArrayList<>();
                try {
                    JSONArray hits = jsonObject.getJSONArray("hits");
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject userJ = hits.getJSONObject(i);
                        OtherUser otherUser =  gson.fromJson(userJ.toString(), OtherUser.class);
                        otherUsers.add(otherUser);
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                listener.finish(otherUsers);
            }
        });


    }

    public void getUserFromFirestore(String id, ResultListener<User> listener){
        firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("USERDAO", "DocumentSnapshot data: " + document.getData());
                        User user = document.toObject(User.class);
                        user.setId(id);
                        listener.finish(user);
                    } else {
                        Log.d("USERDAO", "No such document");
                    }
                } else {
                    Log.d("USERDAO", "get failed with ", task.getException());
                }
            }
        });
    }
    public void createUserWithDataOnFirestore(User user, String id, ResultListener<Boolean> listener){
        firestore = FirebaseFirestore.getInstance();
        Client client = new Client("3YB8F1QGW4", "276ab65203b6812a73bb0c8b5e8127b7");
        Index index = client.getIndex("voices");
        user.setId(id);
        user.setFollowingIds(new HashMap<>());
        user.setFollowingHashes(new HashMap<>());
        user.setFollowedBy(new HashMap<>());
        user.setLastActive(0L);
        user.getFollowingIds().put(id, true);
        user.getFollowedBy().put(id, true);
        OtherUser otherUser = user.toOtherUser();
        DocumentReference docRef = firestore.collection("users").document(id);
        docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Gson gson = new Gson();
                String json = gson.toJson(otherUser);
                try {
                    JSONObject jsonObject = new JSONObject(json);

                    index.addObjectAsync(jsonObject, id, new CompletionHandler() {
                        @Override
                        public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                            listener.finish(true);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.finish(false);
            }
        });
    }
    public void checkIfUserExists(String id, ResultListener<Boolean> listener){
        firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("users").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("USERDAO", "DocumentSnapshot data: " + document.getData());
                        listener.finish(true);
                    } else {
                        Log.d("USERDAO", "No such document");
                        listener.finish(false);
                    }
                } else {
                    Log.d("USERDAO", "get failed with ", task.getException());
                }
            }
        });
    }
    public void followUser(String userID, String myID, ResultListener<Boolean> listener){
        firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("users").document(myID);
        DocumentReference docRefFollow = firestore.collection("users").document(userID);

        firestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(docRef);
                DocumentSnapshot snapshot2 = transaction.get(docRefFollow);

                Map<String,Boolean> followingIds = (Map<String, Boolean>) snapshot.get("followingIds");

                if (followingIds != null){
                    followingIds.put(userID, true);
                }else {
                    followingIds = new HashMap<>();
                    followingIds.put(userID, true);
                }

                Map<String,Boolean> followedBy = (Map<String, Boolean>) snapshot.get("followedBy");
                followedBy.put(myID, true);


                transaction.update(docRef, "followingIds", followingIds);
                transaction.update(docRefFollow, "followedBy", followedBy);

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

    }
    public void getFollowers(String id, ResultListener<List<User>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<User> users = new ArrayList<>();
        firestore.collection("users")
                .whereEqualTo("followingIds." + id, true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        User user = document.toObject(User.class);
                        users.add(user);
                    }
                    listener.finish(users);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
    public void followHashtag(String hashtag, String myID, ResultListener<Boolean> listener){
        firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("users").document(myID);
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(docRef);
                Map<String,Boolean> hashtags = (Map<String, Boolean>) snapshot.get("followingHashes");

                if (hashtags != null){
                    hashtags.put(hashtag, true);
                }else {
                    hashtags = new HashMap<>();
                    hashtags.put(hashtag, true);
                }

                transaction.update(docRef, "followingHashes", hashtags);

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

    }
    public void setLastActive(String  myID, Long ime, ResultListener<Boolean> listener ){
        firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("users").document(myID);
        docRef.update("lastActive" , ime).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
                listener.finish(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        listener.finish(false);
                    }
                });
    }

    public void getFollowing(String id, ResultListener<List<User>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<User> users = new ArrayList<>();
        firestore.collection("users")
                .whereEqualTo("followedBy." + id, true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        User user = document.toObject(User.class);
                        users.add(user);
                    }
                    listener.finish(users);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }



}
