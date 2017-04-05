package com.example.andylin.homepackagemonitor.Views.Interfaces;

import com.example.andylin.homepackagemonitor.Views.Adapters.PageAdapter;

/**
 * Created by Andy Lin on 2017-04-04.
 */

public interface MainView {
    void setActionBarTitle(String title);
    void setNavDrawerSelectedItem(int resID);
    void showViewPager(PageAdapter pageAdapter);
}
