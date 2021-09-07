package com.example.stop_and_flight;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stop_and_flight.utils.TicketDatabaseHandler;
import com.example.stop_and_flight.model.Ticket;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    ViewPager viewPager;
    main_adapter adapter;
    main_adapter2 adpater2;
    List<Ticket> models=new ArrayList<>();
    List<main_model> models2=new ArrayList<>();
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private DatabaseReference mDatabase;
    private RecyclerView ticketRecyclerView;

    private Ticket getTicket;
    private String UID;
    private String ticket_Date;
    private TicketDatabaseHandler db;

    public String strCurYear;
    public String strCurMonth;
    public String strCurDay;
    public String strCurHour;
    public String strCurMinute;
    private int YEAR;
    private int MONTH;
    private int DAY;
    private Context context;
    public int count=0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_main, container, false);

//        LayoutInflater inflater2=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//        inflater2.inflate(R.layout.main_item,container,true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }

        System.out.println("uid확인"+UID);


        viewPager=v.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = new TicketDatabaseHandler(mDatabase);

        AdView mAdView;
        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /* 현재 시간과 날짜를 받아오는 부분 */
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat CurMinuteFormat = new SimpleDateFormat("mm");

        strCurYear = CurYearFormat.format(date);
        strCurMonth = CurMonthFormat.format(date);
        strCurDay = CurDayFormat.format(date);
        strCurHour = CurHourFormat.format(date);
        strCurMinute = CurMinuteFormat.format(date);

        YEAR =  Integer.parseInt(strCurYear);
        MONTH =  Integer.parseInt(strCurMonth);
        DAY =  Integer.parseInt(strCurDay);
        ticket_Date = YEAR + "-" + MONTH + "-" + DAY;

        mDatabase.child("TICKET").child(UID).child(ticket_Date).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models.clear();
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        getTicket = new Ticket();
                        getTicket = fileSnapshot.getValue(Ticket.class);
                        getTicket.setDate(ticket_Date);
                        models.add(getTicket);
                        count++;
                    }
                }
                models.sort(new Comparator<Ticket>() {
                    @Override
                    public int compare(Ticket o1, Ticket o2) {
                        String[] departTime1 = o1.getDepart_time().split(":");
                        String[] departTime2 = o2.getDepart_time().split(":");
                        if (Integer.parseInt(departTime1[0]) == Integer.parseInt(departTime2[0])
                                && Integer.parseInt(departTime1[1]) == Integer.parseInt(departTime2[1]))
                            return 0;
                        if (Integer.parseInt(departTime1[0]) >= Integer.parseInt(departTime2[0])
                                && Integer.parseInt(departTime1[1]) >= Integer.parseInt(departTime2[1]))
                            return 1;
                        if (Integer.parseInt(departTime1[0]) <= Integer.parseInt(departTime2[0])
                                && Integer.parseInt(departTime1[1]) <= Integer.parseInt(departTime2[1]))
                            return -1;
                        return 0;
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });
//      if(count==0){
//          TextView dpt_time = container.findViewById(R.id.dpt_time);
//          dpt_time.setText("없습니다.");
//      }
//        models.add(new main_model("타이틀1","설명1"));
//        models.add(new main_model("타이틀2","설명2"));
//        models.add(new main_model("타이틀2","설명2"));
//        models.add(new main_model("타이틀2","설명2"));

        String str="총";
        String str2="개의 일정이 있습니다.";
        String str3 =str+count+str2;
        if(count==0){
            models2.add(new main_model("오늘은 여행일정이 없습니다","예매하기로 일정을 등록해보세요!"));
             adpater2=new main_adapter2(models2,getContext());
            viewPager=v.findViewById(R.id.viewPager);
            viewPager.setAdapter(adpater2);
            viewPager.setPadding(130, 30, 130, 0);
        }
        else {
            adapter = new main_adapter(models, getContext());
            viewPager = v.findViewById(R.id.viewPager);
            viewPager.setAdapter(adapter);
                    viewPager.setPadding(130, 30, 130, 0);
        }

        return v;


    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).addToBackStack(null).commitAllowingStateLoss();
    }
}