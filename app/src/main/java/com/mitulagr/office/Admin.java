package com.mitulagr.office;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class Admin extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button logout, addEmp, temp;
    private EditText search;
    private RecyclerView Emps;
    private DBHandler db;
    private Adapter_Emp ade;
    int Join[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        logout = (Button) findViewById(R.id.logout);
        addEmp = (Button) findViewById(R.id.button2);
        search = (EditText) findViewById(R.id.search);
        Emps = (RecyclerView) findViewById(R.id.AddEmp);

        db = new DBHandler(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("Current User", 0);
                editor.commit();
                finish();
            }
        });

        addEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmployee();
            }
        });

        Emps.setLayoutManager(new LinearLayoutManager(this));

        int tod[] = new int[]{0,0,0};
        tod[0] = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        tod[1] = Calendar.getInstance().get(Calendar.MONTH)+1;
        tod[2] = Calendar.getInstance().get(Calendar.YEAR);

        ade = new Adapter_Emp(this,String.format("%04d-%02d-%02d",tod[2],tod[1],tod[0]));

        Emps.setAdapter(ade);

        ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        ade.local = db.getAllEmployees();
                        ade.filter();
                        ade.notifyDataSetChanged();
                    }
                });

        ade.setOnItemClickListener(new Adapter_Emp.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                editor.putString("Employee", ade.disp.get(position).email);
                editor.commit();
                //startActivity(new Intent(Admin.this, AdminEmp.class));
                startActivityForResult.launch(new Intent(Admin.this, AdminEmp.class));
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ade.qry = search.getText().toString();
                ade.filter();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    void addEmployee(){
        Dialog curd = new Dialog(Admin.this);
        curd.setContentView(R.layout.addemployee);
        curd.getWindow();
        curd.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        curd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        curd.show();

        EditText EName = (EditText) curd.findViewById(R.id.editTextTextPersonName3);
        EditText EMail = (EditText) curd.findViewById(R.id.editTextTextPersonName5);
        EditText EContact = (EditText) curd.findViewById(R.id.editTextTextPersonName6);
        EditText EDep = (EditText) curd.findViewById(R.id.editTextTextPersonName7);
        EditText EPass = (EditText) curd.findViewById(R.id.editTextTextPersonName9);
        Button EJoin = (Button) curd.findViewById(R.id.joindate);
        Button EAdd = (Button) curd.findViewById(R.id.button3);
        temp = EJoin;
        Join = new int[]{0,0,0};
        Join[0] = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Join[1] = Calendar.getInstance().get(Calendar.MONTH)+1;
        Join[2] = Calendar.getInstance().get(Calendar.YEAR);
        EJoin.setText(String.format("%04d-%02d-%02d",Join[2],Join[1],Join[0]));

        EJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(Join[0],Join[1],Join[2]);
            }
        });

        EAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.EmailExist(EMail.getText().toString())){
                    EMail.requestFocus();
                    EMail.setError("Email Already Exists");
                    return;
                }

                Employee employee = new Employee();

                employee.name = EName.getText().toString();
                employee.email = EMail.getText().toString();
                employee.number = EContact.getText().toString();
                employee.dep = EDep.getText().toString();
                employee.pass = EPass.getText().toString();
                employee.join = EJoin.getText().toString();
                employee.active = 1;

                db.addEmployee(employee);

                ade.local = db.getAllEmployees();
                ade.filter();

                curd.dismiss();
            }
        });
    }

    private void showDatePicker(int day, int month, int year) {
        month = month-1;
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}