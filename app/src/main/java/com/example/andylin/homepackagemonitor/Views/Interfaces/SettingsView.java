package com.example.andylin.homepackagemonitor.Views.Interfaces;

import com.example.andylin.homepackagemonitor.Views.Adapters.DeviceListAdapter;

/**
 * Created by Andy Lin on 2017-04-04.
 */

public interface SettingsView {
    void setActionBarTitle(String title);
    void setNavDrawerSelectedItem(int resID);
    void showDeviceList(DeviceListAdapter deviceListAdapter);
}
