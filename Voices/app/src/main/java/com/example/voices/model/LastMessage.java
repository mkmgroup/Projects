package com.example.voices.model;

/**
 * Created by Juan on 27/06/2018.
 */

public class LastMessage {
    private String text;
    private Long timestamp;

    public LastMessage() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
