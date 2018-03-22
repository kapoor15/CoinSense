package com.example.dhruv.coinsense;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class SentimentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void makeGraph(View v) {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        //get data points
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(4, 0)
        });

        BarGraphSeries<DataPoint> first = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 0.8)
        });

        BarGraphSeries<DataPoint> second = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(2, 0.5)
        });

        BarGraphSeries<DataPoint> third = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(3, -0.5)
        });

        //set coloring
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
        first.setTitle("Bitcoin");
        second.setTitle("Ethereum");
        third.setTitle("Monero");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

    }


}
