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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.andylin.homepackagemonitor.R;
import com.example.andylin.homepackagemonitor.Volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private EditText usernameText;
    private EditText passwordText;
    private EditText phoneNumberText;
    private EditText deviceIDText;

    private Button signUpButton;

    private TextView signInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameText = (EditText) findViewById(R.id.username_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        phoneNumberText = (EditText) findViewById(R.id.phone_number_text);
        deviceIDText = (EditText) findViewById(R.id.device_id_text);

        signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        signInText = (TextView) findViewById(R.id.sign_in_link);
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        // Disable going back to the Main Activity
//        moveTaskToBack(true);
//    }

    public void signUp() {
        if (!isValidInput()) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {

            // Create a Progress Dialog to show the user we are creating their account
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            // Create a JSON Object that uses the user inputted data
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", usernameText.getText().toString());
                jsonObject.put("password", passwordText.getText().toString());
                jsonObject.put("phonenum", phoneNumberText.getText().toString());
                jsonObject.put("deviceid", deviceIDText.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = jsonObject.toString();

            String url = getResources().getString(R.string.serverip) + "signup";

            // Make a custom BooleanRequest to make HTTP requests
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "Response: " + response);
                    progressDialog.dismiss();

                    // Tell the LoginActivity that sign up was successful
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("USERNAME", usernameText.getText().toString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error creating account", Toast.LENGTH_SHORT).show();
                }
            })  {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            // Access the RequestQueue through the singleton class to add the request to the request queue
            VolleySingleton.getInstance(this).getRequestQueue().add(stringRequest);
        }
    }

    public boolean isValidInput() {
        String userNameInput = usernameText.getText().toString();
        String passwordInput = passwordText.getText().toString();
        String phoneNumberInput = phoneNumberText.getText().toString();
        String deviceIDInput = deviceIDText.getText().toString();

        if (userNameInput.equals("") || userNameInput == null) {
            return false;
        }

        if (passwordInput.equals("") || passwordInput == null) {
            return false;
        }

        if (phoneNumberInput.equals("") || phoneNumberInput == null) {
            return false;
        }

        if (deviceIDInput.equals("") || deviceIDInput == null) {
            return false;
        }

        return true;
    }
}
