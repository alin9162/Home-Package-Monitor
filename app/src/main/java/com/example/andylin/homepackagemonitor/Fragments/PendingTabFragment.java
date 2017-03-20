package com.example.andylin.homepackagemonitor.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.andylin.homepackagemonitor.Model.BoxHistory;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Utils.ImageUtils;
import com.example.andylin.homepackagemonitor.Volley.CustomJSONArrayRequest;
import com.example.andylin.homepackagemonitor.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Andy Lin on 2017-03-17.
 */

public class PendingTabFragment extends Fragment{
    private static final String TAG = "PendingTabFragment";

    private ImageView image;
    private TextView pendingRequestLabel;
    private CardView cardView;
    private Button acceptButton;
    private Button declineButton;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPendingRequests();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_pending_tab_fragment, container, false);

        image = (ImageView) view.findViewById(R.id.pending_image);
        pendingRequestLabel = (TextView) view.findViewById(R.id.pending_request_label);
        cardView = (CardView) view.findViewById(R.id.card_view);

        acceptButton = (Button) view.findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences loginSettings = getActivity().getSharedPreferences("PreferenceFile", getActivity().MODE_PRIVATE);
                String username = loginSettings.getString("username", "");
                String password = loginSettings.getString("password", "");
                String deviceID = loginSettings.getString("deviceid", "");

                String url = getResources().getString(R.string.serverip) + "acknowledge";
                final JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                    jsonObject.put("deviceid", deviceID);
                    jsonObject.put("approved", "true");
                }
                catch(JSONException e){
                    e.printStackTrace();
                }

                Log.e(TAG, "Username: " + username + " Password: " + password + " DeviceID: " + deviceID);

                // Make a custom BooleanRequest to make HTTP requests
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        cardView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Opening box...", Toast.LENGTH_SHORT);
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
                VolleySingleton.getInstance(getActivity()).getRequestQueue().add(stringRequest);
            }
        });

        declineButton = (Button) view.findViewById(R.id.decline_button);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences loginSettings = getActivity().getSharedPreferences("PreferenceFile", getActivity().MODE_PRIVATE);
                String username = loginSettings.getString("username", "");
                String password = loginSettings.getString("password", "");
                String deviceID = loginSettings.getString("deviceid", "");

                Log.e(TAG, "Username: " + username + " Password: " + password + " DeviceID: " + deviceID);

                String url = getResources().getString(R.string.serverip) + "acknowledge";
                final JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                    jsonObject.put("deviceid", deviceID);
                    jsonObject.put("approved", "false");
                }
                catch(JSONException e){
                    e.printStackTrace();
                }

                // Make a custom BooleanRequest to make HTTP requests
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        cardView.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Access Declined", Toast.LENGTH_SHORT);
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
                VolleySingleton.getInstance(getActivity()).getRequestQueue().add(stringRequest);
            }
        });
        return view;
    }

    public void getPendingRequests(){
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
        final List<BoxHistory> boxHistoryList = new ArrayList<>();

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

        //gridview.setAdapter(new GridImageAdapter(getActivity(), boxHistoryList));

        Collections.sort(boxHistoryList, new Comparator<BoxHistory>(){
            public int compare(BoxHistory history1, BoxHistory history2){
                if(history1.getId() == history2.getId())
                    return 0;
                return history1.getId() < history2.getId() ? -1 : 1;
            }
        });

        if (boxHistoryList.size() > 0 && !boxHistoryList.get(boxHistoryList.size()-1).getHasAcknowledged()){
            cardView.setVisibility(View.VISIBLE);
            pendingRequestLabel.setVisibility(View.GONE);
            byte[] cameraByteArray = ImageUtils.hexStringToByteArray(boxHistoryList.get(boxHistoryList.size()-1).getImageBytes());
            Bitmap bmp = BitmapFactory.decodeByteArray(cameraByteArray, 0, cameraByteArray.length);
            image.setImageBitmap(bmp);
        }
        else {
            pendingRequestLabel.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
        }
    }
}
