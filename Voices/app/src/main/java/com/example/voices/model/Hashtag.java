package com.example.voices.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.pchmn.materialchips.model.ChipInterface;

/**
 * Created by Juan on 30/05/2018.
 */

public class Hashtag implements ChipInterface {
    private String name;
    private String avatarString;

    public Hashtag() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarString() {
        return avatarString;
    }

    public void setAvatarString(String avatarString) {
        this.avatarString = avatarString;
    }

    @Override
    public Object getId() {
        return name;
    }

    @Override
    public Uri getAvatarUri() {
        Uri uri = Uri.parse(avatarString);
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
        return name;
    }
}
