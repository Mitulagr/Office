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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

public class EmpProfile extends Fragment implements DatePickerDialog.OnDateSetListener {

    public EmpProfile() {}

    public static EmpProfile newInstance(String param1, String param2) {
        EmpProfile fragment = new EmpProfile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Button logout;
    private DBHandler db;
    private TextView DEmail, DName, DNumber, DDep, DJoin, DPass;
    private ImageButton MName, MNumber, MDep, MJoin, MPass;
    private Employee employee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emp_profile, container, false);

        db = new DBHandler(getContext());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        employee = db.getEmployee(sp.getString("Employee",""));

        logout = rootView.findViewById(R.id.logout);

        DEmail = rootView.findViewById(R.id.textView7);
        DName = rootView.findViewById(R.id.textView9);
        MName = rootView.findViewById(R.id.modifyName);
        DNumber = rootView.findViewById(R.id.textView11);
        MNumber = rootView.findViewById(R.id.modifyNumber);
        DDep = rootView.findViewById(R.id.textView13);
        MDep = rootView.findViewById(R.id.modifyDep);
        DJoin = rootView.findViewById(R.id.textView15);
        MJoin = rootView.findViewById(R.id.modifyJoin);
        DPass = rootView.findViewById(R.id.textView17);
        MPass = rootView.findViewById(R.id.modifyPass);

        DEmail.setText(employee.email);
        DName.setText(employee.name);
        DNumber.setText(employee.number);
        DDep.setText(employee.dep);
        DJoin.setText(employee.join);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("Current User", 0);
                editor.commit();
                getActivity().finish();
            }
        });

        MName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEdit(0,DName);
            }
        });

        MNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEdit(1,DNumber);
            }
        });

        MDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEdit(2,DDep);
            }
        });

        MJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        MPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEdit(3,DPass);
            }
        });

        return rootView;
    }

    void showEdit(int attr, TextView prev){
        Dialog curd = new Dialog(getContext());
        curd.setContentView(R.layout.editemp);
        curd.getWindow();
        curd.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        curd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        curd.show();

        EditText EText = (EditText) curd.findViewById(R.id.editTextemp);
        Button EDone = (Button) curd.findViewById(R.id.buttonemp);

        if(attr==1) EText.setInputType(InputType.TYPE_CLASS_PHONE);
        if(attr==3) EText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        EText.setText(prev.getText().toString());
        if(attr==3) EText.setText(employee.pass);

        EDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(attr==0) employee.name = EText.getText().toString();
                if(attr==1) employee.number = EText.getText().toString();
                if(attr==2) employee.dep = EText.getText().toString();
                if(attr==3) employee.pass = EText.getText().toString();

                if(attr!=3) prev.setText(EText.getText().toString());

                db.updateEmployee(employee);

                curd.dismiss();
            }
        });
    }

    private void showDatePicker() {
        String jn = DJoin.getText().toString();
        int year = Integer.parseInt(jn.substring(0,4));
        int month = Integer.parseInt(jn.substring(5,7));
        int day = Integer.parseInt(jn.substring(8));
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
        employee.join = date;
        db.updateEmployee(employee);
        DJoin.setText(date);
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}