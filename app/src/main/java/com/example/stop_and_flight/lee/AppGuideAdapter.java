<<<<<<< HEAD:app/src/main/java/com/example/stop_and_flight/lee/AppGuideAdapter.java
package com.example.stop_and_flight.lee;
=======
package com.example.stop_and_flight.utils;
>>>>>>> origin/develop:app/src/main/java/com/example/stop_and_flight/utils/AppGuideAdapter.java

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AppGuideAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public void addFragment(Fragment fragment, String title)
    {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    public CharSequence getPageTitle(int position)
    {
        return mFragmentTitleList.get(position);
    }
    public AppGuideAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return mFragmentList.get(position);
    }
    @Override
    public int getCount()
    {
        return mFragmentList.size() ;
    }

}
