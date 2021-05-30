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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.widget.Toast;

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
        Bundle bundle = new Bundle();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            bundle.putString("UID", user.getUid());
            bundle.putString("name", user.getDisplayName());
            bundle.putString("email", user.getEmail());
            Toast.makeText(context, "사용자의 정보를 가져오기를 성공했습니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "사용자의 정보를 가져오기를 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon); //뒤로가기 버튼 이미지 지정

        FragmentManager fragmentManager = getSupportFragmentManager(); //fragment 매니저
        //fragment 객체 생성

        Fragment_Ticket_list ticket_list = new Fragment_Ticket_list();
        Ticketing_Fragment ticketing_fragment = new Ticketing_Fragment();
        TaskFragment taskFragment = new TaskFragment();
        PassportFragment passportFragment =  new PassportFragment();

        //<div>아이콘 제작자 <a href="https://www.flaticon.com/kr/authors/pixel-perfect" title="Pixel perfect">Pixel perfect</a> from <a href="https://www.flaticon.com/kr/" title="Flaticon">www.flaticon.com</a></div>

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
                    Intent intent = new Intent(MainActivity.this, MypageActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu2) {
                    Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu3) {
                    fragmentTransaction.replace(R.id.container, ticket_list).addToBackStack(null).commitAllowingStateLoss();
                    ticket_list.setArguments(bundle);
                } else if (id == R.id.menu4) {
                    fragmentTransaction.replace(R.id.container, ticketing_fragment).addToBackStack(null).commitAllowingStateLoss();
                    ticketing_fragment.setArguments(bundle);
                } else if (id == R.id.menu5) {
                    fragmentTransaction.replace(R.id.container, taskFragment).addToBackStack(null).commitAllowingStateLoss();
                    taskFragment.setArguments(bundle);
                } else if (id == R.id.menu6) {
                    fragmentTransaction.replace(R.id.container, passportFragment).addToBackStack(null).commitAllowingStateLoss();
                    passportFragment.setArguments(bundle);
                } else if (id == R.id.menu8) {
                    Intent intent = new Intent(MainActivity.this, FilghtActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu9) {
                    Toast.makeText(context, title + ": menu9 성공", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.menu10) {
                    Toast.makeText(context, title + ": menu10 성공", Toast.LENGTH_SHORT).show();
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