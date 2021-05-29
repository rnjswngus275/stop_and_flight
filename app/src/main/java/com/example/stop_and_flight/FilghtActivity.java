package com.example.stop_and_flight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FilghtActivity extends AppCompatActivity {

    private CircleIndicator mIndicator;
    AppGuideAdapter adapter = new AppGuideAdapter(getSupportFragmentManager());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filght);

        ViewPager viewPager = findViewById(R.id.pager_filght);
        setupViewPager(viewPager);
        mIndicator=(CircleIndicator)findViewById(R.id.indicator_flight);
        mIndicator.setViewPager(viewPager);

    }

    public void setupViewPager(ViewPager viewPager) {
        /*adapter.addFragment(new Fragment_flight1(), "1");*/
        adapter.addFragment(new Fragment_flight2(), "2");
        viewPager.setAdapter(adapter);
    }


}