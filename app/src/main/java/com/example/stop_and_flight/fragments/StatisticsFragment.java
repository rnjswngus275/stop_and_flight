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

}





