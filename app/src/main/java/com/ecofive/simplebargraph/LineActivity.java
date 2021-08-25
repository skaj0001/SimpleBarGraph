package com.ecofive.simplebargraph;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.InputStreamReader;
import java.util.ArrayList;

public class LineActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_activity);
        String councilName = getIntent().getStringExtra("councilName");
        System.out.println(councilName);
        LineChart mChart = findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        mChart.getAxisRight().setEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();

        float max = 0;

        try {
            InputStreamReader is = new InputStreamReader(getAssets()
                    .open("Contamination_rate_all_years.csv"));

            CSVReader csvReader = new CSVReaderBuilder(is).withSkipLines(1).build();
            String[] nextRecord;

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                int i = 0;
                if (nextRecord[i+1].equals(councilName)) {
                    if (max < Float.parseFloat(nextRecord[i + 2])) {
                        max = Float.parseFloat(nextRecord[i + 2]);
                    }
                    Entry d = new Entry(Integer.parseInt(nextRecord[i])
                            , Float.parseFloat(nextRecord[i + 2]));
                    yValues.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }

        LineDataSet set1 = new LineDataSet(yValues, "Dataset 1");

        int selectedColor = Color.rgb(0, 139, 139);

        set1.setFillAlpha(110);
        set1.setColor(selectedColor);
        set1.setLineWidth(2f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        mChart.setData(data);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(max + 1f);
        leftAxis.enableAxisLineDashedLine(10f, 10f, 0f);
        leftAxis.setDrawLimitLinesBehindData(true);
    }
}
