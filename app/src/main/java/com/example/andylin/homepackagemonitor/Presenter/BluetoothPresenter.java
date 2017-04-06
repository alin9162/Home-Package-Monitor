package com.example.andylin.homepackagemonitor.Presenter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Views.Interfaces.BluetoothView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Andy Lin on 2017-04-05.
 */

public class BluetoothPresenter {
    private static final int BLUETOOTH_REQUEST = 1;
    private boolean socketConnected = false;
    private BluetoothAdapter mBluetoothAdapter;
    public BluetoothDevice aNewdevice;
    private BluetoothSocket mmSocket = null;
    public static InputStream mmInStream = null;
    public static OutputStream mmOutStream = null;
    private boolean Connected = false;
    BluetoothDevice ourDevice;


    private Activity mActivity;
    private BluetoothView mBluetoothView;
    private final String TAG = "BluetoothPresenter";

    public BluetoothPresenter(Activity activity, BluetoothView bluetoothView){
        this.mActivity = activity;
        this.mBluetoothView = bluetoothView;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(mActivity, "There is no Bluetooth on this device.", Toast.LENGTH_LONG).show();
        }
    }

    public void setActionBar(){
        mBluetoothView.setActionBarTitle("Control");
    }

    public void setNavDrawerSelectedItem(){
        mBluetoothView.setNavDrawerSelectedItem(R.id.nav_status);
    }

    public BluetoothDevice getDevice() {
        if (mBluetoothAdapter != null) {
            if (Connected) {
                return aNewdevice;
            } else {
                Set<BluetoothDevice> thePairedDevices = mBluetoothAdapter.getBondedDevices();

                if (thePairedDevices.size() > 0) {
                    Iterator<BluetoothDevice> iter = thePairedDevices.iterator();

                    while (iter.hasNext()) {
                        aNewdevice = iter.next();

                        String PairedDevice = new String(aNewdevice.getName());

                        if (PairedDevice.equals("HPM")) {
                            return aNewdevice;
                        }
                    }
                }
                Toast.makeText(mActivity, "Stuck in lockDevice function", Toast.LENGTH_LONG).show();
                return aNewdevice;
            }
        } else {
            return null;
        }
    }

    public void CreateSerialBluetoothDeviceSocket(BluetoothDevice device) {
        if (socketConnected == true) {
            return;
        }

        socketConnected = true;

        mmSocket = null;

        // universal UUID for a serial profile RFCOMM blue tooth device
        // this is just one of those “things” that you have to do and just works
        UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        // Get a Bluetooth Socket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Toast.makeText(mActivity, "Socket Creation Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void ConnectToSerialBlueToothDevice() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
        try {
            // Attempt connection to the device through the socket.
            mmSocket.connect();
        } catch (IOException connectException) {
            Log.e(TAG, "Connection Failed");
            return;
        }

        //create the input/output stream and record fact we have made a connection
        GetInputOutputStreamsForSocket(); // see page 26
        Connected = true;
    }

    public void GetInputOutputStreamsForSocket() {
        try {
            mmInStream = mmSocket.getInputStream();
            mmOutStream = mmSocket.getOutputStream();
        } catch (IOException e) {
        }
    }

    public void WriteToBTDevice(String message) {
        String s = new String("\r\n");
        byte[] msgBuffer = message.getBytes();
        byte[] newline = s.getBytes();

        try {
            mmOutStream.write(msgBuffer);
            mmOutStream.write(newline);
        } catch (IOException e) {
        }
    }

    public void closeConnection() {
        if (mmInStream == null || mmOutStream == null || mmSocket == null){
            return;
        }
        try {
            mmInStream.close();
            mmInStream = null;
        } catch (IOException e) {

        }
        try {
            mmOutStream.close();
            mmOutStream = null;
        } catch (IOException e) {
        }
        try {
            mmSocket.close();
            mmSocket = null;
        } catch (IOException e) {
        }

        Connected = false;
        socketConnected = false;
    }

    public void checkBluetoothEnabled() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivity(enableBtIntent);
        }
    }

    public boolean connectBluetooth() {
        ourDevice = getDevice();
        if (ourDevice == null) {
            checkBluetoothEnabled();
            return true;
        } else {
            CreateSerialBluetoothDeviceSocket(aNewdevice);
            int count = 0;

            while (Connected == false && count < 5) {
                ConnectToSerialBlueToothDevice();
                count++;
            }

            if (Connected == false) {
                return false;
            } else {
                GetInputOutputStreamsForSocket();
                return true;
            }
        }
    }

    public void checkConnected(){
        if (Connected && mBluetoothAdapter != null) {
            mBluetoothView.showUnlockLockLayout();
        }
    }

    public void showUnlockLockLayout(){
        mBluetoothView.showUnlockLockLayout();
    }

    public void disableUnlockLockLayout(){
        mBluetoothView.disableUnlockLockLayout();
    }
}
