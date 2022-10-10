package com.mitulagr.office;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

//import com.anychart.AnyChart;
//import com.anychart.AnyChartView;
//import com.anychart.chart.common.dataentry.DataEntry;
//import com.anychart.chart.common.dataentry.ValueDataEntry;
//import com.anychart.charts.Pie;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatsFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    public StatsFragment() {}

    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Button Sdate;
    private DBHandler db;
    private SharedPreferences sp;
    int Join[];
    private String types [] = {"Work","Meeting","Break"};
    //private AnyChartView anyChartView;
    private PieChart pieChart;
    private BarChart barChart;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        db = new DBHandler(getContext());
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        email = sp.getString("Employee","");

        Sdate = (Button) rootView.findViewById(R.id.statsdate);

        Join = new int[]{Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.MONTH)+1,
                Calendar.getInstance().get(Calendar.YEAR)};

        Sdate.setText(String.format("%04d-%02d-%02d",Join[2],Join[1],Join[0]));

        Sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        //anyChartView = rootView.findViewById(R.id.piechart);

        pieChart = rootView.findViewById(R.id.piechart);

        setupPieChart();
        loadPieData();

        barChart = rootView.findViewById(R.id.barchart);

        setupBarChart();
        loadBarData();

        return rootView;
    }

    public void setupPieChart(){
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelTextSize(16);
        pieChart.setEntryLabelColor(Color.parseColor("#F1F7FF"));
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        pieChart.getLegend().setTextSize(16);
        pieChart.getLegend().setEnabled(false);
    }

    public void loadPieData(){
        ArrayList<PieEntry> entries = new ArrayList<>();

        int vals[] = db.getDataToday(email,Sdate.getText().toString());

        boolean prob = false;

        if(vals[0]==0 && vals[1]==0 && vals[2]==0){
            prob = true;
            vals = new int[]{1,1,1};
        }

        for(int i=0;i<3;i++){
            if(vals[i]>0) entries.add(new PieEntry(vals[i],types[i]));
        }

        ArrayList<Integer> colors = new ArrayList<>();

        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        //data.setValueFormatter(new PercentFormatter(pieChart));
        if(!prob) data.setValueFormatter(new DefaultValueFormatter(0));
        else{
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return "0";
                }
            });
        }

        data.setValueTextSize(24f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    public void setupBarChart(){
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        barChart.getLegend().setTextSize(16);
        barChart.getLegend().setEnabled(true);

        barChart.setTouchEnabled(true);
        barChart.setClickable(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);

        barChart.setDrawBorders(false);
        barChart.setDrawGridBackground(false);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawAxisLine(false);

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawLabels(true);
        barChart.getXAxis().setTextColor(Color.parseColor("#AA5522"));
        barChart.getXAxis().setTextSize(14f);
        barChart.setExtraBottomOffset(10f);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawAxisLine(false);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setDrawAxisLine(false);

        barChart.setTouchEnabled(false);
    }

    public void loadBarData(){
        ArrayList<BarEntry> entries = new ArrayList<>();

        String d = Sdate.getText().toString();
        String dt;

        ArrayList<String> labels = new ArrayList<>();

        labels.add("TOD");

        for(int i=0;i<7;i++){
            dt = getPrevDay(d,i);
            if(i>0) labels.add(dt.substring(5));
            int vals[] = db.getDataToday(email,dt);
            float fvals[] = {vals[0],vals[1],vals[2]};
            entries.add(new BarEntry(i,fvals));
        }

        //entries.add(new PieEntry(0.2f,"M"));
        //entries.add(new PieEntry(0.3f,"V"));

        ArrayList<Integer> colors = new ArrayList<>();

        int cli = 0;
        for(int color: ColorTemplate.MATERIAL_COLORS){
            if(cli==3) break;
            colors.add(color);
            cli++;
        }

        BarDataSet dataSet = new BarDataSet(entries,"");
        dataSet.setColors(colors);

        BarData data = new BarData(dataSet);
        data.setDrawValues(true);
        //data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueFormatter(new DefaultValueFormatter(0));
//        data.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                //float vl = super.getFormattedValue(value);
//                return String.valueOf((int) value)+" Min";
//            }
//        });
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);

        barChart.setData(data);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.invalidate();
    }

    public String getPrevDay(String d, int n){
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final Date prev;
        try {
            prev = format.parse(d);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(prev);
            calendar.add(Calendar.DAY_OF_YEAR, -1*n);
            return format.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    private void showDatePicker() {
        int day = Join[0];
        int month = Join[1];
        int year = Join[2];
        month = month-1;
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date = String.format("%04d-%02d-%02d", i,i1+1,i2);
        Sdate.setText(date);
        Join[0] = i2;
        Join[1] = i1+1;
        Join[2] = i;
        loadPieData();
        loadBarData();
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}