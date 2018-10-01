package com.example.voices.model;

import android.support.annotation.NonNull;

import java.nio.channels.spi.AbstractSelectionKey;
import java.util.AbstractMap;
import java.util.Map;

/**
 * Created by Juan on 22/06/2018.
 */

public class Conversation implements Comparable<Conversation>{
    private Map<String, Boolean> users;
    private String id;
    private Message lastMessage;

    public Conversation() {
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public int compareTo(@NonNull Conversation conversation) {
        return lastMessage.getTimestamp().compareTo(conversation.getLastMessage().getTimestamp());
    }
}
