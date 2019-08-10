package com.example.projectblog;

public class Blog {
    protected String title;
    protected String body;
    protected String image;
    protected String blogger;

    public Blog(String title, String body, String image, String blogger) {
        this.title = title;
        this.body = body;
        this.image = image;
        this.blogger = blogger;
    }

    public Blog(){ /* empty constructor */}

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getBlogger() {return blogger; }

    public String getImage() {
        return image;
    }
}
