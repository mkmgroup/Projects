package com.example.voices.controller;

import com.example.voices.dao.UserDAO;
import com.example.voices.model.OtherUser;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;

import java.util.List;

/**
 * Created by Juan on 30/05/2018.
 */

public class UserController {
    UserDAO userDAO;

    public void getOtherUsersPlaceholder(ResultListener<List<OtherUser>> listener){
        if (userDAO == null){
            userDAO = new UserDAO();
        }
        userDAO.getOtherUsersPlacheHolder(new ResultListener<List<OtherUser>>() {
            @Override
            public void finish(List<OtherUser> resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getUser(String id, ResultListener<User> listener){
        if (userDAO == null){
            userDAO = new UserDAO();
        }
        userDAO.getUserFromFirestore(id, new ResultListener<User>() {
            @Override
            public void finish(User resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void checkIfUserExists(String id, ResultListener<Boolean> listener){
        if (userDAO == null){
            userDAO = new UserDAO();
        }
        userDAO.checkIfUserExists(id, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void createUserOnDb(User user, String id, ResultListener<Boolean> listener){
        if(userDAO == null){
            userDAO = new UserDAO();
        }
        userDAO.createUserWithDataOnFirestore(user, id, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                listener.finish(resultado);
            }
        });
    }

    public void searchOtherUsers(String q, ResultListener<List<OtherUser>> listener){
        if (userDAO == null){
            userDAO = new UserDAO();
        }
        userDAO.getOtherUsersWithQuery(q, new ResultListener<List<OtherUser>>() {
            @Override
            public void finish(List<OtherUser> resultado) {
                listener.finish(resultado);
            }
        });
    }

    public void followUser(String userID, String myID, ResultListener<Boolean> listener){
        if (userDAO == null){
            new UserDAO();
        }
        userDAO.followUser(userID, myID, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getSuggestedUsers(String id, ResultListener<List<OtherUser>> listener){
        if(userDAO == null){
            userDAO = new UserDAO();
        }
        userDAO.getSuggestedOttherUsers(id, new ResultListener<List<OtherUser>>() {
            @Override
            public void finish(List<OtherUser> resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getFollowers(String id, ResultListener<List<User>> listener){
        if(userDAO == null){
            userDAO = new UserDAO();
        }
        userDAO.getFollowers(id, new ResultListener<List<User>>() {
            @Override
            public void finish(List<User> resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void followHashtag(String hashtag, String myID, ResultListener<Boolean> listener){
        if(userDAO == null){
            userDAO = new UserDAO();
        }
        userDAO.followHashtag(hashtag, myID, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void setLastActive(String  myID, Long ime, ResultListener<Boolean> listener ){
        if(userDAO == null){
            userDAO = new UserDAO();
        }
        userDAO.setLastActive(myID, ime, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                listener.finish(resultado);
            }
        });
    }
    public void getFollowing(String id, ResultListener<List<User>> listener){
        if(userDAO == null){
            userDAO = new UserDAO();
        }
        userDAO.getFollowing(id, new ResultListener<List<User>>() {
            @Override
            public void finish(List<User> resultado) {
                listener.finish(resultado);
            }
        });
    }
}
