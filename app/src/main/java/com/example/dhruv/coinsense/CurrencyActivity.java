package com.example.dhruv.coinsense;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class CurrencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       //((TextView) findViewById(R.id.username)).setText(user.getEmail());
    }

    public void goTo(View v) {
        startActivity(new Intent(getApplicationContext(), TwitterActivity.class));
    }

    public void aboutus (View v) {
        startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
    }

    public void info (View v) {
        startActivity(new Intent(getApplicationContext(), InfoActivity.class));
    }

    public void sentiment (View v) {
        startActivity(new Intent(getApplicationContext(), SentimentActivity.class));
    }

    public void logout(View v) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
        Toast.makeText(CurrencyActivity.this, "Signout successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CurrencyActivity.this, "Signout failed", Toast.LENGTH_SHORT).show();
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
        }



    }
}
