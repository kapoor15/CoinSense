package com.example.dhruv.coinsense;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountSettingsActivity extends AppCompatActivity {

    DatabaseReference root;
    FirebaseDatabase database;
    FirebaseUser user;
    String CurrentUser;
    FirebaseAuth firebaseAuth;
    String crypto1;
    String crypto2;
    String crypto3;
    String uid;
    Resources res;
    EditText newpass;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        root = database.getReference();
        user = firebaseAuth.getInstance().getCurrentUser();
        CurrentUser = user.getEmail().toString();
        res = getResources();
        root.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot u: dataSnapshot.getChildren()) {
                    if (u.child("email").getValue(String.class) != null) {
                        if (u.child("email").getValue(String.class).equals(CurrentUser)) {
                            uid = u.getKey();
                            System.out.println("REACHEDD HEREEEEE ::::::::::: ");
                            crypto1 = (u.child("crypto1").getValue(String.class));
                            crypto2 = (u.child("crypto2").getValue(String.class));
                            crypto3 = (u.child("crypto3").getValue(String.class));
                            System.out.println("HEREEEE:: " + crypto1);
                            System.out.println("HEREE :: " + crypto2);
                            System.out.println("HEREE :: " + crypto3);
                        }
                        String[] arr = res.getStringArray(R.array.CryptocurrencyChoice1);
                        int i = 0;
                        int j = 0;
                        int k = 0;
                        for(int a = 0; a < arr.length; a++)
                        {
                            if(arr[a].equalsIgnoreCase(crypto1))
                            {
                                i = a;
                            }
                            if(arr[a].equalsIgnoreCase(crypto2))
                            {
                                j = a;
                            }
                            if(arr[a].equalsIgnoreCase(crypto3))
                            {
                                k = a;
                            }
                        }
                        ((Spinner) findViewById(R.id.Crypto1)).setSelection(i);
                        ((Spinner) findViewById(R.id.Crypto2)).setSelection(j);
                        ((Spinner) findViewById(R.id.Crypto3)).setSelection(k);
                        ((TextView) findViewById(R.id.Email)).setText(CurrentUser);
                    }
                }
            } @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    void updatec1(View v)
    {
        Spinner spinner1 = (Spinner)(findViewById(R.id.Crypto1));
        crypto1 = spinner1.getSelectedItem().toString();
        root.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot u: dataSnapshot.getChildren())
                {
                     if (u.child("email").getValue(String.class) != null)
                     {
                        if (u.child("email").getValue(String.class).equals(CurrentUser))
                        {
                            uid = u.getKey();
                            root.child("users"+"/" + uid+"/crypto1").setValue(crypto1);
                        }
                     }
                }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
        });

    }

    void updatec2(View v)
    {
        Spinner spinner1 = (Spinner)(findViewById(R.id.Crypto2));
        crypto2 = spinner1.getSelectedItem().toString();
        root.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot u: dataSnapshot.getChildren())
                {
                    if (u.child("email").getValue(String.class) != null)
                    {
                        if (u.child("email").getValue(String.class).equals(CurrentUser))
                        {
                            uid = u.getKey();
                            root.child("users"+"/" + uid+"/crypto2").setValue(crypto2);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    void updatec3(View v)
    {
        Spinner spinner1 = (Spinner)(findViewById(R.id.Crypto3));
        crypto3 = spinner1.getSelectedItem().toString();
        root.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot u: dataSnapshot.getChildren())
                {
                    if (u.child("email").getValue(String.class) != null)
                    {
                        if (u.child("email").getValue(String.class).equals(CurrentUser))
                        {
                            uid = u.getKey();
                            root.child("users"+"/" + uid+"/crypto3").setValue(crypto3);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    void updatePassword(View v) {
        newpass = (EditText) findViewById(R.id.NewPass);
        String n = newpass.getText().toString().trim();

        if ( n == null || n.equals("")) {
            Toast.makeText(AccountSettingsActivity.this, "Invalid Password Input", Toast.LENGTH_SHORT).show();

        }
        else{
            user.updatePassword(n)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AccountSettingsActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.id_getTweets) {
            startActivity(new Intent(getApplicationContext(), TwitterActivity.class));
            return true;
        }

        if (id == R.id.id_Sentiment) {
            startActivity(new Intent(getApplicationContext(), SentimentActivity.class));
            return true;
        }

        if (id == R.id.id_aboutUs) {
            startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
            return true;
        }

        if (id == R.id.id_coinInfo) {
            startActivity(new Intent(getApplicationContext(), InfoActivity.class));
            return true;
        }

        if (id == R.id.id_home) {
            startActivity(new Intent(getApplicationContext(), CurrencyActivity.class));
            return true;
        }

        if (id == R.id.id_history) {
            startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
            return true;
        }

        if (id == R.id.id_graph) {
            startActivity(new Intent(getApplicationContext(), GraphActivity.class));
            return true;
        }

        if (id == R.id.id_account) {
            startActivity(new Intent(getApplicationContext(), AccountSettingsActivity.class));
        }
        return true;
    }
    void toHome(View v)
    {
        startActivity(new Intent(getApplicationContext(), CurrencyActivity.class));
    }


}
