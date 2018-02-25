package com.example.dhruv.coinsense;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
}
