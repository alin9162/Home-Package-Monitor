package com.example.andylin.homepackagemonitor.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.andylin.homepackagemonitor.Presenter.BluetoothPresenter;
import com.example.andylin.homepackagemonitor.Views.Activities.MainActivity;

/**
 * Created by Andy Lin on 2017-04-05.
 */

public class BluetoothTask extends AsyncTask<String, Void, Boolean>{
    private static final String TAG = "BluetoothTask";
    private MainActivity mActivity;
    private BluetoothPresenter mBluetoothPresenter;
    private ProgressDialog mProgressDialog;

    public BluetoothTask(MainActivity activity, BluetoothPresenter bluetoothPresenter){
        this.mActivity = activity;
        mProgressDialog = new ProgressDialog(mActivity);
        mBluetoothPresenter = bluetoothPresenter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Pairing to HPM...");
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean isSuccessful) {
        super.onPostExecute(isSuccessful);
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (isSuccessful) {
            Toast.makeText(mActivity, "Successfully connected.", Toast.LENGTH_SHORT).show();
            mBluetoothPresenter.showUnlockLockLayout();
        }
        else {
            Toast.makeText(mActivity, "Connection failed.", Toast.LENGTH_SHORT).show();
            mBluetoothPresenter.disableUnlockLockLayout();
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            return mBluetoothPresenter.connectBluetooth();
        } catch (Exception e){
            Log.e(TAG, e.toString());
            return false;
        }
    }
}
