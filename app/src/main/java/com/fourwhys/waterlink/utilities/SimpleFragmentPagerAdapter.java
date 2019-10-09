package com.fourwhys.waterlink.utilities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.fourwhys.waterlink.fragments.BarChartFragment;
import com.fourwhys.waterlink.fragments.RealTimeChart;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {


    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        if (position == 0) {
            fragment =  new BarChartFragment();
        }
        else if(position == 1) {
            fragment =  new RealTimeChart();
        }
        return fragment;
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Real Time";
            case 1:
                return "Bar chart";
            default:
                return "Real time";
        }
    }
}
