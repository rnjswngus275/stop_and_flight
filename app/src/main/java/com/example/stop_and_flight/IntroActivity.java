package com.example.stop_and_flight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){ @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), AppGuideActivity.class);
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