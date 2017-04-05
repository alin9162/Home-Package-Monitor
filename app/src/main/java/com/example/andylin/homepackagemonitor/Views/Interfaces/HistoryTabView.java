package com.example.andylin.homepackagemonitor.Views.Interfaces;

import com.example.andylin.homepackagemonitor.Views.Adapters.GridImageAdapter;
import com.example.andylin.homepackagemonitor.Views.Adapters.HistoryListAdapter;

/**
 * Created by Andy Lin on 2017-04-04.
 */

public interface HistoryTabView {
    void setActionBarTitle(String title);
    void setNavDrawerSelectedItem(int resID);
    void showGridView(GridImageAdapter gridImageAdapter);
    void showListView(HistoryListAdapter historyListAdapter);
}
