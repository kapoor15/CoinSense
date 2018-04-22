package com.example.dhruv.coinsense;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class CurrencyActivity extends AppCompatActivity {

    DatabaseReference root;
    FirebaseDatabase database;
    FirebaseUser user;
    String CurrentUser;
    FirebaseAuth firebaseAuth;
    String p1;
    String p2;
    String p3;
    String uid;
    String oldprice1;
    String oldprice2;
    String oldprice3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        root = database.getReference();
        user = firebaseAuth.getInstance().getCurrentUser();
        p1 = "0";
        p2 = "0";
        p3 = "0";

        //((TextView) findViewById(R.id.username)).setText(user.getEmail());
        CurrentUser = user.getEmail().toString();

        root.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> cryptos = new ArrayList<String>();
                for (DataSnapshot u: dataSnapshot.getChildren()) {
                    if (u.child("email").getValue(String.class) != null) {
                        if (u.child("email").getValue(String.class).equals(CurrentUser)) {
                            uid = u.getKey();
                            cryptos.add(u.child("crypto1").getValue(String.class));
                            cryptos.add(u.child("crypto2").getValue(String.class));
                            cryptos.add(u.child("crypto3").getValue(String.class));
                            try {
                                oldprice1 = u.child("p1").getValue(String.class);
                                oldprice2 = u.child("p2").getValue(String.class);
                                oldprice3 = u.child("p3").getValue(String.class);
                               p1 = getprice(u.child("crypto1").getValue(String.class));
                               p2 = getprice(u.child("crypto2").getValue(String.class));
                               p3 = getprice(u.child("crypto3").getValue(String.class));
                             } catch(IOException ie) {
                                ie.printStackTrace();
                             }
                        }
                    }
                }

                Spinner cryptoSpinner = (Spinner) findViewById(R.id.choice_spinner);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(CurrencyActivity.this, android.R.layout.simple_spinner_item, cryptos);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cryptoSpinner.setAdapter(areasAdapter);
            } @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       //((TextView) findViewById(R.id.username)).setText(user.getEmail());
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
        return true;
    }



    public void logout(View v)  {

        root.child("users").child(uid).child("p1").setValue(p1);
        root.child("users").child(uid).child("p2").setValue(p2);
        root.child("users").child(uid).child("p3").setValue(p3);
        FirebaseAuth.getInstance().signOut();
        //LoginManager.getInstance().logOut();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
        Toast.makeText(CurrencyActivity.this, "Signout successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CurrencyActivity.this, "Signout failed", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    public String getprice(String currency) throws IOException{
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

    }*/

    public String getprice(String currency) throws IOException{
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

    public void prediction(View v) throws IOException {
        String selection = (String) ((Spinner) findViewById(R.id.choice_spinner)).getSelectedItem();
        String newprice = getprice(selection);
        final int index =  ((Spinner) findViewById(R.id.choice_spinner)).getSelectedItemPosition() + 1;

        String oldprice = "";

        if (index == 1) {
            //final String pricetemp;
            oldprice = oldprice1;
           // System.out.println("!!!!!!!!!!!!!!!!!!!!!!##############" + oldprice);
        } else if (index == 2) {
            oldprice = oldprice2;
           // System.out.println("!!!!!!!!!!!!!!!!!!!!!!##############" + oldprice);

        } else if (index == 3) {
            oldprice = oldprice3;
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!##############" + oldprice);

        }

        int indexold1 = oldprice.indexOf('"');
        int indexold2 = oldprice.lastIndexOf('"');
        oldprice = oldprice.substring(indexold1 + 1, indexold2);

        System.out.println("#########" + newprice);
        int indexnew1 = newprice.indexOf('"');
        System.out.println("#########11111" + indexnew1);
        int indexnew2 = newprice.lastIndexOf('"');
        System.out.println("#########2222" + indexnew2);
        newprice = newprice.substring(indexnew1 + 1, indexnew2);



        double percentage = ((Double.parseDouble(newprice) - Double.parseDouble(oldprice)) / Double.parseDouble(oldprice)) * 100;

        ((TextView) findViewById(R.id.price_text)).setText("Price fluctuation of " + selection + " is " + percentage + "%");
    }

    public void checkPrice(View v) throws IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        String selection = (String) ((Spinner) findViewById(R.id.choice_spinner)).getSelectedItem();

        if (selection.equalsIgnoreCase("Bitcoin cash")) {
            selection = "bitcoin-cash";
        }

        if (selection.equalsIgnoreCase("Ethereum Classic")) {
            selection = "ethereum-classic";
        }

        if (selection.equalsIgnoreCase("Bitcoin gold")) {
            selection = "bitcoin-gold";
        }
        //if (selection.equalsIgnoreCase("Bitcoin")) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://coinmarketcap.com/currencies/" + selection + "/");
        ResponseHandler<String> resHandler = new BasicResponseHandler();
        String page = httpClient.execute(httpGet, resHandler);
        Pattern pattern = Pattern.compile("data-currency-price data-usd=(.*?)>");
        Matcher matcher = pattern.matcher(page);
        String price = "";
        if (matcher.find()) {
            price = matcher.group(1);
        }
        ((TextView) findViewById(R.id.price_text)).setText("Price of " + selection + " in USD is "
                + price);
    }

        //}
        /*else if (selection.equalsIgnoreCase("Ethereum")) {
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
        }*/





    //}


    public void graph(View v) {
        startActivity(new Intent(getApplicationContext(), GraphActivity.class));
    }



    public void AccSettings(View v)
    {
        startActivity(new Intent(getApplicationContext(), AccountSettingsActivity.class));
    }
}

