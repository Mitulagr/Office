package com.mitulagr.office;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdminEmp extends AppCompatActivity {

    Button exit, deactivate;
    TextView name, dep;
    Employee employee;
    SharedPreferences sp;
    DBHandler db;
    boolean act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_emp);

        exit = (Button) findViewById(R.id.back);
        deactivate = (Button) findViewById(R.id.active);
        name = (TextView) findViewById(R.id.textView5);
        dep = (TextView) findViewById(R.id.textView6);

        db = new DBHandler(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        employee = db.getEmployee(sp.getString("Employee",""));

        name.setText(employee.name);
        dep.setText(employee.dep);

        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new StatsFragment());
        transaction.commit();

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(employee.active==0){
            act = false;
            deactivate.setText("Activate");
            deactivate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
        else{
            act = true;
            deactivate.setText("Deactivate");
            deactivate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D60606")));
        }

        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(act){
                    employee.active = 0;
                    deactivate.setText("Activate");
                    deactivate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                }
                else{
                    employee.active = 1;
                    deactivate.setText("Deactivate");
                    deactivate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D60606")));
                }
                act = !act;
                db.updateEmployee(employee);
            }
        });

    }
}