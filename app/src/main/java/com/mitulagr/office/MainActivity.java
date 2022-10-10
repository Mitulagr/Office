package com.mitulagr.office;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private TextView user, name, change;
    private EditText log_id, password;
    private boolean isAdmin;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.button);
        user = (TextView) findViewById(R.id.textView2);
        name = (TextView) findViewById(R.id.textView);
        change = (TextView) findViewById(R.id.textView4);
        log_id = (EditText) findViewById(R.id.editTextTextPersonName);
        password = (EditText) findViewById(R.id.editTextTextPersonName2);
        isAdmin = false;

        db = new DBHandler(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        if(sp.getInt("Current User", 0)==1){
            startActivity(new Intent(MainActivity.this, Admin.class));
        }
        if(sp.getInt("Current User", 0)==2){
            startActivity(new Intent(MainActivity.this, Emp.class));
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAdmin){
                    if(log_id.getText().toString().equals(getString(R.string.admin_username))){
                        if(password.getText().toString().equals(getString(R.string.admin_username))){
                            editor.putInt("Current User", 1);
                            editor.commit();
                            startActivity(new Intent(MainActivity.this, Admin.class));
                        }
                        else {
                            // Wrong Admin Password
                            password.requestFocus();
                            password.setError("Password Incorrect");
                        }
                    }
                    else{
                        // Admin Username doesn't exist
                        log_id.requestFocus();
                        log_id.setError("Username dosen't exist");
                    }
                }
                else{
                    String email = log_id.getText().toString();
                    List<Employee> Employees = db.getAllEmployees();
                    boolean exist = false;
                    for(int i=0;i<Employees.size();i++){
                        if(Employees.get(i).email.equals(email)){
                            exist = true;
                            if(Employees.get(i).active==0){
                                log_id.requestFocus();
                                log_id.setError("Employee Deactivated");
                            }
                            else if(Employees.get(i).pass.equals(password.getText().toString())){
                                editor.putInt("Current User", 2);
                                editor.commit();
                                editor.putString("Employee", email);
                                editor.commit();
                                startActivity(new Intent(MainActivity.this, Emp.class));
                            }
                            else {
                                // Wrong Employee Password
                                password.requestFocus();
                                password.setError("Password Incorrect");
                            }
                            break;
                        }
                    }
                    if(!exist){
                        // Employee with email dosent exist
                        log_id.requestFocus();
                        log_id.setError("Employee with this email dosen't exist");
                    }
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdmin = !isAdmin;
                if(isAdmin){
                    user.setText("Admin");
                    change.setText("Employee? Login Here");
                    name.setText("Username :");
                }
                else{
                    user.setText("Employee");
                    change.setText("Admin? Login Here");
                    name.setText("Email ID :");
                }
            }
        });

    }
}