package com.example.projectblog;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.animation.content.Content;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCreate extends Fragment {

    private Uri filePath;
    private Uri uploadPath;
    private int GET_FROM_GALLERY = 1;
    private User user;
    private View root;
    private ContentResolver contentResolver;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private CollectionReference blogs = db.collection("blogs");
    private StorageReference storageReference = storage.getReference();


    private ImageView imageView;
    private EditText title;
    private EditText body;
    private Button upload;
    private Button selectBtn;

    private HomeScreen mCallBack;
    public interface HomeScreen {
        void home(String uId);
    }

    public FragmentCreate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_create, container, false);

        imageView = (ImageView) root.findViewById(R.id.imageSelected);
        title = (EditText) root.findViewById(R.id.titleText);
        body = (EditText) root.findViewById(R.id.bodyText);
        upload = (Button) root.findViewById(R.id.uploadToFirebase);
        selectBtn = (Button) root.findViewById(R.id.selectImage);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        return root;
    }

    public void setUser(User user){
        this.user = user;
    }
    public void setContentResolver(ContentResolver resolver){
        this.contentResolver = resolver;
    }

    public void pickFromGallery(){
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            filePath = data.getData();
            Picasso.get().load(filePath).into(imageView);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallBack = (HomeScreen) activity;
        } catch (ClassCastException e){
            Log.w("test", "Must implement HomeScreen interface");
        }
    }

    private void uploadDataToFirestore(String title, String body){
        Map<String, String> blog = new HashMap<>();
        blog.put("title", title);
        blog.put("body", body);
        blog.put("image", uploadPath.toString());
        blog.put("blogger", user.uId);

        blogs.add(blog)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("test", "Uploaded successfully");
                        mCallBack.home(user.uId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("test", "Could not upload to firebase");
                    }
                });
    }

    private void upload(){
        final StorageReference ref = storageReference.child("uploaded/");
        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                uploadPath = uri;
                                uploadDataToFirestore(title.getText().toString(), body.getText().toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
