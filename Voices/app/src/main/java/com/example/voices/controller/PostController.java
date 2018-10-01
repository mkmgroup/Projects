package com.example.voices.controller;

import android.net.Uri;

import com.example.voices.dao.PostDAO;
import com.example.voices.model.Post;
import com.example.voices.model.PostAlgolia;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;

import java.io.File;
import java.util.List;

/**
 * Created by Juan on 28/05/2018.
 */

public class PostController {
    PostDAO postDAO;

    public void uploadPost(String filePath, Post post, ResultListener<Boolean> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.uploadPostToFirestore(filePath, post, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getPostsWithID(String id, ResultListener<List<Post>> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.getPostsWithID(id, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {
                listener.finish(resultado);
            }
        });
    }

    public void getAudioFromURL(String url, ResultListener<File> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.getAudioFromURL(url, new ResultListener<File>() {
            @Override
            public void finish(File resultado) {
                listener.finish(resultado);
            }
        });

    }
    public void getPostsWithHashtag(String hashtag, ResultListener<List<Post>> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.getPostsWithHashtag(hashtag, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {
                listener.finish(resultado);
            }
        });

    }
    public void getSuggestedPosts(String id, ResultListener<List<Post>> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.getSuggestedPosts(id, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getPostAlgoliaWithHashtag(String hashtag, ResultListener<List<PostAlgolia>> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.getPostAlgoliaWithHashtag(hashtag, new ResultListener<List<PostAlgolia>>() {
            @Override
            public void finish(List<PostAlgolia> resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getSuggestedPostsAlgolia (String id, ResultListener<List<PostAlgolia>> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.getSuggestedPostsAlgolia(id, new ResultListener<List<PostAlgolia>>() {
            @Override
            public void finish(List<PostAlgolia> resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getPostFromFollowing(User user, ResultListener<List<Post>> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.getPostFromFollowing(user, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {
                listener.finish(resultado);
            }
        });
    }
    public int getPostsFromIFollowToBFollow(User user, int actualSize, ResultListener<List<Post>> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        return postDAO.getPostsFromIFollowToBFollow(user, actualSize, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getActivePostFromUser(String userId, ResultListener<List<Post>> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.getActivePostFromUser(userId, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getActivePostsFromHashtag(String hashtag, ResultListener<List<Post>> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.getActivePostsFromHashtag(hashtag, new ResultListener<List<Post>>() {
            @Override
            public void finish(List<Post> resultado) {
                listener.finish(resultado);
            }
        });
    }

    public void likePost(String id, Post post, Long time,  ResultListener<Boolean> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.likePost(id, post, time, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void uploadImageToStorage(Uri uri, User user, ResultListener<String> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.uploadImageToStorage(uri, user, new ResultListener<String>() {
            @Override
            public void finish(String resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getAPostWithID(String id, ResultListener<Post> listener){
        if (postDAO == null){
            postDAO = new PostDAO();
        }
        postDAO.getAPostWithID(id, new ResultListener<Post>() {
            @Override
            public void finish(Post resultado) {
                listener.finish(resultado);
            }
        });
    }


}
