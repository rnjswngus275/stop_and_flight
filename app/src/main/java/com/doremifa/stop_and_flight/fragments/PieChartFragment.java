package com.doremifa.stop_and_flight.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doremifa.stop_and_flight.R;
import com.doremifa.stop_and_flight.model.Dictionary;
import com.doremifa.stop_and_flight.model.Ticket;
import com.doremifa.stop_and_flight.utils.CustomAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PieChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChartFragment extends Fragment {
    PieChart pieChart;
    private com.github.mikephil.charting.charts.PieChart showpiechart;
    private String UID;
    private Ticket ticket;
    private DatabaseReference databaseReference;
    private ArrayList<Dictionary> mArrayList;
    private CustomAdapter mAdapter;
    private int count = -1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PieChartFragment() { }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static PieChartFragment newInstance(String param1, String param2) {
        PieChartFragment fragment = new PieChartFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_1, container, false);
        Context ct = container.getContext();
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(ct,LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();

        mAdapter = new CustomAdapter( mArrayList);

        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration); //기본 구분선 추가

        showpiechart = (com.github.mikephil.charting.charts.PieChart) view.findViewById(R.id.piechart);
        pieChart = (PieChart) view.findViewById(R.id.piechart);

        /*기본세팅은 파이차트*/
        showpiechart.setVisibility(PieChart.VISIBLE);
        pieChart(inflater,container,savedInstanceState);

        //-------여기부터 페이지 하단의 Todo,총비행시간 ArrayList출력하는 부분------//
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }

        databaseReference =  FirebaseDatabase.getInstance().getReference();
        databaseReference.child("TICKET").child(UID).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 여기서 ticket이 다음 날 넘어가요!!
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        String arr_todo = null;
                        int arr_time = 0;
                        for(DataSnapshot fileSnapshot2 : fileSnapshot.getChildren()){
                            ticket = new Ticket();
                            ticket = fileSnapshot2.getValue(Ticket.class);
                            System.out.println("확인");
                            arr_todo = ticket.getTodo();

                            //비율계산//
                            String[] arr1 = ticket.getDepart_time().split(":"); //출발시간
                            String[] arr2 = ticket.getArrive_time().split(":"); //도착시간
                            int depart_hour=Integer.parseInt(arr1[0]);//출발시간 시
                            int depart_minute=Integer.parseInt(arr1[1]);//출발시간 분
                            int arrive_hour=Integer.parseInt(arr2[0]); //도착시간 시
                            int arrive_minute=Integer.parseInt(arr2[1]); //도착시간 분

                            int term_hour = arrive_hour-depart_hour;
                            int term_minute = arrive_minute-depart_minute;
                            //------//

                            arr_time= term_hour*60+term_minute; //시간을 분으로 계산 (예를들어 1시간 30분-> 90분으로 계산)

                            String arr_times = arr_time + "분";

                            System.out.println(ticket);
                            System.out.println(ticket.getTodo());
                            System.out.println(arr_time);

                            Dictionary data = new Dictionary("◼︎  "+arr_todo, arr_times);
                            mArrayList.add(data); // RecyclerView의 마지막 줄에 삽입
                            mAdapter.notifyDataSetChanged();
                        }
                    }}}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });return view; }

    private void pieChart(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState)
    {
        View view1 = inflater.inflate(R.layout.fragment_1, container, false);
        String date;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져옴
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져옴
        }

        databaseReference =  FirebaseDatabase.getInstance().getReference();
        databaseReference.child("TICKET").child(UID).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pieChart.setUsePercentValues(true);
                pieChart.getDescription().setEnabled(false);
                pieChart.setExtraOffsets(5,10,5,5);
                pieChart.setDragDecelerationFrictionCoef(0.95f);
                pieChart.setDrawHoleEnabled(false);
                pieChart.setHoleColor(Color.BLACK);
                pieChart.setTransparentCircleRadius(55f);
                Description description = new Description();
                description.setText(""); //라벨
                description.setTextSize(15);
                pieChart.setDescription(description);
                pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션 효과

                // 여기서 ticket이 다음 날 넘어가요!!
                ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        String arr_todo = null;
                        int arr_time = 0;
                        for(DataSnapshot fileSnapshot2 : fileSnapshot.getChildren()){
                            ticket = new Ticket();
                            ticket = fileSnapshot2.getValue(Ticket.class);

                            arr_todo = ticket.getTodo();

                            //비율계산//
                            String[] arr1 = ticket.getDepart_time().split(":"); //출발시간
                            String[] arr2 = ticket.getArrive_time().split(":"); //도착시간
                            int depart_hour=Integer.parseInt(arr1[0]);//출발시간 시
                            int depart_minute=Integer.parseInt(arr1[1]);//출발시간 분
                            int arrive_hour=Integer.parseInt(arr2[0]); //도착시간 시
                            int arrive_minute=Integer.parseInt(arr2[1]); //도착시간 분

                            int term_hour = arrive_hour-depart_hour;
                            int term_minute = arrive_minute-depart_minute;
                            //------//

                            arr_time= term_hour*60+term_minute; //시간을 분으로 계산 (예를들어 1시간 30분-> 90분으로 계산)

                            System.out.println(ticket);
                            System.out.println("출발시간"+ticket.getDepart_time());
                            System.out.println("도착시간"+ticket.getArrive_time());
                            System.out.println("계산한시간"+arr_time);
                            System.out.println("---조건문 끝---");
                            yValues.add(new PieEntry(arr_time,arr_todo)); // getTodo 읽어와서 파이차트 엔트리에 추가하는 곳

                        }
                    }
                }

                PieDataSet dataSet = new PieDataSet(yValues,"<Todo List>");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

                PieData data = new PieData((dataSet));
                data.setValueTextSize(10f);
                data.setValueTextColor(Color.DKGRAY);

                pieChart.setEntryLabelColor(Color.BLACK); //Todo 글씨 색깔
                pieChart.setEntryLabelTextSize(10f); //Todo 글씨 사이즈
                pieChart.setData(data);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });

    }

}
