package com.mitulagr.office;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private TextView user, name, change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.button);
        user = (TextView) findViewById(R.id.textView2);
        name = (TextView) findViewById(R.id.textView2);
        change = (TextView) findViewById(R.id.textView2);
    }
}