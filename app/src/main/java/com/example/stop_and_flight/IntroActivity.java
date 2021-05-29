package com.example.stop_and_flight;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){ @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), AppGuide.class);
            startActivity(intent); //introActivity 실행 후 AppGuide로
             finish();
        }
             },2000); //2초 후 인트로 실행

    }
    @Override protected void onPause(){
        super.onPause();
        finish();
    }

}