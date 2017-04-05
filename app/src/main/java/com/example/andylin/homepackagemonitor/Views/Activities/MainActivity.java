package com.example.andylin.homepackagemonitor.Views.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.andylin.homepackagemonitor.Views.Fragments.HistoryFragment;
import com.example.andylin.homepackagemonitor.Views.Fragments.MapFragment;
import com.example.andylin.homepackagemonitor.Views.Fragments.PendingFragment;
import com.example.andylin.homepackagemonitor.Views.Fragments.SettingsFragment;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Views.Fragments.StatusFragment;
import com.example.andylin.homepackagemonitor.Volley.CustomJSONArrayRequest;
import com.example.andylin.homepackagemonitor.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "MainActivity";
    public static final String PREFS_FILE_NAME = "PreferenceFile";
    public static final int LOG_IN_REQUEST = 1;

    private PendingFragment mPendingFragment;
    private HistoryFragment mHistoryFragment;
    private StatusFragment mStatusFragment;
    private SettingsFragment mSettingsFragment;
    private MapFragment mMapFragment;

    private NavigationView navigationView;
    private TextView drawerMessage;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Check if the user is already logged in, if they aren't then open the log in activity
        SharedPreferences loginSettings = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        boolean loggedIn = loginSettings.getBoolean("isLoggedIn", false);

        if (!loggedIn){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOG_IN_REQUEST);
        }

        View header = navigationView.getHeaderView(0);
        drawerMessage = (TextView) header.findViewById(R.id.drawer_welcome_message);
        spinner = (Spinner) header.findViewById(R.id.spinner);

        drawerMessage.setText("Welcome " +  loginSettings.getString("username", ""));
        spinner.setOnItemSelectedListener(this);

        requestDevices();

        startPendingFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOG_IN_REQUEST){
            if (resultCode == RESULT_OK){
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("username", data.getStringExtra("USERNAME"));
                editor.putString("password", data.getStringExtra("PASSWORD"));
                editor.commit();

                drawerMessage.setText("Welcome " +  data.getStringExtra("USERNAME"));
                requestDevices();

                navigationView.getMenu().getItem(0).setChecked(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home){
            startPendingFragment();
        } else if (id == R.id.nav_history){
            startHistoryFragment();
        } else if (id == R.id.nav_status){
            startStatusFragment();
        } else if (id  == R.id.nav_map){
            startMapFragment();
        } else if (id == R.id.nav_settings) {
            startSettingsFragment();
        } else if (id == R.id.nav_sign_out) {
            signOut();
        }

        requestDevices();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void requestDevices(){
        SharedPreferences loginSettings = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
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
                populateSpinner(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        // Access the RequestQueue through the singleton class to add the request to the request queue
        VolleySingleton.getInstance(this).getRequestQueue().add(jsonArrayRequest);
    }

    public void populateSpinner(JSONArray jsonArray){
        List<String> spinnerList = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String deviceid = jsonObject.getString("deviceid");
                spinnerList.add(deviceid);
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerList);
        spinner.setAdapter(arrayAdapter);

        if (spinnerList.size() > 0){
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).edit();
            editor.putString("deviceid", spinnerList.get(0));
            editor.commit();
        }
    }

    /*
        Starts the Pending Fragment
     */
    public void startPendingFragment(){
        if(mPendingFragment == null)
            mPendingFragment = new PendingFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, mPendingFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Log.e(TAG, "Switching to the Status Fragment");
    }

    /*
        Starts the History Fragment
     */
    public void startHistoryFragment(){
        if(mHistoryFragment == null)
            mHistoryFragment = new HistoryFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, mHistoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Log.e(TAG, "Switching to the History Fragment");
    }

    /*
       Starts the Status Fragment
    */
    public void startStatusFragment(){
        if(mStatusFragment == null)
            mStatusFragment = new StatusFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, mStatusFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Log.e(TAG, "Switching to the Status Fragment");
    }

    /*
        Starts the Map Fragment
     */
    public void startMapFragment(){
        if(mMapFragment == null)
            mMapFragment = new MapFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, mMapFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Log.e(TAG, "Switching to the Map Fragment");
    }

    /*
        Start Settings Fragment
     */
    public void startSettingsFragment(){
        if(mSettingsFragment == null)
            mSettingsFragment = new SettingsFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, mSettingsFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Log.e(TAG, "Switching to the Settings Fragment");
    }

    /*
        Sign the user out
     */
    public void signOut(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Signing Out...");
        progressDialog.show();

        getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).edit().clear().commit();

        Log.e(TAG, "Signing Out");

        progressDialog.dismiss();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOG_IN_REQUEST);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences loginSettings = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        String currentDeviceID = loginSettings.getString("deviceid", "");
        Log.e(TAG, "Current device id is: " + currentDeviceID);

        if (!currentDeviceID.equals(parent.getItemAtPosition(position).toString())){
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).edit();
            editor.putString("deviceid", parent.getItemAtPosition(position).toString());
            editor.commit();
            Log.e(TAG, "Changed device id to: " + parent.getItemAtPosition(position).toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setNavigationDrawerCheckedItem(int resId){
        navigationView.setCheckedItem(resId);
    }
}
