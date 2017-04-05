package com.example.andylin.homepackagemonitor.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Views.Interfaces.PendingTabView;
import com.example.andylin.homepackagemonitor.Volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andy Lin on 2017-04-04.
 */

public class PendingTabPresenter {
    private final String TAG = "PendingTabPresenter";
    private Activity mActivity;
    private PendingTabView mPendingTabView;

    public PendingTabPresenter(Activity activity, PendingTabView pendingTabView){
        this.mActivity = activity;
        this.mPendingTabView = pendingTabView;
    }

    public void setActionBar(){
        mPendingTabView.setActionBarTitle("Home");
    }

    public void setNavDrawerSelectedItem(){
        mPendingTabView.setNavDrawerSelectedItem(R.id.nav_home);
    }

    public void checkPendingRequests(){
        SharedPreferences loginSettings = mActivity.getSharedPreferences("PreferenceFile", Context.MODE_PRIVATE);
        String username = loginSettings.getString("username", "");
        String password = loginSettings.getString("password", "");
        String deviceid = loginSettings.getString("deviceid", "");

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("deviceid", deviceid);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        String url = mActivity.getResources().getString(R.string.serverip) + "checkpending";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseCheckPending(response);
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

    public void parseCheckPending(JSONObject response){
        boolean isPending = false;
        try{
            isPending = response.getBoolean("pendingrequest");
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        if (isPending){
            Log.e(TAG, "Calling get status");
            SharedPreferences loginSettings = mActivity.getSharedPreferences("PreferenceFile", Context.MODE_PRIVATE);
            String username = loginSettings.getString("username", "");
            String password = loginSettings.getString("password", "");
            String deviceid = loginSettings.getString("deviceid", "");

            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("username", username);
                jsonObject.put("password", password);
                jsonObject.put("deviceid", deviceid);
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            String url = mActivity.getResources().getString(R.string.serverip) + "getstatus";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    parseGetStatus(response);
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
    }

    public void parseGetStatus(JSONObject response){
        String imageBytes = "";
        boolean isPending = false;
        try{
            isPending = response.getBoolean("pendingrequest");
            imageBytes = response.getString("imagebytes");
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        if (isPending){
            mPendingTabView.showPendingRequest(imageBytes);
        }
        else {
            mPendingTabView.showNoPendingRequest();
        }
    }

    public void acknowledge(boolean request){
        SharedPreferences loginSettings = mActivity.getSharedPreferences("PreferenceFile", mActivity.MODE_PRIVATE);
        String username = loginSettings.getString("username", "");
        String password = loginSettings.getString("password", "");
        String deviceID = loginSettings.getString("deviceid", "");

        final boolean approveStatus = request;

        String url = mActivity.getResources().getString(R.string.serverip) + "acknowledge";
        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("deviceid", deviceID);
            jsonObject.put("approved", request ? "true" : "false");
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mPendingTabView.hidePendingRequest();
                if (approveStatus){
                    Toast.makeText(mActivity, "Opening box...", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(mActivity, "Access Declined", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonObject.toString().getBytes();
            }
        };
        // Access the RequestQueue through the singleton class to add the request to the request queue
        VolleySingleton.getInstance(mActivity).getRequestQueue().add(stringRequest);
    }
}
