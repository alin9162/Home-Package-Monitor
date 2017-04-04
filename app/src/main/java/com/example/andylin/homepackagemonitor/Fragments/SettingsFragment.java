package com.example.andylin.homepackagemonitor.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.andylin.homepackagemonitor.Adapters.DeviceListAdapter;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.View.Activities.MainActivity;
import com.example.andylin.homepackagemonitor.Volley.CustomJSONArrayRequest;
import com.example.andylin.homepackagemonitor.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Lin on 2017-03-14.
 */

public class SettingsFragment extends Fragment{
    private static final String TAG = "SettingsFragment";

    private Button accountSettingsButton;
    private LinearLayout accountLayout;
    private boolean account_tab = false;
    private EditText phoneNumber;

    private Button devicesButton;
    private Button addDeviceButton;
    private LinearLayout devicesLayout;
    private EditText addDeviceEditText;
    private RecyclerView recyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        populateDeviceList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_settings_fragment, container, false);

        accountLayout = (LinearLayout) view.findViewById(R.id.account_layout);
        accountSettingsButton = (Button) view.findViewById(R.id.account_button);

        phoneNumber = (EditText) view.findViewById(R.id.phone_number_entry);
        phoneNumber.setTypeface(Typeface.DEFAULT);

        devicesLayout = (LinearLayout) view.findViewById(R.id.devices_layout);
        addDeviceEditText = (EditText) view.findViewById(R.id.add_device_edittext);

        devicesButton = (Button) view.findViewById(R.id.devices_button);
        devicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(devicesLayout.getVisibility() == View.VISIBLE){
                    devicesLayout.setVisibility(View.GONE);
                }
                else {
                    devicesLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.devices_list);

        addDeviceButton = (Button) view.findViewById(R.id.add_device_button);
        addDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceName = addDeviceEditText.getText().toString();
                if (deviceName.equals("") || deviceName == null){
                    Toast.makeText(getActivity(), "Please enter a device ID first", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences loginSettings = getActivity().getSharedPreferences("PreferenceFile", Context.MODE_PRIVATE);
                    String username = loginSettings.getString("username", "");

                    JSONObject jsonObject = new JSONObject();
                    try{
                        jsonObject.put("username", username);
                        jsonObject.put("deviceid", addDeviceEditText.getText().toString());
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }

                    String url = getActivity().getResources().getString(R.string.serverip) + "adddevice";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            parseAddDevice(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

                    // Access the RequestQueue through the singleton class to add the request to the request queue
                    VolleySingleton.getInstance(getActivity()).getRequestQueue().add(jsonObjectRequest);
                }
                addDeviceEditText.clearFocus();
            }
        });

        accountSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account_tab == true) {
                    accountLayout.setVisibility(View.GONE);
                    account_tab = false;
                    accountSettingsButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.account_logo, 0, R.mipmap.dropdown_carrot, 0);
                } else {
                    accountLayout.setVisibility(View.VISIBLE);
                    account_tab = true;
                    accountSettingsButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.account_logo, 0, R.mipmap.upward_arrow, 0);
                }
            }
        });
        return view;
    }

    public void parseAddDevice(JSONObject response){
        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
        populateDeviceList();
    }

    public void populateDeviceList(){
        SharedPreferences loginSettings = getActivity().getSharedPreferences("PreferenceFile", Context.MODE_PRIVATE);
        String username = loginSettings.getString("username", "");

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", username);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        String url = getResources().getString(R.string.serverip) + "viewdevices";

        CustomJSONArrayRequest jsonArrayRequest = new CustomJSONArrayRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parseDevices(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        // Access the RequestQueue through the singleton class to add the request to the request queue
        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(jsonArrayRequest);
    }

    public void parseDevices(JSONArray jsonArray){
        List<String> devicesList = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String deviceid = jsonObject.getString("deviceid");
                devicesList.add(deviceid);
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }

        DeviceListAdapter deviceListAdapter = new DeviceListAdapter(devicesList, getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(deviceListAdapter);
    }
}
