package com.example.andylin.homepackagemonitor.Views.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.andylin.homepackagemonitor.Model.BoxLocationMarker;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Views.Activities.MainActivity;
import com.example.andylin.homepackagemonitor.Volley.CustomJSONArrayRequest;
import com.example.andylin.homepackagemonitor.Volley.VolleySingleton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Lin on 2017-03-30.
 */

public class MapFragment extends Fragment {
    private static final String TAG = "MapFragment";
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Map");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_map_fragment, container, false);

        mMapView = (MapView) view.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_LOCATION_REQUEST_CODE);
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                getBoxLocations();
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getBoxLocations();

                } else {
                    onDestroy();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void getBoxLocations(){
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
                parseBoxLocations(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Access the RequestQueue through the singleton class to add the request to the request queue
        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(jsonArrayRequest);
    }

    public void parseBoxLocations(JSONArray response){
        List<BoxLocationMarker> boxLocationList = new ArrayList<>();

        for (int i = 0; i < response.length(); i++){
            try{
                JSONObject jsonObject = response.getJSONObject(i);
                boolean isPending = jsonObject.getBoolean("pendingrequest");
                String deviceid = jsonObject.getString("deviceid");
                String latitude = jsonObject.getString("latitude");
                String longitude = jsonObject.getString("longitude");
                BoxLocationMarker boxLocationMarker = new BoxLocationMarker(deviceid,latitude,longitude,isPending);
                boxLocationList.add(boxLocationMarker);
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        setMarker(boxLocationList);
    }

    public void setMarker(List<BoxLocationMarker> boxLocationMarkers){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (BoxLocationMarker marker: boxLocationMarkers){
            // For dropping a marker at a point on the Map
            LatLng lng = new LatLng(parseLatitude(marker.getLatitude()), parseLongitude(marker.getLongitude()));
            googleMap.addMarker(new MarkerOptions().position(lng).title(marker.getDeviceID()).snippet(Boolean.toString(marker.getIsPending())));
            builder.include(lng);
        }

        LatLngBounds bounds = builder.build();

        int padding = 0;
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        googleMap.moveCamera(cameraPosition);
        googleMap.animateCamera(cameraPosition);
    }

    public double parseLatitude(String latitude){
        String degree = latitude.substring(0, 2);
        String minute = latitude.substring(2);
        try{
            return (double) Integer.parseInt(degree) + Double.parseDouble(minute) / 60.0;
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        return 0.0;
    }

    public double parseLongitude(String longitude){
        String degree = longitude.substring(0, 3);
        String minute = longitude.substring(3);
        try{
            return -1.0 * ((double) Integer.parseInt(degree) + Double.parseDouble(minute) / 60.0);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        return 0.0;
    }
}
