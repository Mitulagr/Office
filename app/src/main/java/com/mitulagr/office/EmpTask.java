package com.mitulagr.office;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class EmpTask extends Fragment {

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

    private Button addTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_emp_task, container, false);

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



//        EAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(db.EmailExist(EMail.getText().toString())){
//                    EMail.requestFocus();
//                    EMail.setError("Email Already Exists");
//                    return;
//                }
//
//                Employee employee = new Employee();
//
//                employee.name = EName.getText().toString();
//                employee.email = EMail.getText().toString();
//                employee.number = EContact.getText().toString();
//                employee.dep = EDep.getText().toString();
//                employee.pass = EPass.getText().toString();
//                employee.join = EJoin.getText().toString();
//                employee.active = 1;
//
//                db.addEmployee(employee);
//
//                androidx.fragment.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_stats, new StatsFragment());
//                transaction.commit();
//
//                curd.dismiss();
//            }
//        });
    }

}