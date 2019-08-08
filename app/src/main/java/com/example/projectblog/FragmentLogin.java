package com.example.projectblog;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLogin extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private View root;
    private LoginInterface mCallBack;
    private EditText username;
    private EditText email;
    private EditText password;
    private Button loginBtn;
    private Activity activity;

    public interface LoginInterface {
        void loadMainActivity();
        void loadMainActivityWithUser(String uid);
    }

    public FragmentLogin (){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_login, container, false);
        username = (EditText) root.findViewById(R.id.username);
        password = (EditText) root.findViewById(R.id.password);
        email = (EditText) root.findViewById(R.id.email);
        loginBtn = (Button) root.findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(email.getText().toString(), password.getText().toString());
            }
        });

        return root;
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d("test", "Successfully Signed in");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mCallBack.loadMainActivityWithUser(user.getUid());
                        } else {
                            Log.d("test", "Failed to Sign in");
                            Toast toast = Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setActivity(activity);
        try {
            mCallBack = (LoginInterface) activity;
        } catch (ClassCastException e){
            Log.d("test", "Must implement LoginInterface");
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
