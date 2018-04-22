package com.example.dhruv.coinsense;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    public void analyze(View view) throws IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ArrayList<Double> prices = new ArrayList<Double>();
        ArrayList<Double> opens = new ArrayList<Double>();
        ArrayList<Double> closes = new ArrayList<Double>();
        ArrayList<Double> lows = new ArrayList<Double>();
        ArrayList<Double> highs = new ArrayList<Double>();
        ArrayList<Double> caps = new ArrayList<Double>();




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

        //for market cap
        pattern = Pattern.compile("data-format-market-cap data-format-value=(.*?)>");
        matcher = pattern.matcher(page);
        String cap = "";

        for (int i = 0; i < getDays(); i++) {
            matcher.find();
            cap = matcher.group(1);
            cap = cap.substring(1, cap.length() - 5);
            caps.add(Double.parseDouble(cap));
        }

        double minCap = 999999999;
        double maxCap = -1;

        for (int i = 0; i < getDays(); i++) {
            if (minCap > caps.get(i)) {
                minCap = caps.get(i);
            }
            if (maxCap < caps.get(i)) {
                maxCap = caps.get(i);
            }
        }

        String name = ((Spinner) findViewById(R.id.currency_spinner)).getSelectedItem().toString();

        ((TextView)findViewById(R.id.analysis_text)).setText("Average Price for " + name + " over " +
        getDays() + " days is " + averagePrice + "\n" + "Maximum Price for " + name + " over " +
                getDays() + " days is " + maxPrice +
        "\n" + "Minimum Price for " + name + " over " +
                getDays() + " days is " + minPrice + "\n" + "Maximum Market Cap for " + name + " over " +
        getDays() + " days is " + maxCap + "k" +
                "\n" + "Minimum Market Cap for " + name + " over " +
                getDays() + " days is " + minCap + "k"
                );


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

<<<<<<< HEAD
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
=======
    public String getVolume(String currency) throws IOException{
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
        Pattern pattern = Pattern.compile("data-currency-volume data-usd=(.*?)>");
        Matcher matcher = pattern.matcher(page);
        String volume = "";
        if (matcher.find()) {
            volume = matcher.group(1);
        }

        return volume;
>>>>>>> 65f0de8e6290e8321dcc5f980a086cf836496c44
    }
}
