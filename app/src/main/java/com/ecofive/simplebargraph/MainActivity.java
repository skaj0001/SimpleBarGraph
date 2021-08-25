package com.ecofive.simplebargraph;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener
{

    public String[] mValues;
    HorizontalBarChart mChart;

    @Override
    public void onNothingSelected() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChart = findViewById(R.id.barchart);

        mChart.setOnChartValueSelectedListener(this);

        ArrayList<BarEntry> yValue = new ArrayList<>();
        float barWidth = 0.5f;
        float spaceBar = 1f;

        String[] values = new String[4];

        try {
            InputStreamReader is = new InputStreamReader(getAssets()
                    .open("Contamination_rate_latest_year.csv"));

            CSVReader csvReader = new CSVReaderBuilder(is).withSkipLines(1).build();
            String[] nextRecord;

            int barCount = 0;

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                if (barCount<3) {
                    int nextNum = 0;
                    BarEntry d = new BarEntry(barCount * spaceBar
                            , Float.parseFloat(nextRecord[nextNum + 2]));
                    yValue.add(d);
                    values[barCount]= nextRecord[nextNum + 1];
                    }
                else{
                    barCount=3;
                    int cNum = 0;
                    if(nextRecord[cNum + 1].equals("Yarra")){
                        BarEntry d = new BarEntry(barCount * spaceBar
                                , Float.parseFloat(nextRecord[cNum + 2]));
                        yValue.add(d);
                        values[barCount]= nextRecord[cNum + 1];
                    }
                }
                barCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }

        BarDataSet set1;

        set1 = new BarDataSet(yValue, "Data Set1");

        int selectedColor = Color.rgb(0, 139, 139);
        set1.setColor(selectedColor);

        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLACK);

        BarData data = new BarData(set1);
        data.setBarWidth(barWidth);

        mChart.setData(data);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(32f);

        mChart.getAxisRight().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter(values));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {
        float x = entry.getX(); //get the x value
        Intent nextActivity;
        nextActivity = new Intent(MainActivity.this, LineActivity.class);
        nextActivity.putExtra("councilName", mValues[(int) x]);
        startActivity(nextActivity);
    }

    public class XAxisValueFormatter implements IAxisValueFormatter
    {
        public XAxisValueFormatter(String[] values)
        {
            mValues = values;
        }

        @Override
        public String getFormattedValue(float v, AxisBase axisBase) {
            try{
                return mValues[(int)v];
            }
            catch (Exception e){
                return "";
            }
        }
    }
}

