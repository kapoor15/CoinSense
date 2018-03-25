package com.example.dhruv.coinsense;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class SentimentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        double bitcoin = getValues(getTweets("Bitcoin"));
        double ethereum = getValues(getTweets("Ethereum"));
        double monero = getValues(getTweets("Monero"));
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(4, 0)
        });

        BarGraphSeries<DataPoint> first = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, bitcoin)
        });

        BarGraphSeries<DataPoint> second = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(2, ethereum)
        });

        BarGraphSeries<DataPoint> third = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(3, monero)
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
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Bitcoin", "Ethereum", "Monero"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


    }


}
