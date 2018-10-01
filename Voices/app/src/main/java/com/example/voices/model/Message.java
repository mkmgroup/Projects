package com.example.voices.model;

import android.support.annotation.NonNull;

import com.example.voices.controller.UserController;
import com.example.voices.util.ResultListener;
import com.google.firebase.firestore.Exclude;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;
import java.util.Map;

/**
 * Created by Juan on 22/06/2018.
 */

public class Message implements IMessage, Comparable<Message>{
    private Map<String, Long> userMap;
    private Map<String, Long> ConversationMap;
    private String messageText;
    private Long timestamp;
    private String audioUrl;
    private Long audioDurLong;

    public Message() {
    }

    public Map<String, Long> getConversationMap() {
        return ConversationMap;
    }

    public void setConversationMap(Map<String, Long> conversationMap) {
        ConversationMap = conversationMap;
    }

    public Long getAudioDurLong() {
        return audioDurLong;
    }

    public void setAudioDurLong(Long audioDurLong) {
        this.audioDurLong = audioDurLong;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    @Exclude
    @Override
    public String getId() {
        return Long.toString(audioDurLong);
    }
    @Exclude
    @Override
    public String getText() {
        return messageText;
    }

    @Exclude
    @Override
    public Author getUser() {
        Author author = new Author();
        for (Map.Entry<String, Long> user : userMap.entrySet()) {
            author.setId(user.getKey());
            author.setName(user.getKey());
        }

        return author;
    }
    @Exclude
    @Override
    public Date getCreatedAt() {
        return new Date(timestamp);
    }

    public Map<String, Long> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, Long> userMap) {
        this.userMap = userMap;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(@NonNull Message message) {
        return timestamp.compareTo(message.getTimestamp());
    }
}
