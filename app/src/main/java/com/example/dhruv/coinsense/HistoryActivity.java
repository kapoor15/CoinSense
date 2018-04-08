package com.example.dhruv.coinsense;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void analyze(View view) throws IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ArrayList<Double> prices = new ArrayList<Double>();
        ArrayList<Double> opens = new ArrayList<Double>();
        ArrayList<Double> closes = new ArrayList<Double>();
        ArrayList<Double> lows = new ArrayList<Double>();
        ArrayList<Double> highs = new ArrayList<Double>();




        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(getLink());
        ResponseHandler<String> resHandler = new BasicResponseHandler();
        String page = httpClient.execute(httpGet, resHandler);
        //for open, high, low, close
        Pattern pattern = Pattern.compile("data-format-fiat data-format-value=(.*?)>");
        Matcher matcher = pattern.matcher(page);
        String open = "";
        String high = "";
        String low = "";
        String close = "";
        for (int i = 0; i < getDays(); i++) {
            matcher.find();
                open = matcher.group(1);
                open = open.substring(1, open.length() - 1);
                matcher.find();
                high = matcher.group(1);
                high = high.substring(1, high.length()- 1);
                matcher.find();
                low = matcher.group(1);
                low = low.substring(1, low.length() - 1);
                matcher.find();
                close = matcher.group(1);
                close = close.substring(1, close.length() - 1);
                opens.add(Double.parseDouble(open));
                highs.add(Double.parseDouble(high));
                lows.add(Double.parseDouble(low));
                closes.add(Double.parseDouble(close));
        }

        double maxPrice = -1;
        double minPrice = 999999999;
        for (int i = 0; i < highs.size(); i++) {
            if (highs.get(i) > maxPrice) {
                maxPrice = highs.get(i);
            }
            if (lows.get(i) < minPrice) {
                minPrice = lows.get(i);
            }
            double price = (opens.get(i) + closes.get(i)) / 2;
            prices.add(price);
        }

        double averagePrice = 0;
        for (int i = 0; i < prices.size(); i++) {
            averagePrice += prices.get(i);
        }
        averagePrice = averagePrice / prices.size();

        String name = ((Spinner) findViewById(R.id.currency_spinner)).getSelectedItem().toString();

        ((TextView)findViewById(R.id.analysis_text)).setText("Average Price for " + name + " over " +
        getDays() + " days is " + averagePrice + "\n" + "Maximum Price for " + name + " over " +
                getDays() + " days is " + maxPrice +
        "\n" + "Minimum Price for " + name + " over " +
                getDays() + " days is " + minPrice);


    }

    public String getLink() {
        String name = ((Spinner) findViewById(R.id.currency_spinner)).getSelectedItem().toString();
        String link = "https://coinmarketcap.com/currencies/" + name +
                "/historical-data/";
        if (name.equalsIgnoreCase("Bitcoin Cash")) {
            link = "https://coinmarketcap.com/currencies/bitcoin-cash/historical-data/";
        }
        else if (name.equalsIgnoreCase("Ethereum Classic")) {
            link = "https://coinmarketcap.com/currencies/ethereum-classic/historical-data/";
        }
        else if (name.equalsIgnoreCase("Bitcoin Gold")) {
            link = "https://coinmarketcap.com/currencies/bitcoin-gold/historical-data/";
        }
        return link;
    }

    public int getDays() {
        String days = ((Spinner) findViewById(R.id.days_spinner)).getSelectedItem().toString();
        return Integer.parseInt(days);
    }
}
