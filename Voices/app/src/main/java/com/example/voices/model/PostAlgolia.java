package com.example.voices.model;

import java.util.List;

/**
 * Created by Juan on 07/06/2018.
 */

public class PostAlgolia {
    private String id;
    private String userId;
    private List<String> hashtags;
    private String imageURL;

    public PostAlgolia() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
