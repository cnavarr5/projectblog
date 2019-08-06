package com.example.projectblog;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLogin extends Fragment {

    private FirebaseAuth mAuth;
    private View root;
    private LoginInterface mCallBack;

    public interface LoginInterface {
        void loadMainActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_fragment_login, container, false);

        return root;
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d("test", "Successfully Signed in");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("test", user.getUid() + " is the user");
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
        try {
            mCallBack = (LoginInterface) activity;
        } catch (ClassCastException e){
            Log.d("test", "Must implement LoginInterface");
        }
    }

    private void loadRootFragment(){
        mCallBack.loadMainActivity();
    }

}
