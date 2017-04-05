package com.example.andylin.homepackagemonitor.Views.Interfaces;

import com.example.andylin.homepackagemonitor.Views.Adapters.PageAdapter;

/**
 * Created by Andy Lin on 2017-04-04.
 */

public interface PendingTabView {
    void setActionBarTitle(String title);
    void setNavDrawerSelectedItem(int resID);
    void showPendingRequest(String imageBytes);
    void showNoPendingRequest();
    void hidePendingRequest();
}
