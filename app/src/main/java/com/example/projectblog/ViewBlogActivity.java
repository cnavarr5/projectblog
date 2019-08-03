package com.example.projectblog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewBlogActivity extends AppCompatActivity {

    public TextView titleTV;
    public TextView bodyTV;
    public ImageView blogImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blog);

        titleTV = (TextView) findViewById(R.id.ViewBlogTitle);
        bodyTV = (TextView) findViewById(R.id.ViewBlogBody);
        blogImageView = (ImageView) findViewById(R.id.ViewBlogImage);

        Bundle extras = getIntent().getExtras();
        titleTV.setText(extras.getString("title"));
        bodyTV.setText(extras.getString("body"));
        Picasso.get().load(extras.getString("image")).resize(400, 400).into(blogImageView);
        extras.clear();
    }

}
