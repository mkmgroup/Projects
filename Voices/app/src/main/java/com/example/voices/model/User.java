package com.example.voices.model;

import com.stfalcon.chatkit.commons.models.IUser;

import java.util.List;
import java.util.Map;

/**
 * Created by Juan on 28/05/2018.
 */

public class User implements IUser{
    private String id;
    private String avatarUrl;
    private String name;
    private String nickname;
    private Map<String,Boolean> followingIds;
    private Map<String,Boolean> followingHashes;
    private Map<String,Boolean> followedBy;
    private Boolean firsTime;
    private Long lastActive;


    public User() {
    }



    public Boolean getFirsTime() {
        return firsTime;
    }

    public Long getLastActive() {
        return lastActive;
    }

    public Map<String, Boolean> getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(Map<String, Boolean> followedBy) {
        this.followedBy = followedBy;
    }

    public void setLastActive(Long lastActive) {
        this.lastActive = lastActive;
    }

    public void setFirsTime(Boolean firsTime) {
        this.firsTime = firsTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatarUrl;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Map<String, Boolean> getFollowingIds() {
        return followingIds;
    }

    public void setFollowingIds(Map<String, Boolean> followingIds) {
        this.followingIds = followingIds;
    }

    public Map<String, Boolean> getFollowingHashes() {
        return followingHashes;
    }

    public void setFollowingHashes(Map<String, Boolean> followingHashes) {
        this.followingHashes = followingHashes;
    }

    public OtherUser toOtherUser(){
        OtherUser otherUser = new OtherUser();
        otherUser.setId(id);
        otherUser.setName(name);
        otherUser.setNickname(nickname);
        otherUser.setAvatarUriString(avatarUrl);
        return otherUser;
    }
}
