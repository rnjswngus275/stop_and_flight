package com.example.stop_and_flight.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.stop_and_flight.MainActivity;
import com.example.stop_and_flight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlightFailureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlightFailureFragment extends Fragment {

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

    public FlightFailureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FlightFailureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FlightFailureFragment newInstance(String param1, String param2,String param3,String param4,String param5) {
        FlightFailureFragment fragment = new FlightFailureFragment();
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
        }
        System.out.println("확인파라매터"+ today);
        System.out.println("확인파라매터"+ arr_time);
        System.out.println("확인파라매터"+ dpt_time);
        System.out.println("확인파라매터"+ goal);
        System.out.println("확인파라매터"+ id);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user!=null){
            uid  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flight_failure, container, false);

        Button btnOk=(Button)view.findViewById(R.id.btnOk);

        //성공률 데이터베이스에 저장 if 읽어온 성공률 데이터가 존재하지 않으면 100%로 저장 아니면 성공으로 데이터베이스에 전달하고
        // 성공한 비행(equal to) /전체 비행 *100 계산한 결과 데이터베이스에 저장

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance(); // 유저 계정 정보 가져오기
                mDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기

                mDatabase.child("TICKET").child(uid).child(today).child(id).child("success").setValue(1);     //대기중인 예매 완료된 예매

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}