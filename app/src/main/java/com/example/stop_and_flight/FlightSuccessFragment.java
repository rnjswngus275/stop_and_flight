package com.example.stop_and_flight;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.stop_and_flight.model.CurTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;


public class FlightSuccessFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    private String arr_time;
    private String dpt_time;
    private String goal;
    private String id;
    private String today;
    private int studytime;
    private HashMap<String, Object> UserMap;
    private DatabaseReference mDatabase ;
    private String uid;
    public int point = 0;
    public FlightSuccessFragment() {
        // Required empty public constructor
    }


    public static FlightSuccessFragment newInstance(String param1, String param2,String param3,String param4,String param5) {
        FlightSuccessFragment fragment = new FlightSuccessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            today = getArguments().getString(ARG_PARAM1);
            arr_time = getArguments().getString(ARG_PARAM2);
            dpt_time = getArguments().getString(ARG_PARAM3);
            goal = getArguments().getString(ARG_PARAM4);
            id = getArguments().getString(ARG_PARAM5);
            System.out.println("확인파라매터 널이 아니면 나오는 글짜"+ arr_time);
        }




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user!=null){
            uid  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        System.out.println("확인파라매터"+ today);
        System.out.println("확인파라매터"+ arr_time);
        System.out.println("확인파라매터"+ dpt_time);
        System.out.println("확인파라매터"+ goal);
        System.out.println("확인파라매터"+ id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flight_success, container, false);

        TextView SuccessRate=(TextView)view.findViewById(R.id.TextSuccessRate);
        RatingBar RatingBar =(RatingBar)view.findViewById(R.id.ratingBar);
        EditText Memo=(EditText)view.findViewById(R.id.editTextMemo);
        Button btnOk=(Button)view.findViewById(R.id.btnOk);

        //TODO: 시간 자르기 IF (출발시간<도착시간이면 24:00 - 출발시간 + 도착시간) 10분당 1pt 부여
        //TODO: userInfo db에서 불러오기


        CurTime curTime = new CurTime();
        int curIntTime = curTime.getIntYear() * 10000 + curTime.getIntMonth() * 100 + curTime.getIntDay() * 1;

        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    UserMap = (HashMap<String, Object>) snapshot.getValue();
                    point = Integer.parseInt(String.valueOf(UserMap.get("point")));
                    studytime =  Integer.parseInt(String.valueOf(UserMap.getOrDefault("date/" + curIntTime, 0)));
                }
                point = calculateMinute(arr_time, dpt_time) / 10 + point;
                studytime = studytime + calculateMinute(arr_time, dpt_time);
                SuccessRate.setText(String.valueOf(point));

                mDatabase.child("TICKET").child(uid).child(today).child(id).child("success").setValue(2);
                mDatabase.child("users").child(uid).child("point").setValue(point);
                mDatabase.child("users").child(uid).child("date").child(String.valueOf(curIntTime)).setValue(studytime);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //TODO: textview 에 set text해서 point 얼마 얻었는지 알려주기

        //별점 데이터베이스에 저장
        RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(android.widget.RatingBar ratingBar, float rating, boolean fromUser) {
                mDatabase.child("TICKET").child(uid).child(today).child(id).child("rating").setValue(rating);
            }
        });
        //TODO: 성공률 데이터베이스에 저장
        //성공률 데이터베이스에 저장 if 읽어온 성공률 데이터가 존재하지 않으면 100%로 저장 아니면 성공으로 데이터베이스에 전달하고
        // 성공한 비행(equal to) /전체 비행 *100 계산한 결과 데이터베이스에 저장

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //메모 데이터베이스에 저장
                String Memo_txt = Memo.getText().toString();
                mDatabase.child("TICKET").child(uid).child(today).child(id).child("memo").setValue(Memo_txt);
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private int calculateMinute(String arr_time, String dpt_time)
    {
        int result = 0;
        String[] dptTime = dpt_time.split(":");
        String[] arrTime = arr_time.split(":");

        //형변환
        int dpt_h2 = Integer.parseInt(dptTime[0]);
        int dpt_m2 = Integer.parseInt(dptTime[1]);
        int arr_h2 = Integer.parseInt(arrTime[0]);
        int arr_m2 = Integer.parseInt(arrTime[1]);

        // 도착시간이 출발시간보다 큰 경우
        if((arr_h2 - dpt_h2) >= 0){
            result = (arr_h2 * 60 + arr_m2) - (dpt_h2 * 60 + dpt_m2);
            //분단위로 변환하여 빼고 10분당 1pt
        }
        // 도착시간이 출발시간보다 작은 경우 ex) 출발시간에서 ~ 24:00까지
        else{
            int midnight = (24 - dpt_h2 - 1) * 60 + ( 60 - dpt_m2);
            //24:00부터 도착시간까지
            if(arr_h2 == 0){
                result = midnight + arr_m2;
            }
            else{
                result = midnight + arr_h2 * 60 + arr_m2;
            }
        }

        return result;
    }
}