package com.example.andylin.homepackagemonitor.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.andylin.homepackagemonitor.Adapters.BoxHistoryListAdapter;
import com.example.andylin.homepackagemonitor.Model.BoxHistory;
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
 * Created by Andy Lin on 2017-03-15.
 */

public class BoxStatusFragment extends Fragment{
    private static final String TAG = "BoxStatusFragment";

    private RecyclerView recyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Request History");
        getCameraImage();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_box_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.image_history_list);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getCameraImage(){
        SharedPreferences loginSettings = getActivity().getSharedPreferences("PreferenceFile", Context.MODE_PRIVATE);
        String username = loginSettings.getString("username", "");

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", username);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        String url = getResources().getString(R.string.serverip) + "viewlog";

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
        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(jsonArrayRequest);
    }

    public void parseJSONArray(JSONArray jsonArray){
        List<BoxHistory> boxHistoryList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String dateAccessed = jsonObject.getString("datetime");
                Boolean wasGranted = jsonObject.getBoolean("wasgranted");
                String image = jsonObject.getString("imagebytes");
                Boolean wasAcknowledged = jsonObject.getBoolean("hasack");
                int id = jsonObject.getInt("id");
                BoxHistory boxHistory = new BoxHistory(dateAccessed, wasGranted, image.substring(10), wasAcknowledged, id);
                boxHistoryList.add(boxHistory);
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }

        BoxHistoryListAdapter boxHistoryListAdapter = new BoxHistoryListAdapter(boxHistoryList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(boxHistoryListAdapter);
    }
}
