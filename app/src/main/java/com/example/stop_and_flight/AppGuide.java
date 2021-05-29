package com.example.stop_and_flight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
//import me.relex.circleindicator.CircleIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AppGuide extends AppCompatActivity {
    //private CircleIndicator mIndicator;
    AppGuideAdapter adapter = new AppGuideAdapter(getSupportFragmentManager());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        ViewPager viewPager = findViewById(R.id.pager);
        setupViewPager(viewPager);
        //mIndicator=(CircleIndicator)findViewById(R.id.indicator);
        //mIndicator.setViewPager(viewPager);

        Button signup_button=(Button)findViewById(R.id.button2);
        Button login_button=(Button)findViewById(R.id.go_login);

        signup_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent= new Intent(getApplicationContext(),select_signup_method.class);
                startActivity(intent);
            }
        });


        login_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        Button go_login_button=(Button)findViewById(R.id.go_login);
        go_login_button.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
        });


    }


    public void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new AppGuideFragment1(), "1");
        adapter.addFragment(new AppGuideFragment2(), "2");
        viewPager.setAdapter(adapter);
    }


}