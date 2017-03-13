package com.example.andylin.homepackagemonitor.View.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andylin.homepackagemonitor.R;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

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
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the Main Activity
        moveTaskToBack(true);
    }

    public void signIn() {
        if (!isValidInput()) {
            Toast.makeText(getApplicationContext(), "Please enter a username and password", Toast.LENGTH_LONG).show();
        } else {
            // Authenticate Username and Password Combination here

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();

            // TODO: Implement your own authentication logic here.

            new android.os.Handler().postDelayed(new Runnable() {
                public void run() {
                    finish();
                    progressDialog.dismiss();
                }
            }, 3000);
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
