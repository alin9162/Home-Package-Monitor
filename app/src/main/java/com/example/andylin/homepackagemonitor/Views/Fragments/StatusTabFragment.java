package com.example.andylin.homepackagemonitor.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.example.andylin.homepackagemonitor.R;

/**
 * Created by Andy Lin on 2017-03-18.
 */

public class StatusTabFragment extends Fragment {
    private static final String TAG = "StatusTabFragment";

    private static final int BLUETOOTH_REQUEST = 1;
    private boolean socketConnected = false;
    private BluetoothAdapter mBluetoothAdapter;
    public BluetoothDevice aNewdevice;
    private BluetoothSocket mmSocket = null;
    public static InputStream mmInStream = null;
    public static OutputStream mmOutStream = null;
    private boolean Connected = false;

    private Button mUnlockButton;
    private Button mLockButton;
    private Button mEnableBluetoothButton;
    private LinearLayout mLockUnlockLayout;
    private LinearLayout mEnableBluetoothLayout;
    BluetoothDevice ourDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_status_tab_fragment, container, false);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "There is no Bluetooth on this device.", Toast.LENGTH_LONG).show();
        }

        mLockUnlockLayout = (LinearLayout) view.findViewById(R.id.lock_unlock_layout);
        mEnableBluetoothLayout = (LinearLayout) view.findViewById(R.id.enable_bt_layout);

        mEnableBluetoothButton = (Button) view.findViewById(R.id.enable_bt_button);
        mEnableBluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectBluetooth()) {
                    mLockUnlockLayout.setVisibility(View.VISIBLE);
                    mEnableBluetoothLayout.setVisibility(View.GONE);
                }
            }
        });

        mUnlockButton = (Button) view.findViewById(R.id.unlock_button);
        mUnlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100; i++) {
                    WriteToBTDevice("uuuuuuuu");
                }
            }
        });

        mLockButton = (Button) view.findViewById(R.id.lock_button);
        mLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100; i++) {
                    WriteToBTDevice("llllllll");
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Connected && mBluetoothAdapter != null) {
            mLockUnlockLayout.setVisibility(View.VISIBLE);
            mEnableBluetoothLayout.setVisibility(View.GONE);
        }
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
                Toast.makeText(getActivity(), "Stuck in lockDevice function", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(), "Socket Creation Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void ConnectToSerialBlueToothDevice() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
        try {
            // Attempt connection to the device through the socket.
            mmSocket.connect();
            Toast.makeText(getActivity(), "Connection Made", Toast.LENGTH_LONG).show();
        } catch (IOException connectException) {
            Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_LONG).show();
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

    void closeConnection() {
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
    }

    public void checkBluetoothEnabled() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BLUETOOTH_REQUEST);
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
                Toast.makeText(getActivity(), "Bluetooth Connection Failed", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                GetInputOutputStreamsForSocket();
                return true;
            }
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible && isAdded()) {
            checkBluetoothEnabled();
        }
    }
}
