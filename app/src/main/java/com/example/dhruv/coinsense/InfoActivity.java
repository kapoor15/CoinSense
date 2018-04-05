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

public class InfoActivity extends AppCompatActivity {

    DatabaseReference root;
    FirebaseDatabase database;
    FirebaseUser user;
    String CurrentUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
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
                    if (u.child("email").getValue(String.class).equals(CurrentUser)) {
                        cryptos.add(u.child("crypto1").getValue(String.class));
                        cryptos.add(u.child("crypto2").getValue(String.class));
                        cryptos.add(u.child("crypto3").getValue(String.class));
                    }
                    // String areaName = u.child("areaName").getValue(String.class);
                    // areas.add(areaName);
                }

                Spinner cryptoSpinner = (Spinner) findViewById(R.id.choice_spinner);
                Spinner infoSpinner = (Spinner) findViewById(R.id.info_spinner);
                ArrayAdapter<String> cryptosAdapter = new ArrayAdapter<String>(InfoActivity.this, android.R.layout.simple_spinner_item, cryptos);
                cryptosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cryptoSpinner.setAdapter(cryptosAdapter);
                infoSpinner.setAdapter(cryptosAdapter);
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

    public void getInfo(View v) {
        String choice = ( (Spinner) findViewById(R.id.info_spinner)).getSelectedItem().toString();

        if (choice.equalsIgnoreCase("verge")) {
            String answer = "Verge is a scrypt based alternative crypto currency trying to take" + "\n" +
                    " the popularity of both Dogecoin and Bitcoin and combine it with the anonymous" + "\n" +
                    " features of DASH. The block time is 30 seconds and the coin operates through Proof of Work.\n" +
                    "\n" +
                    "VERGE prides itself on being a symbol of progression in the cryptocurrency world. " + "\n" +
                    "It is a more secure, private, and evolving cryptocurrency that is backed by bitcoin, " +
                    "a ton of developer resources and privacy tools";

            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }
        else if (choice.equalsIgnoreCase("ethereum")) {
            String answer = "Ethereum is a decentralized platform that runs smart contracts: " + "\n" +
                    "applications that run exactly as programmed without any possibility of downtime, " +"\n" +
                    "censorship, fraud or third party interference. In the Ethereum protocol and " +"\n" +
                    "blockchain there is a price for each operation. The general idea is, in order " +"\n" +
                    "to have things transferred or executed by the network, you have to consume or " +"\n" +
                    "burn Gas. The cryptocurrency is called Ether and is used to pay for computation " +"\n" +
                    "time and for transaction fees.";

            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }
        else if (choice.equalsIgnoreCase("bitcoin")) {
            String answer = "Bitcoin is a new currency that was created in 2009 by an unknown " +"\n" +
                    "person using the alias Satoshi Nakamoto. Transactions are made with no middle" +"\n" +
                    " men – meaning, no banks! Bitcoin can be used to book hotels on Expedia, " +"\n" +
                    "shop for furniture on Overstock and buy Xbox games. But much of the hype " +"\n" +
                    "is about getting rich by trading it. The price of bitcoin skyrocketed into " +"\n" +
                    "the thousands in 2017.";

            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }
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

        }
        else if (selection.equalsIgnoreCase("Ethereum")) {
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
        }
        else if (selection.equalsIgnoreCase("Verge")) {
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
            Toast.makeText(InfoActivity.this, "Signout successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(InfoActivity.this, "Signout failed", Toast.LENGTH_SHORT).show();
        }
    }
}
