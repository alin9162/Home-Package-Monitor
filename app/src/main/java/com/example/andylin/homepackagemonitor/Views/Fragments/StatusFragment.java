package com.example.andylin.homepackagemonitor.Views.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.andylin.homepackagemonitor.AsyncTask.BluetoothTask;
import com.example.andylin.homepackagemonitor.Presenter.BluetoothPresenter;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Views.Activities.MainActivity;
import com.example.andylin.homepackagemonitor.Views.Interfaces.BluetoothView;

/**
 * Created by Andy Lin on 2017-03-18.
 */

public class StatusFragment extends Fragment implements BluetoothView{
    private static final String TAG = "StatusFragment";
    private BluetoothPresenter mBluetoothPresenter;
    private MainActivity mMainActivity;

    private LinearLayout mLockUnlockLayout;
    private Button mUnlockButton;
    private Button mLockButton;
    private Switch mSwitch;

    @Override
    public void setActionBarTitle(String title) {
        mMainActivity.getSupportActionBar().setTitle(title);
    }

    @Override
    public void setNavDrawerSelectedItem(int resID) {
        mMainActivity.setNavigationDrawerCheckedItem(resID);
    }

    @Override
    public void showUnlockLockLayout() {
        mLockUnlockLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableUnlockLockLayout() {
        mLockUnlockLayout.setVisibility(View.GONE);
        mSwitch.setChecked(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_status_tab_fragment, container, false);
        mMainActivity = (MainActivity) getActivity();
        mBluetoothPresenter = new BluetoothPresenter(mMainActivity, this);

        mSwitch = (Switch) view.findViewById(R.id.pair_switch);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    new BluetoothTask((MainActivity)getActivity(), mBluetoothPresenter).execute();
                }
                else{
                    mLockUnlockLayout.setVisibility(View.GONE);
                    mBluetoothPresenter.closeConnection();
                }
            }
        });

        mLockUnlockLayout = (LinearLayout) view.findViewById(R.id.lock_unlock_layout);

        mUnlockButton = (Button) view.findViewById(R.id.unlock_button);
        mUnlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100; i++) {
                    mBluetoothPresenter.WriteToBTDevice("uuuuuuuu");
                }
            }
        });

        mLockButton = (Button) view.findViewById(R.id.lock_button);
        mLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100; i++) {
                    mBluetoothPresenter.WriteToBTDevice("llllllll");
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBluetoothPresenter = new BluetoothPresenter(getActivity(), this);
        mBluetoothPresenter.setActionBar();
        mBluetoothPresenter.setNavDrawerSelectedItem();
        mBluetoothPresenter.checkBluetoothEnabled();
        mBluetoothPresenter.checkConnected();
    }
}
