package com.example.dhruv.coinsense;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AboutUsActivity extends AppCompatActivity {

    DatabaseReference root;
    FirebaseDatabase database;
    FirebaseUser user;
    String CurrentUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        root = database.getReference();
        user = firebaseAuth.getInstance().getCurrentUser();

        //((TextView) findViewById(R.id.username)).setText(user.getEmail());
        CurrentUser = user.getEmail().toString();
        root.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> cryptos = new ArrayList<String>();

                for (DataSnapshot u: dataSnapshot.getChildren()) {
                    if (u.child("email").getValue(String.class) != null) {
                        if (u.child("email").getValue(String.class).equals(CurrentUser)) {
                            cryptos.add(u.child("crypto1").getValue(String.class));
                            cryptos.add(u.child("crypto2").getValue(String.class));
                            cryptos.add(u.child("crypto3").getValue(String.class));
                        }

                    }
                    // String areaName = u.child("areaName").getValue(String.class);
                    // areas.add(areaName);
                }

                Spinner cryptoSpinner = (Spinner) findViewById(R.id.choice_spinner);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AboutUsActivity.this, android.R.layout.simple_spinner_item, cryptos);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cryptoSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        return true;
    }

    public void checkPrice(View v) throws IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        String selection = (String) ((Spinner) findViewById(R.id.choice_spinner)).getSelectedItem();


        if (selection.equalsIgnoreCase("Bitcoin")) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://coinmarketcap.com/currencies/bitcoin/");
            ResponseHandler<String> resHandler = new BasicResponseHandler();
            String page = httpClient.execute(httpGet, resHandler);
            Pattern pattern = Pattern.compile("data-currency-price data-usd=(.*?)>");
            Matcher matcher = pattern.matcher(page);
            String price = "";
            if (matcher.find()) {
                price = matcher.group(1);
            }
            ((TextView) findViewById(R.id.price_text)).setText("Price of Bitcoin in USD is "
                    + price);

        } else if (selection.equalsIgnoreCase("Ethereum")) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://coinmarketcap.com/currencies/ethereum/");
            ResponseHandler<String> resHandler = new BasicResponseHandler();
            String page = httpClient.execute(httpGet, resHandler);
            Pattern pattern = Pattern.compile("data-currency-price data-usd=(.*?)>");
            Matcher matcher = pattern.matcher(page);
            String price = "";
            if (matcher.find()) {
                price = matcher.group(1);
            }
            ((TextView) findViewById(R.id.price_text)).setText("Price of Ethereum in USD is "
                    + price);
        } else if (selection.equalsIgnoreCase("Verge")) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://coinmarketcap.com/currencies/verge/");
            ResponseHandler<String> resHandler = new BasicResponseHandler();
            String page = httpClient.execute(httpGet, resHandler);
            Pattern pattern = Pattern.compile("data-currency-price data-usd=(.*?)>");
            Matcher matcher = pattern.matcher(page);
            String price = "";
            if (matcher.find()) {
                price = matcher.group(1);
            }
            ((TextView) findViewById(R.id.price_text)).setText("Price of Verge in USD is "
                    + price);
        } else if (selection.equalsIgnoreCase("Stellar")) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://coinmarketcap.com/currencies/stellar/");
            ResponseHandler<String> resHandler = new BasicResponseHandler();
            String page = httpClient.execute(httpGet, resHandler);
            Pattern pattern = Pattern.compile("data-currency-price data-usd=(.*?)>");
            Matcher matcher = pattern.matcher(page);
            String price = "";
            if (matcher.find()) {
                price = matcher.group(1);
            }
            ((TextView) findViewById(R.id.price_text)).setText("Price of Stellar in USD is "
                    + price);
        } else if (selection.equalsIgnoreCase("Litecoin")) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://coinmarketcap.com/currencies/litecoin/");
            ResponseHandler<String> resHandler = new BasicResponseHandler();
            String page = httpClient.execute(httpGet, resHandler);
            Pattern pattern = Pattern.compile("data-currency-price data-usd=(.*?)>");
            Matcher matcher = pattern.matcher(page);
            String price = "";
            if (matcher.find()) {
                price = matcher.group(1);
            }
            ((TextView) findViewById(R.id.price_text)).setText("Price of Litecoin in USD is "
                    + price);
        }


    }
    public void logout(View v) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
            Toast.makeText(AboutUsActivity.this, "Signout successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AboutUsActivity.this, "Signout failed", Toast.LENGTH_SHORT).show();
        }
    }

    public String getPrice(String currency) throws IOException{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        String adder = currency;

        if (currency.equalsIgnoreCase("Bitcoin cash")) {
            adder = "bitcoin-cash";
        }

        if (currency.equalsIgnoreCase("Ethereum Classic")) {
            adder = "ethereum-classic";
        }

        if (currency.equalsIgnoreCase("Bitcoin gold")) {
            adder = "bitcoin-gold";
        }

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://coinmarketcap.com/currencies/" + adder + "/");
        ResponseHandler<String> resHandler = new BasicResponseHandler();
        String page = httpClient.execute(httpGet, resHandler);
        Pattern pattern = Pattern.compile("data-currency-price data-usd=(.*?)>");
        Matcher matcher = pattern.matcher(page);
        String price = "";
        if (matcher.find()) {
            price = matcher.group(1);
        }

        return price;

    }
}
