package com.example.projectblog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements FragmentLogin.LoginInterface,
        FragmentCreate.HomeScreen {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private CollectionReference blogRef = db.collection("blogs");
    private CollectionReference userRef = db.collection("users");
    private BlogAdapter adapter;
    private Toolbar toolbar;
    private FragmentManager fm = getSupportFragmentManager();
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
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
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
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
                if(recyclerView.getVisibility() == View.VISIBLE){
                    if(loggedInUser == null){
                        Toast toast = Toast.makeText(this, "You Must be signed in", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        loadCreatePostFragment(loggedInUser);
                    }
                } else {
                    loadMainActivity();
                }
                return true;
            case R.id.settings:
                Toast t2 = Toast.makeText(this, "Settings menu coming soon", Toast.LENGTH_SHORT);
                t2.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCreatePostFragment(User user){
        FragmentCreate createPost = new FragmentCreate();
        createPost.setUser(user);
        createPost.setContentResolver(getContentResolver());
        recyclerView.setVisibility(View.GONE);
        fm.beginTransaction()
                .add(R.id.root_fragment, createPost)
                .addToBackStack(null)
                .commit();
    }

    private void loadLoginFragment() {
        recyclerView.setVisibility(View.GONE);
        FragmentLogin login = new FragmentLogin();
        login.setActivity(this);
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

    @Override
    public void loadMainActivityWithUser(String uid) {
        loadMainActivity();
        userRef.whereEqualTo("fireId", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        loggedInUser = new User(
                                (ArrayList<String>) doc.get("favorites"),
                                (ArrayList<String>) doc.get("posts"),
                                (String) doc.get("fireId"),
                                (String) doc.get("username"),
                                 doc.getId()
                        );
                        Log.d("test", loggedInUser.toString());
                        toolbar.setTitle(loggedInUser.username);
                    }
                } else {
                    Log.d("test", "Could not get document");
                }
            }
        });
    }

    @Override
    public void home(String uid) {
        loadMainActivityWithUser(uid);
    }
}
