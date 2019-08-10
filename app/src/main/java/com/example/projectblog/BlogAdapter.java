package com.example.projectblog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class BlogAdapter extends FirestoreRecyclerAdapter<Blog, BlogAdapter.BlogHolder> {

    public BlogAdapter(@NonNull FirestoreRecyclerOptions<Blog> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BlogHolder holder, int position, @NonNull final Blog model) {
        holder.title.setText(model.getTitle());
        holder.body.setText(model.getBody());
        Picasso.get().load(model.image).resize(150, 150).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ViewBlogActivity.class);
                intent.putExtra("title", model.title);
                intent.putExtra("body", model.body);
                intent.putExtra("image", model.image);
                intent.putExtra("blogger", model.blogger);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public BlogHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_item, viewGroup, false);
        return new BlogHolder(view);
    }

    class BlogHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView body;
        ImageView image;

        public BlogHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.BlogTitle);
            body = (TextView) itemView.findViewById(R.id.BlogBody);
            image = (ImageView) itemView.findViewById(R.id.BlogImage);
        }
    }

}
