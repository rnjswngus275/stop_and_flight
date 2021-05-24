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

    private String mParam1;
    private String mParam2;

    public FlightSuccessFragment() {
        // Required empty public constructor
    }


    public static FlightSuccessFragment newInstance(String param1, String param2) {
        FlightSuccessFragment fragment = new FlightSuccessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView SuccessRate=(TextView)getView().findViewById(R.id.TextSuccessRate);
        RatingBar RatingBar =(RatingBar)getView().findViewById(R.id.ratingBar);
        EditText Memo=(EditText)getView().findViewById(R.id.editTextMemo);
        Button btnOk=(Button)getView().findViewById(R.id.btnOk);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance(); // 유저 계정 정보 가져오기
        mDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user!=null){
            uid  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }

        //별점 데이터베이스에 저장
       RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(android.widget.RatingBar ratingBar, float rating, boolean fromUser) {

                mDatabase.child("TICKET").child(uid).child("티켓아이디").child("rating").setValue(rating);

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
                mDatabase.child("TICKET").child(uid).child("티켓아이디").child("memo").setValue(Memo_txt);

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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_success, container, false);
    }
}