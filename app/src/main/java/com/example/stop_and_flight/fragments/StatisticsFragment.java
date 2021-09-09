package com.example.stop_and_flight.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.stop_and_flight.R;

import java.util.ArrayList;



public class StatisticsFragment extends Fragment {


    ViewPager pager;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view1 = inflater.inflate(R.layout.fragment_statistics1, container, false);

        pager = view1.findViewById(R.id.pager);
        //페이지 갯수지정
        pager.setOffscreenPageLimit(2);
        //어뎁터 객체생성
        MyPagerAdapter adapter = new MyPagerAdapter(getFragmentManager());
        PieChartFragment pieChartfragment = new PieChartFragment();
        adapter.addItem(pieChartfragment);
        BarChartFragment barChartfragment = new BarChartFragment();
        adapter.addItem(barChartfragment);
        pager.setAdapter(adapter);
        return view1;
    }



    class MyPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> items = new ArrayList<Fragment>();
        public MyPagerAdapter(FragmentManager fm){
            super(fm);
        }
        public void addItem(Fragment item){
            items.add(item);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Todo별 공부량";
                case 1:
                    return "일간/주간 공부량";
                default:
                    return null;
            }
        }

    }
//
//
//    private void barChart()
//    {
//        ArrayList<BarEntry> barEntry = new ArrayList<>();
//        barEntry.add(new BarEntry(1f, 50));
//        barEntry.add(new BarEntry(2f,40));
//        barEntry.add(new BarEntry(3f,20));
//        barEntry.add(new BarEntry(4f,90));
//        barEntry.add(new BarEntry(5f, 30));
//        barEntry.add(new BarEntry(6f, 20));
//        barEntry.add(new BarEntry(7f, 10));
//        barEntry.add(new BarEntry(8f, 40));
//        barEntry.add(new BarEntry(9f, 80));
//        barEntry.add(new BarEntry(10f, 20));
//        barEntry.add(new BarEntry(11f, 50));
//        barEntry.add(new BarEntry(12f, 60));
//
//        BarDataSet dataSet = new BarDataSet(barEntry,"월별 비행시간");
//        BarData data = new BarData(dataSet);
//
//        barChart.setData(data);
//        barChart.animateY(1000, Easing.EaseInOutCubic); //애니메이션
//        barChart.invalidate();
//
//        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS); //데이터 색깔
//        dataSet.setValueTextSize(10f);
//        dataSet.setValueTextColor(Color.DKGRAY);
//
//
//        ArrayList<String> xLabels = new ArrayList<>();
//        xLabels.add("1월");
//        xLabels.add("2월");
//        xLabels.add("3월");
//        xLabels.add("4월");
//        xLabels.add("5월");
//        xLabels.add("6월");
//        xLabels.add("7월");
//        xLabels.add("8월");
//        xLabels.add("9월");
//        xLabels.add("10월");
//        xLabels.add("11월");
//        xLabels.add("12월");
//
//
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//        xAxis.setGranularity(1f);
//        xAxis.setGranularityEnabled(true);
//        xAxis.setCenterAxisLabels(false);
//        xAxis.setSpaceMax(0.5f);
//
//        xAxis.setDrawAxisLine(true);
//        xAxis.setEnabled(true);
//        dataSet.setDrawValues(true);
//
//        barChart.getDescription().setEnabled(true);
//        Description description = new Description();
//        description.setText("월별 비행시간");
//        barChart.setDescription(description);
//    }
}





