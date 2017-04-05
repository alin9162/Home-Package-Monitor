package com.example.andylin.homepackagemonitor.Views.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.andylin.homepackagemonitor.Presenter.SettingsPresenter;
import com.example.andylin.homepackagemonitor.Views.Adapters.DeviceListAdapter;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Views.Activities.MainActivity;
import com.example.andylin.homepackagemonitor.Views.Interfaces.SettingsView;


/**
 * Created by Andy Lin on 2017-03-14.
 */

public class SettingsFragment extends Fragment implements SettingsView{
    private static final String TAG = "SettingsFragment";
    private SettingsPresenter mSettingsPresenter;
    private MainActivity mMainActivity;

    private Button mAccountSettingsButton;
    private LinearLayout mAccountLayout;
    private EditText mPhoneNumberEditText;

    private Button mDevicesButton;
    private Button mAddDeviceButton;
    private LinearLayout mDevicesLayout;
    private EditText mAddDeviceEditText;
    private RecyclerView mRecyclerView;

    @Override
    public void setActionBarTitle(String title) {
        mMainActivity.getSupportActionBar().setTitle(title);
    }

    @Override
    public void setNavDrawerSelectedItem(int resID) {
        mMainActivity.setNavigationDrawerCheckedItem(resID);
    }

    @Override
    public void showDeviceList(DeviceListAdapter deviceListAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(deviceListAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSettingsPresenter.setActionBar();
        mSettingsPresenter.setNavDrawerSelectedItem();
        mSettingsPresenter.populateDeviceList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_settings_fragment, container, false);
        this.mSettingsPresenter = new SettingsPresenter(getActivity(), this);
        this.mMainActivity = ((MainActivity) getActivity());

        mAccountLayout = (LinearLayout) view.findViewById(R.id.account_layout);
        mAccountSettingsButton = (Button) view.findViewById(R.id.account_button);

        mPhoneNumberEditText = (EditText) view.findViewById(R.id.phone_number_entry);
        mPhoneNumberEditText.setTypeface(Typeface.DEFAULT);

        mDevicesLayout = (LinearLayout) view.findViewById(R.id.devices_layout);
        mAddDeviceEditText = (EditText) view.findViewById(R.id.add_device_edittext);

        mDevicesButton = (Button) view.findViewById(R.id.devices_button);
        mDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDevicesLayout();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.devices_list);

        mAddDeviceButton = (Button) view.findViewById(R.id.add_device_button);
        mAddDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceName = mAddDeviceEditText.getText().toString();
                if (deviceName.equals("") || deviceName == null){
                    Toast.makeText(getActivity(), "Please enter a device ID first", Toast.LENGTH_SHORT).show();
                }
                else {
                    mSettingsPresenter.addDevice(deviceName);
                }
                mAddDeviceEditText.clearFocus();
            }
        });

        mAccountSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAccountLayout();
            }
        });
        return view;
    }

    public void toggleDevicesLayout(){
        if(mDevicesLayout.getVisibility() == View.VISIBLE){
            mDevicesLayout.setVisibility(View.GONE);
            mDevicesButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_devices, 0, R.mipmap.dropdown_carrot, 0);

        }
        else {
            mDevicesLayout.setVisibility(View.VISIBLE);
            mDevicesButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_devices, 0, R.mipmap.upward_arrow, 0);
        }
    }

    public void toggleAccountLayout(){
        if(mAccountLayout.getVisibility() == View.VISIBLE){
            mAccountLayout.setVisibility(View.GONE);
            mAccountSettingsButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.account_logo, 0, R.mipmap.dropdown_carrot, 0);

        }
        else {
            mAccountLayout.setVisibility(View.VISIBLE);
            mAccountSettingsButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.account_logo, 0, R.mipmap.upward_arrow, 0);
        }
    }
}
