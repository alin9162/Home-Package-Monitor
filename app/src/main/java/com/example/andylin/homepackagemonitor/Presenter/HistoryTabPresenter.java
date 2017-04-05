package com.example.andylin.homepackagemonitor.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.andylin.homepackagemonitor.Model.BoxHistory;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Views.Adapters.GridImageAdapter;
import com.example.andylin.homepackagemonitor.Views.Adapters.HistoryListAdapter;
import com.example.andylin.homepackagemonitor.Views.Interfaces.HistoryTabView;
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

public class HistoryTabPresenter {
    private Activity mActivity;
    private HistoryTabView mHistoryTabView;
    private final String TAG = "HomePresenter";

    public HistoryTabPresenter(Activity activity, HistoryTabView historyTabView){
        this.mActivity = activity;
        this.mHistoryTabView = historyTabView;
    }

    public void setActionBar(){
        mHistoryTabView.setActionBarTitle("History");
    }

    public void setNavDrawerSelectedItem(){
        mHistoryTabView.setNavDrawerSelectedItem(R.id.nav_history);
    }

    public void getCameraImage(){
        SharedPreferences loginSettings = mActivity.getSharedPreferences("PreferenceFile", Context.MODE_PRIVATE);
        String username = loginSettings.getString("username", "");
        String deviceid = loginSettings.getString("deviceid", "");

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", username);
            jsonObject.put("deviceid", deviceid);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        String url = mActivity.getResources().getString(R.string.serverip) + "viewlog";

        CustomJSONArrayRequest jsonArrayRequest = new CustomJSONArrayRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parseJSONArray(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Access the RequestQueue through the singleton class to add the request to the request queue
        VolleySingleton.getInstance(mActivity).getRequestQueue().add(jsonArrayRequest);
    }

    public void parseJSONArray(JSONArray jsonArray){
        List<BoxHistory> boxHistoryList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String dateAccessed = jsonObject.getString("datetime");
                Boolean wasGranted = jsonObject.getBoolean("wasgranted");
                String image = jsonObject.getString("imagebytes");
                int id = jsonObject.getInt("id");
                BoxHistory boxHistory = new BoxHistory(dateAccessed, wasGranted, image.substring(10), id);
                boxHistoryList.add(boxHistory);
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }

        mHistoryTabView.showGridView(new GridImageAdapter(mActivity, boxHistoryList));
        mHistoryTabView.showListView(new HistoryListAdapter(boxHistoryList, mActivity));
    }
}
