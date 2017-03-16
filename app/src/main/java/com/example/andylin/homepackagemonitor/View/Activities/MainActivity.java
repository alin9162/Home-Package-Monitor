package com.example.andylin.homepackagemonitor.View.Activities;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.andylin.homepackagemonitor.Fragments.BoxStatusFragment;
import com.example.andylin.homepackagemonitor.Fragments.HomeFragment;
import com.example.andylin.homepackagemonitor.Fragments.SettingsFragment;
import com.example.andylin.homepackagemonitor.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    public static final String PREFS_FILE_NAME = "PreferenceFile";
    public static final int LOG_IN_REQUEST = 1;

    private HomeFragment homeFragment;
    private SettingsFragment settingsFragment;
    private BoxStatusFragment boxStatusFragment;

    NavigationView navigationView;
    TextView drawerMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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

        if (savedInstanceState == null){
            homeFragment = new HomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, homeFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        View header = navigationView.getHeaderView(0);
        drawerMessage = (TextView) header.findViewById(R.id.drawer_welcome_message);
        drawerMessage.setText("Welcome " +  loginSettings.getString("username", ""));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOG_IN_REQUEST){
            if (resultCode == RESULT_OK){
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("username", data.getStringExtra("USERNAME"));
                editor.putString("password", data.getStringExtra("PASSWORD"));
                editor.putString("deviceid", data.getStringExtra("DEVICEID"));
                editor.commit();

                drawerMessage.setText("Welcome " +  data.getStringExtra("USERNAME"));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if(homeFragment == null)
                homeFragment = new HomeFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, homeFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }  else if (id == R.id.nav_history) {
            if(boxStatusFragment == null)
                boxStatusFragment = new BoxStatusFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, boxStatusFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.nav_settings) {
            if(settingsFragment == null)
                settingsFragment = new SettingsFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, settingsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.nav_sign_out) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Signing Out...");
            progressDialog.show();

            getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE).edit().clear().commit();

            progressDialog.dismiss();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOG_IN_REQUEST);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
