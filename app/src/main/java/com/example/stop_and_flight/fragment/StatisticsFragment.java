package com.example.stop_and_flight.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.stop_and_flight.R;
import com.example.stop_and_flight.StatisticsViewPagerAdapter;
import com.example.stop_and_flight.ViewPagerAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;


public class StatisticsFragment extends Fragment {

    PieChart pieChart;
    Switch sw;
    private ViewPager viewPager;
    private StatisticsViewPagerAdapter pagerAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics1, container, false);
        pieChart = (PieChart) view.findViewById(R.id.piechart);
        pieChart();
        sw=(Switch)view.findViewById(R.id.sw);
        sw.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      if(sw.isChecked()){
                                          //파이차트
                                      }
                                      else{
                                          //바차트
                                      }
                                  }
                              }
        );
        return view;
    }

    private void pieChart()
    {

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.setTransparentCircleRadius(55f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(34f,"컴활1급 실기"));
        yValues.add(new PieEntry(23f,"정보처리기사 필기"));
        yValues.add(new PieEntry(14f,"토익"));
        yValues.add(new PieEntry(35f,"졸업작품"));
        yValues.add(new PieEntry(20f,"주식 공부"));

        Description description = new Description();
        description.setText(""); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"<Todo List>");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.DKGRAY);

        pieChart.setEntryLabelColor(Color.BLACK); //Todo 글씨 색깔
        pieChart.setEntryLabelTextSize(13f); //Todo 글씨 사이즈
        pieChart.setData(data);
    }
    private void LineChart() {
    }


}
