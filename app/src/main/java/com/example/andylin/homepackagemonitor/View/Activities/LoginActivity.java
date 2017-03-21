package com.example.andylin.homepackagemonitor.View.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Volley.BooleanRequest;
import com.example.andylin.homepackagemonitor.Volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int SIGN_UP_REQUEST = 1;

    private EditText usernameText;
    private EditText passwordText;
    private Button signInButton;
    private TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = (EditText) findViewById(R.id.username_text);
        passwordText = (EditText) findViewById(R.id.password_text);

        signInButton = (Button) findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signUpText = (TextView) findViewById(R.id.sign_up_link);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, SIGN_UP_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SIGN_UP_REQUEST){
            if (resultCode == RESULT_OK){
                // Return RESULT_OK if sign up was successful
                Intent returnIntent = new Intent();
                returnIntent.putExtra("USERNAME", data.getStringExtra("USERNAME"));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the Main Activity
        moveTaskToBack(true);
    }

    public void signIn() {
        if (!isValidInput()) {
            Toast.makeText(getApplicationContext(), "Please enter a username and password", Toast.LENGTH_SHORT).show();
        } else {

            // Authenticate Username and Password Combination here

            // Create a Progress Dialog to show the user we are authenticating the username/password combination
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            // Create a JSON Object that uses the user inputted username and password
            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("username", usernameText.getText().toString());
                jsonObject.put("password", passwordText.getText().toString());

            }
            catch(JSONException e){
                e.printStackTrace();
            }

            String url = getResources().getString(R.string.serverip) + "login";

            // Make a custom BooleanRequest to make HTTP requests
            BooleanRequest booleanRequest = new BooleanRequest(Request.Method.PUT, url, jsonObject.toString(), new Response.Listener<Boolean>() {
                @Override
                public void onResponse(Boolean response) {
                    // Check if the response was true or false
                    if (response) {

                        // Set RESULT_OK after successful Login.
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("USERNAME", usernameText.getText().toString());
                        returnIntent.putExtra("PASSWORD", passwordText.getText().toString());
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Wrong username and password combination", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });

            // Access the RequestQueue through the singleton class to add the request to the request queue
            VolleySingleton.getInstance(this).getRequestQueue().add(booleanRequest);
        }
    }

    public boolean isValidInput() {
        String userNameInput = usernameText.getText().toString();
        String passwordInput = passwordText.getText().toString();

        if (userNameInput.equals("") || userNameInput == null) {
            return false;
        }

        if (passwordInput.equals("") || passwordInput == null) {
            return false;
        }

        return true;
    }
}
