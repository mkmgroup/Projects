package com.example.voices.dao;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.beloo.widget.chipslayoutmanager.layouter.Item;
import com.example.voices.model.OtherUser;
import com.example.voices.model.Post;
import com.example.voices.model.PostAlgolia;
import com.example.voices.model.User;
import com.example.voices.util.FrUserStoriesResultListener;
import com.example.voices.util.ResultListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by Juan on 28/05/2018.
 */

public class PostDAO {
    FirebaseFirestore firestore;
    ListenerRegistration listenerRegistration;
    FirebaseStorage storage;
    public PostDAO() {
        storage = FirebaseStorage.getInstance();
    }

    public void uploadPostToFirestore(String filePath, Post post, ResultListener<Boolean> listener){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        Client client = new Client("3YB8F1QGW4", "276ab65203b6812a73bb0c8b5e8127b7");
        Index index = client.getIndex("voices_posts");

        StorageReference storageRef = storage.getReference();

        StorageReference userRef = storageRef.child(user.getUid());

        String id = Long.toString(System.nanoTime());

        StorageReference postRef = userRef.child(id + ".m4a");


        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        UploadTask uploadTask = postRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...

                Log.d("uploaded to: ", taskSnapshot.getDownloadUrl().toString());
                post.setAudioUrl(taskSnapshot.getDownloadUrl().toString());


                firestore.collection("posts")
                        .add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                File file = new File(filePath);
                                file.delete();
                                Gson gson = new Gson();
                                String json = gson.toJson(post.toPostAlgolia(documentReference.getId()));
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
                                listener.finish(false);
                            }
                        });


            }
        });
    }

    public void getPostsWithID(String id, ResultListener<List<Post>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<Post> posts = new ArrayList<>();

        firestore.collection("posts").whereGreaterThan("user." + id, 0).orderBy("user." + id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Post post = document.toObject(Post.class);
                        post.setId(document.getId());
                        posts.add(post);
                    }
                    listener.finish(posts);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        /*firestore.collection("posts").whereEqualTo("userId", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Post post = document.toObject(Post.class);
                        post.setId(document.getId());
                        posts.add(post);
                    }
                    listener.finish(posts);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });*/
    }
    public void getAudioFromURL(String url, ResultListener<File> listener){
        StorageReference storageRef = storage.getReference();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        File localFile = null;
        try {
            localFile = File.createTempFile("audio", "m4a");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File finalLocalFile = localFile;
        httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                listener.finish(finalLocalFile);
                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
    public void getPostsWithHashtag(String hashtag, ResultListener<List<Post>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<Post> posts = new ArrayList<>();
        firestore.collection("posts")
                .whereGreaterThan("hashtags." + hashtag, 0)
                .orderBy("hashtags." + hashtag).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Post post = document.toObject(Post.class);
                        post.setId(document.getId());
                        posts.add(post);
                    }
                    listener.finish(posts);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void getPostAlgoliaWithHashtag(String hashtag, ResultListener<List<PostAlgolia>> listener){
        Gson gson = new Gson();
        Client client = new Client("3YB8F1QGW4", "276ab65203b6812a73bb0c8b5e8127b7");
        Index index = client.getIndex("voices_posts");
        com.algolia.search.saas.Query query = new com.algolia.search.saas.Query(hashtag).setHitsPerPage(8);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                List<PostAlgolia> postAlgoliaList = new ArrayList<>();
                try {
                    JSONArray hits = jsonObject.getJSONArray("hits");
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject userJ = hits.getJSONObject(i);
                        PostAlgolia postAlgolia =  gson.fromJson(userJ.toString(), PostAlgolia.class);
                        postAlgoliaList.add(postAlgolia);
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                listener.finish(postAlgoliaList);
            }
        });
    }

    public void getSuggestedPosts(String id, ResultListener<List<Post>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<Post> posts = new ArrayList<>();
        firestore.collection("posts").limit(8).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Post post = document.toObject(Post.class);
                        post.setId(document.getId());
                        posts.add(post);
                    }
                    listener.finish(posts);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
    public void getSuggestedPostsAlgolia (String id, ResultListener<List<PostAlgolia>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<PostAlgolia> posts = new ArrayList<>();
        firestore.collection("posts").limit(8).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Post post = document.toObject(Post.class);
                        post.setId(document.getId());
                        posts.add(post.toPostAlgolia(document.getId()));
                    }
                    listener.finish(posts);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
    public void getPostFromFollowing(User user, ResultListener<List<Post>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<Post> posts = new ArrayList<>();
        final int[] successCount = {0};
        long i = 0;
        for (Map.Entry<String, Boolean> folUs : user.getFollowingIds().entrySet()) {
                firestore.collection("posts").whereGreaterThan("user."+folUs.getKey(), System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Post post = document.toObject(Post.class);
                                post.setId(document.getId());
                                posts.add(post);
                            }
                        }
                        successCount[0]++;
                        if (successCount[0] == user.getFollowingIds().size()){
                            listener.finish(posts);
                        }
                    }
                });
            i++;
        }

    }

    public int getPostsFromIFollowToBFollow(User user, int actualSize, ResultListener<List<Post>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<Post> posts = new ArrayList<>();
        List<String> usersIds = new ArrayList<>();
        for (Map.Entry<String, Boolean> folUs : user.getFollowingIds().entrySet()) {
            usersIds.add(folUs.getKey());
        }
        Collections.sort(usersIds);

        if (actualSize + 10 <= usersIds.size()){
            for (int i = actualSize; i <= actualSize + 10 ; i++){
                int finalI = i;
                firestore.collection("posts").whereGreaterThan("user."+usersIds.get(i), System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Post post = document.toObject(Post.class);
                                post.setId(document.getId());
                                posts.add(post);
                            }
                            if (finalI == actualSize + 10){
                                listener.finish(posts);
                            }
                        }
                    }
                });
            }

            return usersIds.size();

        }
        else {
            for (int i = actualSize; i < usersIds.size(); i++){
                int finalI = i;
                firestore.collection("posts").whereGreaterThan("user."+usersIds.get(i), System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Post post = document.toObject(Post.class);
                                post.setId(document.getId());
                                posts.add(post);
                            }
                            if (finalI == usersIds.size() -1 ){
                                listener.finish(posts);
                            }
                        }
                    }
                });
            }
            return actualSize + 10;
        }



    }

    public void getActivePostFromUser(String userId, ResultListener<List<Post>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<Post> posts = new ArrayList<>();
        firestore.collection("posts").whereGreaterThan("user."+userId, System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Post post = document.toObject(Post.class);
                        post.setId(document.getId());
                        posts.add(post);
                    }
                    listener.finish(posts);
                }
            }
        });

    }
    public void getActivePostsFromHashtag(String hashtag, ResultListener<List<Post>> listener){
        firestore = FirebaseFirestore.getInstance();
        List<Post> posts = new ArrayList<>();
        firestore.collection("posts").whereGreaterThan("hashtags."+hashtag, System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Post post = document.toObject(Post.class);
                        post.setId(document.getId());
                        posts.add(post);
                    }
                    listener.finish(posts);
                }
            }
        });
    }

    public void likePost(String id, Post post, Long time,  ResultListener<Boolean> listener){
        firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("posts").document(post.getId());
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(docRef);
                Map<String,Long> likes = (Map<String, Long>) snapshot.get("likes");

                if (likes != null){
                    likes.put(id, time);
                }else {
                    likes = new HashMap<>();
                    likes.put(id, time);
                }

                transaction.update(docRef, "likes", likes);

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

    public void uploadImageToStorage(Uri uri, User user, ResultListener<String> listener){
        StorageReference storageRef = storage.getReference();

        StorageReference images = storageRef.child("images");

        StorageReference imageRef = images.child(user.getId() + Long.toString(System.currentTimeMillis()) + ".jpg");

        UploadTask uploadTask = imageRef.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                listener.finish(storageRef.getDownloadUrl().toString());
            }
        });
    }
    public void getAPostWithID(String id, ResultListener<Post> listener){
        firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("posts").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Post post = task.getResult().toObject(Post.class);
                    post.setId(task.getResult().getId());
                    listener.finish(post);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
