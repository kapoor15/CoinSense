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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
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
        }



    }
    public void logout(View v) {
        FirebaseAuth.getInstance().signOut();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
            Toast.makeText(InfoActivity.this, "Signout successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(InfoActivity.this, "Signout failed", Toast.LENGTH_SHORT).show();
        }
    }
}
