package com.example.stop_and_flight.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stop_and_flight.R;
import com.example.stop_and_flight.model.CurTime;
import com.example.stop_and_flight.model.DateInfo;
import com.example.stop_and_flight.model.Ticket;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarChartFragment extends Fragment {
    BarChart barChart;
    public ArrayList<Integer> time_list = new ArrayList<Integer>();
    private com.github.mikephil.charting.charts.BarChart showbarchart;
    private ArrayList<DateInfo> dateInfos = new ArrayList<DateInfo>();
    private HashMap<String, Object> UserMap;
    private String UID;
    private Ticket ticket;
    private DatabaseReference databaseReference;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BarChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static BarChartFragment newInstance(String param1, String param2) {
        BarChartFragment fragment = new BarChartFragment();
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
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        barChart = (BarChart) view.findViewById(R.id.barchart);

        ArrayList<BarEntry> barEntry = new ArrayList<>();
        ArrayList<String> week = new ArrayList<>();
        TextView week_total_time= (TextView)view.findViewById(R.id.textView30);
        TextView today_total_time=(TextView)view.findViewById(R.id.textView28);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        databaseReference =  FirebaseDatabase.getInstance().getReference();
        databaseReference.child("TICKET").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                time_list.clear();
                int arr_time = 0;
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {

                        for (DataSnapshot fileSnapshot2 : fileSnapshot.getChildren()) {

                            int[] Arr_times = new int[7];
                            ticket = new Ticket();
                            ticket = fileSnapshot2.getValue(Ticket.class);

                            //비율계산//
                            String[] arr1 = ticket.getDepart_time().split(":"); //출발시간
                            String[] arr2 = ticket.getArrive_time().split(":"); //도착시간
                            int depart_hour = Integer.parseInt(arr1[0]);//출발시간 시
                            int depart_minute = Integer.parseInt(arr1[1]);//출발시간 분
                            int arrive_hour = Integer.parseInt(arr2[0]); //도착시간 시
                            int arrive_minute = Integer.parseInt(arr2[1]); //도착시간 분

                            int term_hour = arrive_hour - depart_hour;
                            int term_minute = arrive_minute - depart_minute;

                            //------//

                            arr_time = term_hour * 60 + term_minute; //시간을 분으로 계산 (예를들어 1시간 30분-> 90분으로 계산)
                            String arr_times = arr_time + "분";


                            time_list.add(arr_time);
                            System.out.println("비행시간은" + arr_time);
                            int i = 1;
                            barEntry.add(new BarEntry(i, arr_time));
                            System.out.println("---로그시작---");
                            System.out.println("i=" + i);
                            System.out.println("값=" + arr_time);
                            System.out.println("---로그끝---");

                            final String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; // Your List / array with String Values For X-axis Labels

// Set the value formatter
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(weekdays));
                            week_total_time.setText(arr_times);
                            today_total_time.setText(arr_times);
                        }
                    }
/*                    for(int i=1;i<=7;i++){



                    }*/


                }
                BarDataSet barDataSet = new BarDataSet(barEntry, "주별 비행시간");
                barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);
                BarData barData = new BarData(barDataSet);
                barChart.setFitBars(true);
                barChart.setData(barData);
                barChart.animateY(2000);
                barChart.setDrawGridBackground(false);
                barChart.setTouchEnabled(false);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });
        return view;
    }

}
