package com.example.andylin.homepackagemonitor.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Utils.ImageUtils;
import com.example.andylin.homepackagemonitor.Volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andy Lin on 2017-03-17.
 */

public class PendingTabFragment extends Fragment{
    private static final String TAG = "PendingTabFragment";

    private LinearLayout noPendingRequestLayout;
    private CardView cardView;
    private ImageView image;
    private Button acceptButton;
    private Button declineButton;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "Refreshing Pending Tab");
        getPendingRequests();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_pending_tab_fragment, container, false);

        image = (ImageView) view.findViewById(R.id.pending_image);
        noPendingRequestLayout = (LinearLayout) view.findViewById(R.id.no_pending_request_layout);
        cardView = (CardView) view.findViewById(R.id.card_view);

        acceptButton = (Button) view.findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acknowledge(true);
            }
        });

        declineButton = (Button) view.findViewById(R.id.decline_button);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acknowledge(false);
            }
        });
        return view;
    }

    public void getPendingRequests(){
        SharedPreferences loginSettings = getActivity().getSharedPreferences("PreferenceFile", Context.MODE_PRIVATE);
        String username = loginSettings.getString("username", "");
        String password = loginSettings.getString("password", "");

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        String url = getResources().getString(R.string.serverip) + "getstatus";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJSONObject(response);
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

    public void parseJSONObject(JSONObject jsonObject){
        String imageBytes = "";
        boolean isPending = false;
        try{
            imageBytes = jsonObject.getString("imagebytes");
            isPending = jsonObject.getBoolean("pendingrequest");
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        if (isPending){
            cardView.setVisibility(View.VISIBLE);
            noPendingRequestLayout.setVisibility(View.GONE);
            byte[] cameraByteArray = ImageUtils.hexStringToByteArray(imageBytes.substring(10));
            Bitmap bmp = BitmapFactory.decodeByteArray(cameraByteArray, 0, cameraByteArray.length);
            image.setImageBitmap(bmp);
        }
        else {
            noPendingRequestLayout.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
        }
    }

    public void acknowledge(boolean request){
        SharedPreferences loginSettings = getActivity().getSharedPreferences("PreferenceFile", getActivity().MODE_PRIVATE);
        String username = loginSettings.getString("username", "");
        String password = loginSettings.getString("password", "");
        String deviceID = loginSettings.getString("deviceid", "");

        final boolean approveStatus = request;

        String url = getResources().getString(R.string.serverip) + "acknowledge";
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
                cardView.setVisibility(View.GONE);
                noPendingRequestLayout.setVisibility(View.VISIBLE);
                if (approveStatus){
                    Toast.makeText(getActivity(), "Opening box...", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Access Declined", Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(stringRequest);
    }
}
