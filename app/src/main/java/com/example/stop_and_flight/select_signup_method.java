package com.example.stop_and_flight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class select_signup_method extends AppCompatActivity {



    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
            );
    // Create and launch sign-in intent


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_signup_method);
        Button email_signup=(Button)findViewById(R.id.button_email);
        email_signup.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);


            }

        });
    }
}