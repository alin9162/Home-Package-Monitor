package com.example.andylin.homepackagemonitor.Views.Interfaces;

import android.widget.ArrayAdapter;

import com.example.andylin.homepackagemonitor.Views.Adapters.DeviceListAdapter;

/**
 * Created by Andy Lin on 2017-04-04.
 */

public interface SettingsView {
    void setActionBarTitle(String title);
    void setNavDrawerSelectedItem(int resID);
    void showDeviceList(DeviceListAdapter deviceListAdapter);
    void showSpinner(ArrayAdapter arrayAdapter, int index);
}
