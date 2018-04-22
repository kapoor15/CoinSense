package com.example.dhruv.coinsense;

import android.content.Intent;
import android.os.StrictMode;
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

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoActivity extends AppCompatActivity {

    DatabaseReference root;
    FirebaseDatabase database;
    FirebaseUser user;
    String CurrentUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        root = database.getReference();
        user = firebaseAuth.getInstance().getCurrentUser();

        //((TextView) findViewById(R.id.username)).setText(user.getEmail());
        CurrentUser = user.getEmail().toString();
        root.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> cryptos = new ArrayList<String>();

                for (DataSnapshot u: dataSnapshot.getChildren()) {
                    if (u.child("email").getValue(String.class) != null) {
                        if (u.child("email").getValue(String.class).equals(CurrentUser)) {
                            cryptos.add(u.child("crypto1").getValue(String.class));
                            cryptos.add(u.child("crypto2").getValue(String.class));
                            cryptos.add(u.child("crypto3").getValue(String.class));
                        }
                        // String areaName = u.child("areaName").getValue(String.class);
                        // areas.add(areaName);
                    }
                }
                Spinner cryptoSpinner = (Spinner) findViewById(R.id.choice_spinner);
                Spinner infoSpinner = (Spinner) findViewById(R.id.info_spinner);
                ArrayAdapter<String> cryptosAdapter = new ArrayAdapter<String>(InfoActivity.this, android.R.layout.simple_spinner_item, cryptos);
                cryptosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cryptoSpinner.setAdapter(cryptosAdapter);
                infoSpinner.setAdapter(cryptosAdapter);
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

        if (id == R.id.id_graph) {
            startActivity(new Intent(getApplicationContext(), GraphActivity.class));
            return true;
        }

        if (id == R.id.id_account) {
            startActivity(new Intent(getApplicationContext(), AccountSettingsActivity.class));
        }
        return true;
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
        else if (choice.equalsIgnoreCase("stellar"))
        {
            String answer = "The Stellar network is a distributed blockchain based ledger and " +
                    "database that facilitates cross-asset transfers of value, including payments." +
                    " The native digital asset of Stellar is called Lumens (XLM).The Stellar network is an open source," +
                    " distributed, and community owned network used to facilitate cross-asset transfers of value. " +
                    "It doesn’t handle all software types like Ethereum, but it is one of the only other choices for ICOs " +
                    "aside from Ethereum (but not the only other choice).\n" +
                    "Like Ripple, Stellar can handle exchanges between fiat-based currencies and between cryptocurrencies.\n" +
                    "Lumens have low fees (each transaction has a minor fee—0.00001 lumens—associated with it).\n" +
                    "The Stellar network, and thus Lumens and any other asset using the Lumen network," +
                    " has fast transaction speeds that rival Ripple.";
            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }
        else if (choice.equalsIgnoreCase("litecoin"))
        {
            String answer = "Litecoin is a peer-to-peer Internet currency that enables instant, near-zero cost payments to anyone in the world." +
                    " Litecoin is an open source, global payment network that is fully decentralized. " +
                    "Mathematics secures the network and empowers individuals to control their own finances.\n" +
                    "\n" +
                    "Compared to Bitcoin, Litecoin features faster transaction confirmation times (2.5 minutes) " +
                    "and improved storage efficiency.\n" +
                    "\n" +
                    "With substantial industry support, trade volume, and liquidity, " +
                    "Litecoin is a proven medium of commerce complementary to Bitcoin. Litecoin is " +
                    "the second most popular cryptocurrency.";
            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }
        else if (choice.equalsIgnoreCase("Ripple"))
        {
            String answer = "The digital currency, XRP, acts as a bridge currency to other currencies. It does not discriminate between one fiat/crypto currency and another, and thus, makes it easy for any currency to be exchanged for another." +
                    " Each currency on the ecosystem has its own gateway e.g. CADBluzelle, BTCbitstamp, and USDsnapswap. " +
                    "If David wanted bitcoins as payment for the services rendered to Lawrence, Lawrence does not necessarily have to have bitcoins. He can send the payment to his gateway in Canadian dollars (CAD), " +
                    "and David can receive bitcoins from his gateway. One gateway is not needed to initiate a complete a transaction, multiple gateways can be used, forming a chain of trust rippling across the users.\n" +
                    "\n";
            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }
        else if (choice.equalsIgnoreCase("Bitcoin Cash"))
        {
            String answer = "Bitcoin Cash (BCH) is an upgraded version of the Bitcoin Core software. " +
                    "It was released on August 1st, 2017.Bitcoin Cash was created to bring back the essential qualities of money inherent in the original Bitcoin software. Over the years, these qualities were filtered out of Bitcoin Core and progress was stifled by various people, organizations, and companies involved in Bitcoin protocol development. The result is that Bitcoin Core is currently unusable as money due to increasingly high fees per transactions and transfer times taking hours to days. " +
                    "This is all because of the problems created by Bitcoin Core’s blocks being full.";
            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }

        else if(choice.equalsIgnoreCase("Cardano"))
        {
            String answer = "Cardano is home to the Ada cryptocurrency, which can be used to send and receive digital funds. This digital cash represents the future of money, making possible fast, direct transfers that are guaranteed to be secure through the use of cryptography.\n" +
                    "\n" +
                    "Cardano is more than just a cryptocurrency, however, it is a technological " +
                    "platform that will be capable of running financial applications currently used " +
                    "every day by individuals, organisations and governments all around the world. " +
                    "The platform is being constructed in layers, which gives the system the flexibility " +
                    "to be more easily maintained and allow for upgrades by way of soft forks. " +
                    "After the settlement layer that will run Ada is complete, a separate computing " +
                    "layer will be built to handle smart contracts, the digital legal agreements that " +
                    "will underpin future commerce and business. Cardano will also run decentralised applications, or dapps," +
                    " services not controlled by any single party but instead operate on a blockchain.";
            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }
        else if(choice.equalsIgnoreCase("Neo"))
        {
            String answer = "NEO is a next generation smart economy platform (formerly Antshares) " +
                    "and China's first open source blockchain that was founded in 2014, " +
                    "is often known as the “Ethereum of China. NEO uses a smart economy approach to implement its distributed network concept. Its main objective is to digitize assets using the decentralized network of blockchain technology and digital identity.\n" +
                    "\n" +
                    "Neo’s main aim is to be the distributed network for “smart economy”. As their website states: " +
                    "Digital Assets + Digital Identity + Smart Contract = Smart Economy." +
                    "Neo was developed by Shanghai-based blockchain R&D company “OnChain” and funded by two crowdsales: first crowdsale on October 2015 sold 17.5 million NEO tokens for $550,000 and " +
                    "the second crowdsale sold the remaining 22.5 million NEO tokens for $4.5 million.";

            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }
        else if(choice.equalsIgnoreCase("EOS"))
        {
            String answer = "EOS appears to be a direct competitor to Ethereum. Headed by Dan Larimer the founder of two successful crypto coins STEEM and Bitshares, EOS is promising to be a new blockchain operating system faster and more scalable than Ethereum that will allows users to build decentralized applications more efficiently.\n" +
                    "\n" +
                    "EOS idea show promise but without a solid community foundation regularly using the coin its too early too predict what will happen in the long term. ";

            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }
        else if(choice.equalsIgnoreCase("Dash"))
        {
            String answer = "Dashcoin (DSH) is an automatically mutating anonymous cryptocurrency\n" +
                    "Dashcoin is a Next generation anonymous cryptocurrency and the first automatically " +
                    "mutating cryptocurrency created with CryptoNote technology. What does this really means? O" +
                    "ur network code is always up to date with minimal developer cost and close to 0% errors. " +
                    "You are no longer depending on developers good will." +
                    " Concentrate less on developers and more on your trades and projects.";
            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }
        else if(choice.equalsIgnoreCase("Monero"))
        {
            String answer = "Monero (XMR) is an open-source cryptocurrency " +
                    "created in April 2014 that focuses on privacy and " +
                    "decentralization that runs on Windows, macOS, Linux, " +
                    "Android, iOS, and FreeBSD. Monero uses a public ledger to " +
                    "record transactions while new units are created through a process " +
                    "called mining. Monero aims to improve on existing cryptocurrency design by obscuring sender, " +
                    "recipient and amount of every transaction made as well as making the mining process more egalitarian.";
            ((TextView) findViewById(R.id.info_text)).setText(answer);

        }
        else if(choice.equalsIgnoreCase("Lisk"))
        {
            String answer = "Lisk is an open source blockchain platform powered by LSK tokens that will allow developers to write decentralized applications in the JavaScript programming language. " +
                    "Lisk will give developers the ability to build applications on their " +
                    "own sidechain linked to the Lisk network, with their own custom token.\n" +
                    "\n" +
                    "With the goal of making blockchain development more accessible " +
                    "Lisk was forked from Crypti by Max Kordek and Oliver Beddows in May of 2016. " +
                    "Shortly after being listed Lisk became the second most popular cryptocurrency traded against Bitcoin.";
            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }
        else if(choice.equalsIgnoreCase("Bitcoin Gold"))
        {
            String answer = "Bitcoin Gold hopes to change the paradigm around mining on the Bitcoin blockchain. " +
                    "According to the founders, the Bitcoin blockchain has become too centralized. " +
                    "Large companies with huge banks of mining computers now mine the vast majority of Bitcoin. " +
                    "For the founders of Bitcoin Gold," +
                    " having large companies control the Bitcoin network defeats the " +
                    "purpose of a decentralized ledger and peer-to-peer currencies.\n" +
                    "\n" +
                    "In response, they’ve initialized the Bitcoin Gold project. " +
                    "It’s an alternate fork of the Bitcoin blockchain that implements changes that make mining more equitable. " +
                    "The goal for Bitcoin Gold is to create a network where anyone can become a miner with only basic hardware. " +
                    "As a result, Bitcoin Gold mining would be spread among many miners, instead of a few large companies.";
            ((TextView) findViewById(R.id.info_text)).setText(answer);
        }

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

    public void logout(View v) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
            Toast.makeText(InfoActivity.this, "Signout successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(InfoActivity.this, "Signout failed", Toast.LENGTH_SHORT).show();
        }
    }
}
