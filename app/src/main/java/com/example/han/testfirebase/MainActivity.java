package com.example.han.testfirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    final FirebaseDatabase database =  FirebaseDatabase.getInstance();;
    private EditText emailEditText;
    private EditText nameEditText;
    private Button sendButton;
    private Button nextButton;
    private Button errorButton;
    private Button prevButton;
    private DatabaseReference ref;
    private Context context;
    private ButtonClickListener buttonClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonClickListener = new ButtonClickListener();
        ref = database.getReference("");
        context = this;

        emailEditText = (EditText) findViewById(R.id.email_editText_main);
        nameEditText = (EditText) findViewById(R.id.name_editText_main);
        sendButton = (Button) findViewById(R.id.send_button_main);
        nextButton = (Button) findViewById(R.id.next_main);
        errorButton = (Button) findViewById(R.id.error_button);

        sendButton.setOnClickListener(buttonClickListener);
        nextButton.setOnClickListener(buttonClickListener);
        errorButton.setOnClickListener(buttonClickListener);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
    }

    class ButtonClickListener implements Button.OnClickListener {


        @Override
        public void onClick(View v) {

            int id = v.getId();


            if(id == R.id.send_button_main){

                //users = new Users(emailEditText.getText().toString(),nameEditText.getText().toString());
                String userKey = ref.child("users").push().getKey();

                Users inputUser = new Users(emailEditText.getText().toString(),nameEditText.getText().toString());

                Map<String, Object> user = new HashMap<String, Object>();
                user.put("member" + userKey, inputUser );

                ref.updateChildren(user);

            }

            else if(id == R.id.next_main){
                Intent intent = new Intent(context,AuthActivity.class);
                startActivity(intent);

            }

            else if (id == R.id.error_button){

                Log.d("Error","this is error");
                forceCrash();


            }


        }
    }

    public static class Users{

        public String email;
        public String name;

        public Users(String email, String name){
            this.email = email;
            this.name = name;

        }

        public String getEmail(){
            return email;
        }

        public String getName(){

            return name;
        }

    }


    public void forceCrash() {
        throw new RuntimeException("This is a crash");
    }


}
