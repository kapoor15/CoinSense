package com.example.dhruv.coinsense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FacebookLogin extends AppCompatActivity {
    private Button login;
    FirebaseAuth firebaseAuth;
    DatabaseReference usersRef;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        login = (Button) findViewById(R.id.register_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference();
    }

    public void fbregister(View v) {
        if (v == login) {
            String email = firebaseAuth.getCurrentUser().getEmail().toString();
            Spinner crypto1 = (Spinner)findViewById(R.id.crypto1);
            String c1 = crypto1.getSelectedItem().toString();
            Spinner crypto2 = (Spinner)findViewById(R.id.crypto2);
            String c2 = crypto2.getSelectedItem().toString();
            Spinner crypto3 = (Spinner)findViewById(R.id.crypto3);
            String c3 = crypto3.getSelectedItem().toString();

            User u1 = new User(email, c1, c2, c3);
            String key = usersRef.child("users").push().getKey();
            Map<String, Object> userValues = u1.toMap();
            Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("/users/" + key, userValues);

            usersRef.updateChildren(userUpdates);

            startActivity(new Intent(getApplicationContext(), CurrencyActivity.class));
        }
    }
}
