package com.example.andylin.homepackagemonitor.Views.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Andy Lin on 2017-04-03.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder>{
    private List<String> deviceList;
    private Context context;

    public DeviceListAdapter(List<String> deviceList, Context context){
        this.deviceList = deviceList;
        this.context = context;
    }

    @Override
    public DeviceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list_item, parent, false);
        DeviceListAdapter.ViewHolder vh = new DeviceListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DeviceListAdapter.ViewHolder holder, final int position) {
        final String deviceName = deviceList.get(position);
        holder.deviceIDTextView.setText(deviceName);
        holder.removeDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences loginSettings = context.getSharedPreferences("PreferenceFile", Context.MODE_PRIVATE);
                String username = loginSettings.getString("username", "");

                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("username", username);
                    jsonObject.put("deviceid", deviceName);
                }
                catch(JSONException e){
                    e.printStackTrace();
                }

                String url = context.getResources().getString(R.string.serverip) + "removedevice";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseRemoveDevice(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                // Access the RequestQueue through the singleton class to add the request to the request queue
                VolleySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
            }

            public void parseRemoveDevice(JSONObject response){
                try {
                    Toast.makeText(context, response.getString("result"), Toast.LENGTH_SHORT).show();
                } catch(JSONException e) {
                    Log.e("REMOVE DEVICE", e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (deviceList != null){
            return deviceList.size();
        }
        else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deviceIDTextView;
        public Button removeDeviceButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.removeDeviceButton = (Button) itemView.findViewById(R.id.remove_device_button);
            this.deviceIDTextView = (TextView) itemView.findViewById(R.id.device_id_text);
        }
    }
}
