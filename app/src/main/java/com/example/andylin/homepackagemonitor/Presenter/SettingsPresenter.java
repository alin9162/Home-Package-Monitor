package com.example.andylin.homepackagemonitor.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Views.Adapters.DeviceListAdapter;
import com.example.andylin.homepackagemonitor.Views.Interfaces.SettingsView;
import com.example.andylin.homepackagemonitor.Volley.CustomJSONArrayRequest;
import com.example.andylin.homepackagemonitor.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Lin on 2017-04-04.
 */

public class SettingsPresenter {
    private final String TAG = "SettingsPresenter";
    private Activity mActivity;
    private SettingsView mSettingsView;

    public SettingsPresenter(Activity activity, SettingsView settingsView){
        this.mActivity = activity;
        this.mSettingsView = settingsView;
    }

    public void setActionBar(){
        mSettingsView.setActionBarTitle("Settings");
    }

    public void setNavDrawerSelectedItem(){
        mSettingsView.setNavDrawerSelectedItem(R.id.nav_settings);
    }

    public void populateDeviceList(){
        SharedPreferences loginSettings = mActivity.getSharedPreferences("PreferenceFile", Context.MODE_PRIVATE);
        String username = loginSettings.getString("username", "");

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", username);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        String url = mActivity.getResources().getString(R.string.serverip) + "viewdevices";

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
        VolleySingleton.getInstance(mActivity).getRequestQueue().add(jsonArrayRequest);
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

        DeviceListAdapter deviceListAdapter = new DeviceListAdapter(devicesList, mActivity);
        mSettingsView.showDeviceList(deviceListAdapter);
    }

    public void addDevice(String deviceName){
        SharedPreferences loginSettings = mActivity.getSharedPreferences("PreferenceFile", Context.MODE_PRIVATE);
        String username = loginSettings.getString("username", "");

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", username);
            jsonObject.put("deviceid", deviceName);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        String url = mActivity.getResources().getString(R.string.serverip) + "adddevice";

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
        VolleySingleton.getInstance(mActivity).getRequestQueue().add(jsonObjectRequest);
    }

    public void parseAddDevice(JSONObject response){
        try {
            Toast.makeText(mActivity, response.getString("result"), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Log.e("ADD DEVICE", e.getMessage());
        }
        populateDeviceList();
    }

    public void changePhoneNumber(String phoneNumber) {
        SharedPreferences loginSettings = mActivity.getSharedPreferences("PreferenceFile", Context.MODE_PRIVATE);
        String username = loginSettings.getString("username", "");
        String password = loginSettings.getString("password", "");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("phonenumber", phoneNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = mActivity.getResources().getString(R.string.serverip) + "updatenumber";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());

                String result = "";
                try{
                    result = response.getString("result");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

                if (!result.equals("Succesfully updated phone number")){
                    Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Access the RequestQueue through the singleton class to add the request to the request queue
        VolleySingleton.getInstance(mActivity).getRequestQueue().add(jsonObjectRequest);
    }
}
