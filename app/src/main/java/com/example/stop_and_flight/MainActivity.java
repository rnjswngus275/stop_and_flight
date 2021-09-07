package com.example.stop_and_flight;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stop_and_flight.utils.TicketDatabaseHandler;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;



import com.bumptech.glide.Glide;
import com.example.stop_and_flight.fragments.CalendarFragment;
import com.example.stop_and_flight.fragments.RankingFragment;
import com.example.stop_and_flight.fragments.TaskFragment;
import com.example.stop_and_flight.model.Ticket;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private DrawerLayout mDrawerLayout;
    private Context context = this;
    ViewPager viewPager;
    main_adapter adapter;
    List<Ticket> models=new ArrayList<>();;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private DatabaseReference mDatabase;
    private RecyclerView ticketRecyclerView;
    private TicketAdapter ticketAdapter;
    private Ticket getTicket;
    private String UID;
    private String ticket_Date;
    private TicketDatabaseHandler db;
    ImageView headerIcon;
    TextView title_toolbar;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon); //뒤로가기 버튼 이미지 지정


        title_toolbar=(TextView)findViewById(R.id.toolbar_title);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        String filename=UID+"_ProfileImage";
        getImage(filename);

        MainFragment mainfragment=MainFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,mainfragment).commit();

        FragmentManager fragmentManager = getSupportFragmentManager(); //fragment 매니저
        //fragment 객체 생성

        StatisticsFragment statisticsFragment = new StatisticsFragment();
        CalendarFragment calendarFragment = new CalendarFragment();
        TaskFragment taskFragment = new TaskFragment();
        MypageFragment mypage = new MypageFragment();
        RankingFragment rankingFragment = new RankingFragment();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);
        headerIcon=headerView.findViewById(R.id.imageView);
        TextView nickname=headerView.findViewById(R.id.navi_nickname);
        TextView chingho=headerView.findViewById(R.id.navi_chingho);

        final DatabaseReference ref = mDatabase.child("users").child(UID).child("nickname");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nickname.setText(snapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


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
                    title_toolbar.setText("MY PAGE");
                } else if (id == R.id.menu2) {
                    fragmentTransaction.replace(R.id.container, calendarFragment).addToBackStack(null).commitAllowingStateLoss();
                    title_toolbar.setText("TICKETING");
                }
                 else if (id == R.id.menu3) {
                    fragmentTransaction.replace(R.id.container, taskFragment).addToBackStack(null).commitAllowingStateLoss();
                    title_toolbar.setText("TASK");
                } else if (id == R.id.menu4) {
                    fragmentTransaction.replace(R.id.container, rankingFragment).addToBackStack(null).commitAllowingStateLoss();
                    title_toolbar.setText("RANKING");
                } else if (id == R.id.menu5) {
                    title_toolbar.setText("FRIENDS");
                    Intent intent = new Intent(MainActivity.this, FilghtActivity.class);
                    startActivity(intent);
                } else if (id == R.id.menu6) {
                    fragmentTransaction.replace(R.id.container, statisticsFragment).addToBackStack(null).commitAllowingStateLoss();
                    title_toolbar.setText("STATISTICS");
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
    // 프로필 이미지 가져오기
    public void getImage(String filename){
        File file=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/ProfileImage");
        if(!file.isDirectory()){
            file.mkdir();
        }
        downloadImg(filename);
    }
    //이미지 다운로드해서 이미지 뷰에 보여주기
    public void downloadImg(String filename){
        FirebaseStorage storage =FirebaseStorage.getInstance();
        StorageReference storageRef=storage.getReference();
        storageRef.child("ProfileImage/"+filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(headerIcon);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}