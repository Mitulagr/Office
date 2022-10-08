//package com.mitulagr.office;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class Adapter_Emp extends RecyclerView.Adapter<Adapter_Emp.ViewHolder> {
//
//    List<Employee> disp;
//    List<Employee> local;
//    DBHandler db;
//    String qry = "";
//
//    /**
//     * Provide a reference to the type of views that you are using
//     * (custom ViewHolder).
//     */
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        private final TextView txtCode, txtSymbol, txtCountry;
//
//        public ViewHolder(View view) {
//            super(view);
//            // Define click listener for the ViewHolder's View
//            txtCode = (TextView) view.findViewById(R.id.textCurCode);
//            txtSymbol = (TextView) view.findViewById(R.id.textCurSymbol);
//            txtCountry = (TextView) view.findViewById(R.id.textCurCountry);
//        }
//
//    }
//
//    public Adapter_Currency(String[] dataSet1, String[] dataSet2, String[] dataSet3) {
//        localDataSet1 = dataSet1;
//        localDataSet2 = dataSet2;
//        localDataSet3 = dataSet3;
//        disp1 = Arrays.asList(dataSet1);
//        disp2 = Arrays.asList(dataSet2);
//        disp3 = Arrays.asList(dataSet3);
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        // Create a new view, which defines the UI of the list item
//        View view = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.design_currency, viewGroup, false);
//
//        return new ViewHolder(view);
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//
//        // Get element from your dataset at this position and replace the
//        // contents of the view with that element
//
//        viewHolder.txtCode.setText(disp1.get(position));
//        viewHolder.txtSymbol.setText(disp2.get(position));
//        viewHolder.txtCountry.setText(disp3.get(position));
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return disp1.size();
//    }
//
//    String getItem(int pos) {
//        return disp1.get(pos)+" - "+disp2.get(pos);
//    }
//    String getCode(int pos) {
//        return disp1.get(pos).toLowerCase();
//    }
//    String getSym(int pos) {
//        return disp2.get(pos);
//    }
//
//    void filter(String text){
//        List<String> temp1 = new ArrayList<String>();
//        List<String> temp2 = new ArrayList<String>();
//        List<String> temp3 = new ArrayList<String>();
//        text = text.toUpperCase();
//        for(int i=0; i<localDataSet1.length; i++){
//            if(localDataSet1[i].contains(text) || localDataSet3[i].toUpperCase().contains(text)){
//                temp1.add(localDataSet1[i]);
//                temp2.add(localDataSet2[i]);
//                temp3.add(localDataSet3[i]);
//            }
//        }
//        disp1 = temp1;
//        disp2 = temp2;
//        disp3 = temp3;
//        notifyDataSetChanged();
//    }
//
//}