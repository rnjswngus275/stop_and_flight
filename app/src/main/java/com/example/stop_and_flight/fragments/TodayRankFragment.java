package com.example.stop_and_flight.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stop_and_flight.R;
import com.example.stop_and_flight.utils.RankingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayRankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayRankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String UID;
    private Context context;
    private DatabaseReference mDatabase;
    private RankingAdapter rankingAdapter;
    private RecyclerView todayRankRecyclerView;



    public TodayRankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayRankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayRankFragment newInstance(String param1, String param2) {
        TodayRankFragment fragment = new TodayRankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_today_rank, container, false);

        rankingAdapter = new RankingAdapter(context);
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        todayRankRecyclerView = view.findViewById(R.id.todayRankRecyclerView);

        return view;
    }

    // 필요한 DB 정보 - 닉네임 & 이용 시간별 (일간 / 주간)
    private void getTodayRankingDB()
    {

    }
}