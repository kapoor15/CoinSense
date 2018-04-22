package com.example.dhruv.coinsense;

import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
    }

    public void analyze(View v) throws IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        String startDate = "";
        String endDate = "";


        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(getLink());
        ResponseHandler<String> resHandler = new BasicResponseHandler();
        String page = httpClient.execute(httpGet, resHandler);

        //get dates
        Pattern pattern = Pattern.compile("<td class=\"text-left\">(.*?)</td>");
        Matcher matcher = pattern.matcher(page);

        for (int i = 0; i < getDays(); i++) {
            matcher.find();
            if (i == 0) {
                endDate = matcher.group(1);
            }
            if (i == getDays() - 1) {
                startDate = matcher.group(1);
            }
        }

        ArrayList<Double> prices = new ArrayList<Double>();

        //get open prices
        Pattern p2 = Pattern.compile("<td data-format-fiat data-format-value=(.*?)>");
        Matcher m2 = p2.matcher(page);
        for (int i = 0; i < getDays(); i++) {
            m2.find();
            String price = m2.group(1);
            price = price.substring(1, price.length() - 1);
            prices.add(Double.parseDouble(price));
            m2.find();
            m2.find();
            m2.find();
        }

        //make graph
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();

        DataPoint[] dataPoints = new DataPoint[getDays()];
        for (int i = 0; i < getDays(); i++) {
            dataPoints[i] = new DataPoint(i, prices.get(i));
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
        series.setDrawDataPoints(true);

        series.setDataPointsRadius(12);
        series.setThickness(8);
        series.setColor(Color.BLACK);
        String name = ((Spinner) findViewById(R.id.currency_spinner)).getSelectedItem().toString();
        series.setTitle(name + " Price in $");

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {startDate, endDate});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.addSeries(series);
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

        if (id == R.id.id_account) {
            startActivity(new Intent(getApplicationContext(), AccountSettingsActivity.class));
        }
        return true;
    }
}
