package com.example.voices.controller;

import com.example.voices.dao.ConversationDAO;
import com.example.voices.dao.MessageDAO;
import com.example.voices.model.Message;
import com.example.voices.util.ResultListener;

import java.util.List;

/**
 * Created by Juan on 26/06/2018.
 */

public class MessageControler {
    MessageDAO messageDAO;

    public void getMessagesFromConvRealtime(String id, ResultListener<List<Message>> listener) {
        if (messageDAO == null){
            messageDAO = new MessageDAO();
        }
        messageDAO.getMessagesFromConvRealtime(id, new ResultListener<List<Message>>() {
            @Override
            public void finish(List<Message> resultado) {
                listener.finish(resultado);
            }
        });
    }


    public void removeMessagesListenerRegistration(){
        if (messageDAO == null){
            messageDAO = new MessageDAO();
        }
        messageDAO.removeMessagesListenerRegistration();
    }

    public void addMessage(Message message, ResultListener<Boolean> listener){
        if (messageDAO == null){
            messageDAO = new MessageDAO();
        }
        messageDAO.addMessage(message, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getMessagesFromConv(String id, ResultListener<List<Message>> listener){
        if (messageDAO == null){
            messageDAO = new MessageDAO();
        }
        messageDAO.getMessagesFromConv(id, new ResultListener<List<Message>>() {
            @Override
            public void finish(List<Message> resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getNewMessageRealtime(String id, ResultListener<Message> listener){
        if (messageDAO == null){
            messageDAO = new MessageDAO();
        }
        messageDAO.getNewMessageRealtime(id, new ResultListener<Message>() {
            @Override
            public void finish(Message resultado) {
                listener.finish(resultado);
            }
        });
    }

}
