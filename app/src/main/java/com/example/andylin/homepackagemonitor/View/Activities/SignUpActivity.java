package com.example.andylin.homepackagemonitor.View.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.andylin.homepackagemonitor.R;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private TextView signInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signInText = (TextView) findViewById(R.id.sign_in_link);
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class );
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
}
