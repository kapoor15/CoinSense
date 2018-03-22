package com.example.dhruv.coinsense;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button reg;
    private EditText user;
    private EditText pass;

    private ProgressDialog pd;

    FirebaseAuth firebaseAuth;
    DatabaseReference usersRef;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference();
        FileRead read = FileRead.getInstance(this);
        read.reader();
        HashMap<String, Integer> vals = read.get();

        for(String s : vals.keySet())
        {
            System.out.println(s + " " + vals.get(s));
        }
        pd = new ProgressDialog(this);
        reg = (Button) findViewById(R.id.signup);
        user = (EditText) findViewById(R.id.signup_email);
        pass = (EditText) findViewById(R.id.signup_password);
        reg.setOnClickListener(this);
    }

    private void registerUser(){
        String email = user.getText().toString().trim();
        String password = pass.getText().toString().trim();

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

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();;
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();;
            return;
        }

        pd.setMessage("Registering...");
        pd.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), CurrencyActivity.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            pd.hide();
                        }
                    }
                });
    }

    public void onClick(View view) {
        if (view == reg) {
            registerUser();
        }
    }
}
