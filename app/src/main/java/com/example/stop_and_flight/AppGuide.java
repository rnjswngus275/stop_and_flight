package com.example.stop_and_flight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class AppGuide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        ViewPager viewPager = findViewById(R.id.pager);
        AppGuideAdapter adapter = new AppGuideAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}