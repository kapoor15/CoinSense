package com.example.dhruv.coinsense;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Twitter;
import twitter4j.*;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
    }

    public void getTweets(View v) throws TwitterException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("fEjXjyi83N2Pn0u1Mp3oMyTuT");
        cb.setOAuthConsumerSecret("SHvHjBuP25vdug4KypxZv30iaAdOwAfO2qmXrykIis9N56CoIr");
        cb.setOAuthAccessToken("1029950803-iGthn8jCr1glfo1ZVfslAqeu5aab2q4NP2NCh3a");
        cb.setOAuthAccessTokenSecret("yxZDOKFcQYV1AgL4nn4MF49KK1e5vwz0EYaP88BlQUpbU");

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

        String choice = ((Spinner) findViewById(R.id.tweet_spinner)).getSelectedItem().toString();

        Query query = new Query(choice);
        QueryResult result;
        result = twitter.search(query);
        List<Status> tweets = result.getTweets();

        String results = "";

        for (int i = 0; i < 5; i++) {
            results += tweets.get(i).getText() + "\n" + "\n";
        }

        ((TextView)findViewById(R.id.tweets_text)).setText(results);
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

    public void logout(View v) {
        FirebaseAuth.getInstance().signOut();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
            Toast.makeText(TwitterActivity.this, "Signout successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(TwitterActivity.this, "Signout failed", Toast.LENGTH_SHORT).show();
        }
    }
}
