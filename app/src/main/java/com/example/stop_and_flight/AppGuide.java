package com.example.stop_and_flight;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
//import me.relex.circleindicator.CircleIndicator;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;

import java.util.List;

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
                Intent intent= new Intent(getApplicationContext(),LoginMethodActivity.class);
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



        if(!checkAccessibilityPermissions()) {
            setAccessibilityPermissions();
        }
    }


    public void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new AppGuideFragment1(), "1");
        adapter.addFragment(new AppGuideFragment2(), "2");
        viewPager.setAdapter(adapter);
    }

    //접근성 권한 설정 부분
    // 접근성 권한 매니저를 통해 접근성 권한이 있는지 확인
    public boolean checkAccessibilityPermissions(){
        AccessibilityManager accessibilityManager =
                (AccessibilityManager)getSystemService(Context.ACCESSIBILITY_SERVICE);

        List list =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);

        Log.d("service_test", "size : " + list.size());
        for(int i = 0; i < list.size(); i++){
            AccessibilityServiceInfo info = (AccessibilityServiceInfo) list.get(i);
            //
            if(info.getResolveInfo().serviceInfo.packageName.equals(getApplication().getPackageName())){
                return true;
            }
        }
        return false;
    }
    //접근성 권한 허용받는 dialog
    public void setAccessibilityPermissions(){
        AlertDialog.Builder permissionDialog = new AlertDialog.Builder(this);
        permissionDialog.setTitle("접근성 권한 설정");
        permissionDialog.setMessage("앱을 사용하기 위해 접근성 권한이 필요합니다.");
        permissionDialog.setPositiveButton("허용", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                return;
            }
        }).create().show();
    }
}
