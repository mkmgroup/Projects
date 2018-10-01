package com.example.voices.controller;

import com.example.voices.dao.HashtagDAO;
import com.example.voices.model.Hashtag;
import com.example.voices.util.ResultListener;

import java.util.List;

/**
 * Created by Juan on 30/05/2018.
 */

public class HashtagContoller {
    HashtagDAO hashtagDAO;
    public void getHastagsPlaceholders(ResultListener<List<Hashtag>> listener){
        if (hashtagDAO == null){
            hashtagDAO = new HashtagDAO();
        }
        hashtagDAO.getHashtagsPlacheHolder(new ResultListener<List<Hashtag>>() {
            @Override
            public void finish(List<Hashtag> resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void createHashtag(Hashtag hashtag, ResultListener<Boolean> listener){
        if (hashtagDAO == null){
            hashtagDAO = new HashtagDAO();
        }
        hashtagDAO.createHashtag(hashtag, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void checkIfHashtagExists(String hashName, ResultListener<Boolean> listener){
        if (hashtagDAO == null){
            hashtagDAO = new HashtagDAO();
        }
        hashtagDAO.checkIfHashtagExists(hashName, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                listener.finish(resultado);
            }
        });
    }
}
