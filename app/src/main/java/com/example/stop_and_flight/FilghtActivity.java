package com.example.stop_and_flight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FilghtActivity extends AppCompatActivity implements Fragment_flight1.OnTimePickListener{

    String arrive_time;
    String depart_time;
    private CircleIndicator mIndicator;
    AppGuideAdapter adapter = new AppGuideAdapter(getSupportFragmentManager());

    @Override
    public void onTimeSelected(String arr, String dpt) {
        arrive_time=arr;
        depart_time=dpt;

        System.out.println("확인 진입확인 arr"+arrive_time);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filght);
        ViewPager viewPager = findViewById(R.id.pager_filght);
        setupViewPager(viewPager);
        mIndicator=(CircleIndicator)findViewById(R.id.indicator_flight);
        mIndicator.setViewPager(viewPager);

//        System.out.println("확인 액티비티에서"+arrive_time);
//        System.out.println("확인 액티비티에서"+depart_time);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //accessibility 서비스 시작
        Intent sintent = new Intent(getApplicationContext(),Accessibility.class); // 이동할 컴포넌트
        startService(sintent); // 서비스 시작
        sintent.putExtra("flight","on");
        System.out.println("확인 서비스 시작");
    }

    @Override
    protected void onStop() {
        Intent sintent = new Intent(getApplicationContext(),Accessibility.class); // 이동할 컴포넌트
        stopService(sintent); // 서비스 끝
        super.onStop();
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new Fragment_flight1(), "1");
        adapter.addFragment(new Fragment_flight2(), "2");
        viewPager.setAdapter(adapter);
    }



}