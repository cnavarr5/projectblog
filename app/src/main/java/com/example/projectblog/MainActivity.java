package com.example.projectblog;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity implements FragmentLogin.LoginInterface {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private CollectionReference blogRef = db.collection("blogs");
    private BlogAdapter adapter;
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = blogRef.orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Blog> options = new FirestoreRecyclerOptions.Builder<Blog>()
                .setQuery(query, Blog.class)
                .build();

        adapter = new BlogAdapter(options);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.login:
                if(recyclerView.getVisibility() == View.VISIBLE){
                    loadLoginFragment();
                } else {
                    loadMainActivity();
                }
                return true;
            case R.id.createBlog:
                Toast toast = Toast.makeText(this, "Creating new blog", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            case R.id.settings:
                Toast t2 = Toast.makeText(this, "Settings menu coming soon", Toast.LENGTH_SHORT);
                t2.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadLoginFragment() {
        recyclerView.setVisibility(View.GONE);
        FragmentLogin login = new FragmentLogin();
        fm.beginTransaction()
                .add(R.id.root_fragment, login)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void loadMainActivity() {
        recyclerView.setVisibility(View.VISIBLE);
        fm.popBackStackImmediate();
    }
}
