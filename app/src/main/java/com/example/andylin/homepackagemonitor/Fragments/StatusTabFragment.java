package com.example.andylin.homepackagemonitor.Fragments;

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

public class StatusTabFragment extends Fragment{
    private static final String TAG = "StatusTabFragment";
    private boolean socketConnected = false;
    private BluetoothAdapter mBluetoothAdapter;
    public BluetoothDevice aNewdevice;
    private BluetoothSocket mmSocket = null;
    public static InputStream mmInStream = null;
    public static OutputStream mmOutStream = null;
    private boolean Connected = false;

    private LinearLayout unlockButton;
    private LinearLayout lockButton;
    BluetoothDevice ourDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_status_tab_fragment, container, false);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // check to see if your android device even has a bluetooth device !!!!,
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "No Bluetooth !!", Toast.LENGTH_LONG).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }



        unlockButton = (LinearLayout) view.findViewById(R.id.unlock_button);
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ourDevice = lockDevice();
                CreateSerialBluetoothDeviceSocket(aNewdevice);
                if(Connected == false) {
                    ConnectToSerialBlueToothDevice();
                }

                GetInputOutputStreamsForSocket();
                WriteToBTDevice("uuuuuuuu");
                //closeConnection();

            }
        });
        lockButton = (LinearLayout) view.findViewById(R.id.lock_button);
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ourDevice = lockDevice();
                CreateSerialBluetoothDeviceSocket(aNewdevice);

                if(Connected == false) {
                    ConnectToSerialBlueToothDevice();
                }

                GetInputOutputStreamsForSocket();
                WriteToBTDevice("llllllll");
                //closeConnection();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "Refreshing Status Tab");
    }

    public BluetoothDevice lockDevice() {

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









    public void CreateSerialBluetoothDeviceSocket(BluetoothDevice device) {
        if(socketConnected == true) {
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

    public void refreshFragment(){

    }
}
