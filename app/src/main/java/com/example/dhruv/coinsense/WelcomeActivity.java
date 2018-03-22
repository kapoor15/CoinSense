package com.example.dhruv.coinsense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.login.LoginManager;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void goRegister(View v) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void goLogin(View v) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
