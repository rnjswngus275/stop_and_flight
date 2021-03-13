package com.example.stop_and_flight;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class AppGuideAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mData;

    public AppGuideAdapter(@NonNull FragmentManager fm) {
        super(fm);

        mData = new ArrayList<>();
        mData.add(new AppGuideFragment1());
        mData.add(new AppGuideFragment2());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }
}