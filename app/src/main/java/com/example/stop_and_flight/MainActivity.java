package com.example.stop_and_flight;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.stop_and_flight.fragment.StatisticsFragment;
import com.example.stop_and_flight.fragment.TicketListFragment;
import com.example.stop_and_flight.fragment.TaskFragment;
import com.example.stop_and_flight.fragment.TicketingFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private DrawerLayout mDrawerLayout;
    private Context context = this;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon); //뒤로가기 버튼 이미지 지정

        FragmentManager fragmentManager = getSupportFragmentManager(); //fragment 매니저
        //fragment 객체 생성
        TicketListFragment ticket_list = new TicketListFragment();
        TicketingFragment ticketing_fragment = new TicketingFragment();
        TaskFragment taskFragment = new TaskFragment();
        MypageFragment mypage=new MypageFragment();

        StatisticsFragment statisticsFragment = new StatisticsFragment();
        CalendarFragment calendarFragment = new CalendarFragment();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //fragment 트랜색션
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();
                if (id == R.id.menu1) {       //여기에 메뉴 버튼 클릭했을때 옮길 페이지 연결하시면 됩니다.
                    fragmentTransaction.replace(R.id.container, mypage).addToBackStack(null).commitAllowingStateLoss();
                } else if (id == R.id.menu2) {
                    fragmentTransaction.replace(R.id.container, calendarFragment).addToBackStack(null).commitAllowingStateLoss();
                } else if (id == R.id.menu3) {
                    fragmentTransaction.replace(R.id.container, ticket_list).addToBackStack(null).commitAllowingStateLoss();
                } else if (id == R.id.menu4) {
                    fragmentTransaction.replace(R.id.container, ticketing_fragment).addToBackStack(null).commitAllowingStateLoss();
                } else if (id == R.id.menu5) {
                    fragmentTransaction.replace(R.id.container, taskFragment).addToBackStack(null).commitAllowingStateLoss();
                } else if (id == R.id.menu8) {
                    Intent intent = new Intent(MainActivity.this, FilghtActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu9) {
                    Toast.makeText(context, title + ": 친구페이지 성공", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.menu10) {
                    fragmentTransaction.replace(R.id.container, statisticsFragment).addToBackStack(null).commitAllowingStateLoss();
                }
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).addToBackStack(null).commitAllowingStateLoss();
    }
}