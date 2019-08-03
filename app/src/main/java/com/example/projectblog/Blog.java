package com.example.projectblog;

public class Blog {
    protected String title;
    protected String body;
    protected String image;

    public Blog(String title, String body, String image) {
        this.title = title;
        this.body = body;
        this.image = image;
    }

    public Blog(){ /* empty constructor */}

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getImage() {
        return image;
    }
}
