package com.mitulagr.office;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.Calendar;

public class EmpTask extends Fragment implements DatePickerDialog.OnDateSetListener {

    public EmpTask() {}

    public static EmpTask newInstance(String param1, String param2) {
        EmpTask fragment = new EmpTask();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Button addTask, temp;
    private DBHandler db;
    private SharedPreferences sp;
    int Join[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_emp_task, container, false);

        db = new DBHandler(getContext());
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        androidx.fragment.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_stats, new StatsFragment());
        transaction.commit();

        addTask = (Button) rootView.findViewById(R.id.addtask);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTask();

            }
        });

        return rootView;
    }

    void showTask(){
        Dialog curd = new Dialog(getContext());
        curd.setContentView(R.layout.addtask);
        curd.getWindow();
        curd.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        curd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        curd.show();

        EditText TDes = (EditText) curd.findViewById(R.id.editTextTextPersonName3);
        Button TType = (Button) curd.findViewById(R.id.editTextTextPersonName5);
        Button TDate = (Button) curd.findViewById(R.id.taskdate);
        View TView = (View) curd.findViewById(R.id.joindate);
        TextView TTime = (TextView) curd.findViewById(R.id.textView21);
        EditText TMins = (EditText) curd.findViewById(R.id.editTextTextPersonName9);
        Button TDone = (Button) curd.findViewById(R.id.button3);

        final int[] ttype = new int[]{0};

        TType.setText("Work");

        TType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(),TType);
                popup.getMenuInflater()
                        .inflate(R.menu.task_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        String typename = "";
                        int typeid = 0;

                        switch (menuItem.getItemId()) {

                            case R.id.tt1:
                                typename = "Work";
                                typeid = 0;
                                break;
                            case R.id.tt2:
                                typename = "Meeting";
                                typeid = 1;
                                break;
                            default:
                                typename = "Break";
                                typeid = 2;
                                break;
                        }

                        TType.setText(typename);
                        ttype[0] = typeid;

                        return true;
                    }
                });

                popup.show();
            }
        });

        temp = TDate;
        Join = new int[]{0,0,0};
        Join[0] = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Join[1] = Calendar.getInstance().get(Calendar.MONTH)+1;
        Join[2] = Calendar.getInstance().get(Calendar.YEAR);
        TDate.setText(String.format("%04d-%02d-%02d",Join[2],Join[1],Join[0]));

        int hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        boolean am = true;
        if(hr==0) hr=12;
        else if(hr==12) am=false;
        else if(hr>12){
            hr-=12;
            am = false;
        }
        String curr = String.valueOf(hr) + ":" + String.format("%02d ",(Calendar.getInstance().get(Calendar.MINUTE)/5)*5);
        if(am) curr = curr + "AM";
        else curr = curr + "PM";

        TTime.setText(curr);

        clock TClock = new clock(TView,TTime,curr);

        TDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        TDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Task task = new Task();

                task.ID = db.getTasksCount();
                task.des = TDes.getText().toString();
                task.type = ttype[0];
                task.email = sp.getString("Employee","");
                task.date = TDate.getText().toString();
                task.time = TTime.getText().toString();
                if(TMins.getText().toString().length()==0) task.mins=0;
                else task.mins = Integer.parseInt(TMins.getText().toString());

                db.addTask(task);

                androidx.fragment.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_stats, new StatsFragment());
                transaction.commit();

                curd.dismiss();
            }
        });

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
        temp.setText(date);
        Join[0] = i2;
        Join[1] = i1+1;
        Join[2] = i;
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}