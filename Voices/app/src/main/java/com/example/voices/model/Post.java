package com.example.voices.model;

import android.location.Location;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Juan on 28/05/2018.
 */

public class Post {
    private String id;
    private String audioUrl;
    private Date date;
    private Map<String,Long> hashtags;
    private String userId;
    private Location location;
    private Map<String,Long> usersTaggedIds;
    private String duration;
    private Long durLong;
    private Map<String,Long> likes;
    private Map<String, Long> user;
    private String imageURl;


    public Post() {
    }

    public Long getDurLong() {
        return durLong;
    }

    public void setDurLong(Long durLong) {
        this.durLong = durLong;
    }

    public Map<String, Long> getUser() {
        return user;
    }

    public void setUser(Map<String, Long> user) {
        this.user = user;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }

    public String getDuration() {
        return duration;
    }

    public Map<String, Long> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Long> likes) {
        this.likes = likes;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, Long> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Map<String, Long> hashtags) {
        this.hashtags = hashtags;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Map<String, Long> getUsersTaggedIds() {
        return usersTaggedIds;
    }

    public void setUsersTaggedIds(Map<String, Long> usersTaggedIds) {
        this.usersTaggedIds = usersTaggedIds;
    }

    public PostAlgolia toPostAlgolia(String id){
        PostAlgolia postAlgolia = new PostAlgolia();
        postAlgolia.setUserId(userId);
        postAlgolia.setImageURL(imageURl);
        List<String> hashesStrings = new ArrayList<>();
        for (Map.Entry<String, Long> hash : hashtags.entrySet()) {
            hashesStrings.add(hash.getKey());
        }
        postAlgolia.setHashtags(hashesStrings);
        postAlgolia.setId(id);
        return postAlgolia;
    }
}
