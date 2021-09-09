package com.example.stop_and_flight.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stop_and_flight.R;
import com.example.stop_and_flight.model.CurTime;
import com.example.stop_and_flight.model.DateInfo;
import com.example.stop_and_flight.model.Ticket;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
        CurTime curTime = new CurTime();
        showbarchart = (BarChart) view.findViewById(R.id.barchart);
        barChart = (BarChart) view.findViewById(R.id.barchart);
        showbarchart.setVisibility(PieChart.VISIBLE);
        int curIntTime = curTime.getIntYear() * 10000 + curTime.getIntMonth() * 100 + curTime.getIntDay() * 1;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }

        ArrayList<BarEntry> barEntry = new ArrayList<>();
        ArrayList<String> week = new ArrayList<>();
        TextView week_total_time= (TextView)view.findViewById(R.id.textView30);
        TextView today_total_time=(TextView)view.findViewById(R.id.textView28);
        databaseReference =  FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                String weektotalTime = null;
                String todaytotalTime=null;
                if (snapshot!=null) {
                    if (snapshot.child("date").getValue() != null) {

                        UserMap = (HashMap<String, Object>) snapshot.child("date").getValue();
                        for (String day : curTime.getCurWeek()) {
                            float i = 0f;
                            int Studytime = Integer.parseInt(String.valueOf(UserMap.getOrDefault(day, 1)));
                            System.out.println(Studytime);
                            sum = sum + Studytime;
                            weektotalTime= sum +"분";
                            todaytotalTime=Studytime+"분";

                            barEntry.add(new BarEntry(i++, Studytime));
                            /*week.add(day);*/
                        }

                    }

                    BarDataSet dataSet = new BarDataSet(barEntry,"주별 비행시간");
                    dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                    BarData data = new BarData(dataSet);

                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.BLACK);
                    data.setBarWidth(0.15f);//너비
                    Description description = new Description();
                    description.setText(""); //라벨
                    description.setTextSize(15);
                    description.setEnabled(false);

                    barChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션
                    barChart.setData(data);
                    barChart.setDrawGridBackground(false);
                    barChart.setTouchEnabled(false);

                    final ArrayList<String> xAxisLabel = new ArrayList<>();
                    xAxisLabel.add("Mon");
                    xAxisLabel.add("Tue");
                    xAxisLabel.add("Wed");
                    xAxisLabel.add("Thu");
                    xAxisLabel.add("Fri");
                    xAxisLabel.add("Sat");
                    xAxisLabel.add("Sun");
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            return xAxisLabel.get((int) value);

                        }
                    });

                    week_total_time.setText(weektotalTime);
                    today_total_time.setText(todaytotalTime);
                }
//                barChart.notifyDataSetChanged();
//                barChart.invalidate();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }
//    private void barChart(LayoutInflater inflater, ViewGroup container,
//                          Bundle savedInstanceState)
//    {
//
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
//        if(user != null){
//            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
//        }
//
//        databaseReference =  FirebaseDatabase.getInstance().getReference();
//        databaseReference.child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // 여기서 ticket이 다음 날 넘어가요!!
//                ArrayList<DateInfo> dateinfo =new ArrayList<>();
//                if (snapshot!=null) {
//                    if (snapshot.child("date").getValue() != null)
//                    {
//                        UserMap = (HashMap<String, Object>) snapshot.child("date").getValue();
//                        for (String day : curTime.getCurWeek()){
//                            int Studytime = Integer.parseInt(String.valueOf(UserMap.getOrDefault(day, 0)));
//
//                            //여기부터 데이터 그래프그리는 부분//
//                            barEntry.add(new BarEntry(Float.parseFloat(day),Studytime));
//                            /*xLabels.add(day); //x축*/
//                        }
//                    }
//                    barChart.notifyDataSetChanged();
//                    barChart.invalidate();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
}