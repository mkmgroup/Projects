package com.example.voices.model;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by Juan on 26/06/2018.
 */

public class Author implements IUser {
    private String id;
    private String name;


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return null;
    }
}
