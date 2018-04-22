package com.example.dhruv.coinsense;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class SentimentActivity extends AppCompatActivity {

    DatabaseReference root;
    FirebaseDatabase database;
    FirebaseUser user;
    String CurrentUser;
    FirebaseAuth firebaseAuth;

    double crypto1;
    double crypto2;
    double crypto3;
    String c1;
    String c2;
    String c3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        root = database.getReference();
        user = firebaseAuth.getInstance().getCurrentUser();

        CurrentUser = user.getEmail().toString();
        root.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array

                System.out.println("WE ARE HEREEEEEEEEEEEEEE");
                for (DataSnapshot u: dataSnapshot.getChildren()) {
                    if (u.child("email").getValue(String.class) != null) {
                        if (u.child("email").getValue(String.class).equals(CurrentUser)) {

                            System.out.println("WE ARE NOWWWWW  HEREEEEEEEEEEEEEE");
                            c1 = (u.child("crypto1").getValue(String.class));
                            c2 = (u.child("crypto2").getValue(String.class));
                            c3 = (u.child("crypto3").getValue(String.class));
                        }

                    }
                    // String areaName = u.child("areaName").getValue(String.class);
                    // areas.add(areaName);
                }

                crypto1 = getValues(getTweets(c1));

                crypto2 = getValues(getTweets(c2));
                crypto3 = getValues(getTweets(c3));
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
        if (id == R.id.id_history) {
            startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
            return true;
        }
        return true;
    }

    public ArrayList<String> getTweets(String coin) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("fEjXjyi83N2Pn0u1Mp3oMyTuT");
        cb.setOAuthConsumerSecret("SHvHjBuP25vdug4KypxZv30iaAdOwAfO2qmXrykIis9N56CoIr");
        cb.setOAuthAccessToken("1029950803-iGthn8jCr1glfo1ZVfslAqeu5aab2q4NP2NCh3a");
        cb.setOAuthAccessTokenSecret("yxZDOKFcQYV1AgL4nn4MF49KK1e5vwz0EYaP88BlQUpbU");

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

        Query query = new Query(coin);
        query.setCount(30);
        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        List<Status> tweets = result.getTweets();

        ArrayList<String> obtainedTweets = new ArrayList<String>();

        for (int i = 0; i < tweets.size(); i++) {
            obtainedTweets.add(tweets.get(i).getText());
        }

        return obtainedTweets;

    }

    public double getValues(ArrayList<String> tweets) {
        FileRead read = FileRead.getInstance(this);
        read.reader();
        HashMap<String, Integer> emotionMap = read.get();

        double total = 0;

        for (int i = 0; i < tweets.size(); i++) {
            String tweet = tweets.get(i);
            String[] words = tweet.split(" ");
            for (int j = 0; j < words.length; j++) {
                String checker = words[j].toLowerCase();
                if (emotionMap.containsKey(checker)) {
                    total += emotionMap.get(checker);
                }
            }
        }

        double answer = total/tweets.size();
        return answer;
    }

    public void makeGraph(View v) {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();

        //get data points

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(4, 0)
        });

        BarGraphSeries<DataPoint> first = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1,crypto1)
        });

        BarGraphSeries<DataPoint> second = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(2, crypto2)
        });

        BarGraphSeries<DataPoint> third = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(3, crypto3)
        });

        //set coloring
        series.setColor(Color.DKGRAY);
        first.setColor(Color.YELLOW);
        second.setColor(Color.BLUE);
        third.setColor(Color.LTGRAY);

        //setStyle
        first.setDrawValuesOnTop(true);
        second.setDrawValuesOnTop(true);
        third.setDrawValuesOnTop(true);
        first.setValuesOnTopColor(Color.BLACK);
        second.setValuesOnTopColor(Color.BLACK);
        third.setValuesOnTopColor(Color.BLACK);
        first.setValuesOnTopSize(50);
        second.setValuesOnTopSize(50);
        third.setValuesOnTopSize(50);
        series.setSpacing(30);
        first.setSpacing(30);
        second.setSpacing(30);
        third.setSpacing(30);


        //add the series
        graph.addSeries(series);
        graph.addSeries(first);
        graph.addSeries(second);
        graph.addSeries(third);

        //get legend
        //first.setTitle("Bitcoin");
        //second.setTitle("Ethereum");
        //third.setTitle("Monero");
        //graph.getLegendRenderer().setVisible(true);
        //graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {c1, c2, c3});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


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


}
