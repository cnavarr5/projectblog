package com.example.projectblog;

public class User {
    protected String[] favorites;
    protected String[] posts;
    protected String fireId;
    protected String username;

    public User(){}

    public User(String[] favorites, String[] posts, String fireId, String username) {
        this.favorites = favorites;
        this.posts = posts;
        this.fireId = fireId;
        this.username = username;
    }
}
