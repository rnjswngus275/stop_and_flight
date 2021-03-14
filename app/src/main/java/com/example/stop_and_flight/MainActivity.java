package com.example.stop_and_flight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import me.relex.circleindicator.CircleIndicator;


public class MainActivity extends AppCompatActivity {
    private CircleIndicator mIndicator;
    AppGuideAdapter adapter = new AppGuideAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.pager);
        setupViewPager(viewPager);
        mIndicator=(CircleIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);

    }


    public void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new AppGuideFragment1(), "1");
        adapter.addFragment(new AppGuideFragment2(), "2");
        viewPager.setAdapter(adapter);
    }

}
