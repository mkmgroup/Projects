package com.example.voices.controller;

import com.example.voices.dao.ConversationDAO;
import com.example.voices.model.Conversation;
import com.example.voices.util.ResultListener;

import java.util.List;

/**
 * Created by Juan on 22/06/2018.
 */

public class ConversationController {
    ConversationDAO conversationDAO;

    public void getConversationsRealtimeWithUSerID(String id, ResultListener<List<Conversation>> listResultListener){
        if (conversationDAO == null){
            conversationDAO = new ConversationDAO();
        }
        conversationDAO.getConversationsRealtimeWithUSerID(id, new ResultListener<List<Conversation>>() {
            @Override
            public void finish(List<Conversation> resultado) {
                listResultListener.finish(resultado);
            }
        });
    }
    public void removeConversationListenerRegistration(){
        if (conversationDAO == null){
            conversationDAO = new ConversationDAO();
        }
        conversationDAO.removeConversationListenerRegistration();
    }

    public void newConversationUpload(Conversation conversation, ResultListener<String> listener){
        if (conversationDAO == null){
            conversationDAO = new ConversationDAO();
        }
        conversationDAO.newConversationUpload(conversation, new ResultListener<String>() {
            @Override
            public void finish(String resultado) {
                listener.finish(resultado);
            }
        });
    }
}
