package com.mitulagr.office;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Adapter_Emp extends RecyclerView.Adapter<Adapter_Emp.ViewHolder> {

    List<Employee> disp;
    List<Employee> local;
    DBHandler db;
    String qry;
    String Edate;

    private Adapter_Emp.onRecyclerViewItemClickListener mItemClickListener;

    public void setOnItemClickListener(Adapter_Emp.onRecyclerViewItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface onRecyclerViewItemClickListener {
        void onItemClickListener(View view, int position);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView AName, ADep, AStatus;
        private final PieChart APie;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            AName = (TextView) view.findViewById(R.id.textView22);
            ADep = (TextView) view.findViewById(R.id.textView23);
            AStatus = (TextView) view.findViewById(R.id.textView24);
            APie = (PieChart) view.findViewById(R.id.piechart);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener!=null) {
                mItemClickListener.onItemClickListener(view, getAdapterPosition());
            }
        }
    }

    public Adapter_Emp(Context context, String date) {
        Edate = date;
        db = new DBHandler(context);
        local = db.getAllEmployees();
        disp = local;
        qry = "";
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.design_emp, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.AName.setText(disp.get(position).name);
        viewHolder.ADep.setText(disp.get(position).dep);
        if(disp.get(position).active==0){
            viewHolder.AStatus.setText(" INACTIVE");
            viewHolder.AStatus.setTextColor(Color.parseColor("#C60F1E"));
            viewHolder.AStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_circle_24_r,0,0,0);
        }
        else{
            viewHolder.AStatus.setText(" ACTIVE");
            viewHolder.AStatus.setTextColor(Color.parseColor("#0FC613"));
            viewHolder.AStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_circle_24_g,0,0,0);
        }

        setupPieChart(viewHolder.APie);
        loadPieData(viewHolder.APie,disp.get(position).email);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return disp.size();
    }

    public void filter(){
        if(qry.length()==0){
            disp = local;
            notifyDataSetChanged();
            return;
        }
        disp = new ArrayList<Employee>();
        String text = qry.toUpperCase();
        for(int i=0; i<local.size(); i++){
            if(local.get(i).name.toUpperCase().contains(text) || local.get(i).dep.toUpperCase().contains(text)){
                disp.add(local.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void setupPieChart(PieChart pieChart){
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelTextSize(10);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setTouchEnabled(false);
    }

    public void loadPieData(PieChart pieChart, String email){
        ArrayList<PieEntry> entries = new ArrayList<>();

        int vals[] = db.getDataToday(email,Edate);

        boolean prob = false;

        if(vals[0]==0 && vals[1]==0 && vals[2]==0){
            prob = true;
            vals = new int[]{1,1,1};
        }

        String types[] = {"Work","Meeting","Break"};

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

        data.setValueTextSize(20f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }

}