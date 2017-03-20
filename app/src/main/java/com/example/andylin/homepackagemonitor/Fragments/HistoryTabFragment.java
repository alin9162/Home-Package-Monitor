package com.example.andylin.homepackagemonitor.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.andylin.homepackagemonitor.Adapters.GridImageAdapter;
import com.example.andylin.homepackagemonitor.Adapters.HistoryListAdapter;
import com.example.andylin.homepackagemonitor.Model.BoxHistory;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Volley.CustomJSONArrayRequest;
import com.example.andylin.homepackagemonitor.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Lin on 2017-03-18.
 */

public class HistoryTabFragment extends Fragment implements View.OnClickListener{
    private GridView gridView;
    private ImageButton gridDisplayButton;
    private ImageButton listDisplayButton;
    private TextView viewTypeTextView;
    private RecyclerView recyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCameraImage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_history_tab_fragment, container, false);

        viewTypeTextView = (TextView) view.findViewById(R.id.view_type_text);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        gridDisplayButton = (ImageButton) view.findViewById(R.id.grid_display_button);
        gridDisplayButton.setSelected(true);
        gridDisplayButton.setOnClickListener(this);

        listDisplayButton = (ImageButton) view.findViewById(R.id.list_display_button);
        listDisplayButton.setSelected(false);
        listDisplayButton.setOnClickListener(this);

        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
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

        gridView.setAdapter(new GridImageAdapter(getActivity(), boxHistoryList));

        HistoryListAdapter boxHistoryListAdapter = new HistoryListAdapter(boxHistoryList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(boxHistoryListAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.grid_display_button:
                viewTypeTextView.setText("Grid View");
                gridView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                break;
            case R.id.list_display_button:
                viewTypeTextView.setText("List View");
                gridView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
