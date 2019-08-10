package com.example.projectblog;

import java.util.ArrayList;

public class User {
    protected ArrayList<String> favorites;
    protected ArrayList<String> posts;
    protected String fireId;
    protected String username;
    protected String uId;

    public User(){}

    public User(ArrayList<String> favorites, ArrayList<String> posts, String fireId, String username, String uId) {
        this.favorites = favorites;
        this.posts = posts;
        this.fireId = fireId;
        this.username = username;
        this.uId = uId;
    }

    public String toString(){
        return "uid: " + uId + ", fireId: " + fireId;
    }
}
