package com.example.andylin.homepackagemonitor.Views.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Andy Lin on 2017-03-17.
 */

public class PageAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    public List<Fragment> fragmentList;
    private String tabTitles[] = new String[] { "Pending", "History", "Control" };

    public PageAdapter (FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragmentList == null){
            return null;
        }
        else {
            return fragmentList.get(position);
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
