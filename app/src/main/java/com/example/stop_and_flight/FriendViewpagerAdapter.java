package com.example.stop_and_flight;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class FriendViewpagerAdapter extends FragmentPagerAdapter {


    public FriendViewpagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_friend1();
            case 1:
                return new Fragment_friend2();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
