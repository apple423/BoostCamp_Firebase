package com.example.han.testfirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Created by Han on 2017-07-11.
 */

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText authEmailEditText;
    private EditText authPasswordEditText;
    private Button authSendButton;
    private Button authPrevButton;
    private Context context;
    private ButtonClickListener buttonClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        context = this;
        mAuth = FirebaseAuth.getInstance();
        buttonClickListener = new ButtonClickListener();
        authEmailEditText = (EditText) findViewById(R.id.email_editText_auth);
        authPasswordEditText = (EditText) findViewById(R.id.password_editText_auth);
        authSendButton = (Button) findViewById(R.id.send_button_auth);
        authPrevButton = (Button) findViewById(R.id.prev_auth);

        authSendButton.setOnClickListener(buttonClickListener);
        authPrevButton.setOnClickListener(buttonClickListener);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Sign in", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Sign out", "onAuthStateChanged:signed_out");
                }

            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    class ButtonClickListener implements Button.OnClickListener {


            @Override
            public void onClick(View v) {

                int id = v.getId();


                if (id == R.id.send_button_auth) {

                    signIn(authEmailEditText.getText().toString(),authPasswordEditText.getText().toString());

                } else if (id == R.id.next_auth) {
                    // Intent intent = new Intent(context,AuthActivity.class);
                    // startActivity(intent);

                } else if (id == R.id.prev_auth) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);


                }


            }
        }

    public void signIn(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d("Success", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(AuthActivity.this, "실패",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }


}

