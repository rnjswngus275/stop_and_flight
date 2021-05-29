package com.example.stop_and_flight;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FlightSuccessFragment extends Fragment {


    DatabaseReference mDatabase ;
    String uid="";
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
            goal =getArguments().getString(ARG_PARAM4);
            id =getArguments().getString(ARG_PARAM5);
            System.out.println("확인파라매터 널이 아니면 나오는 글짜"+ arr_time);
        }
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

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance(); // 유저 계정 정보 가져오기
        mDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user!=null){
            uid  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        mDatabase.child("TICKET").child(uid).child(today).child(id).child("wait").setValue("false");


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
                String Memo_txt =Memo.getText().toString();
                mDatabase.child("TICKET").child(uid).child(today).child(id).child("memo").setValue(Memo_txt);

                //비행시간 데이터 읽어오기
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //데이터 불러오는데 실패했을때
                    }
                });
                //TODO: 스탬프 페이지 완성되면 FRAGMENT로 화면 전환
            }
        });

        return view;
    }
}