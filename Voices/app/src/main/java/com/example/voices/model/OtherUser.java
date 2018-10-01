package com.example.voices.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.pchmn.materialchips.model.ChipInterface;

/**
 * Created by Juan on 28/05/2018.
 */

public class OtherUser implements ChipInterface {

    private String id;
    private String name;
    private String nickname;
    private String avatarUriString;

    public OtherUser() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
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

    public void setAvatarUriString(String avatarUriString) {
        this.avatarUriString = avatarUriString;
    }

    public String getAvatarUriString() {
        return avatarUriString;
    }

    public String getTheId(){
        return id;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Uri getAvatarUri() {
        Uri uri = Uri.parse(avatarUriString);
        return uri;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String getInfo() {
        return nickname;
    }
}
