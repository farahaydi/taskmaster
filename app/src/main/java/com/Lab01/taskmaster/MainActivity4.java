package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        String status = intent.getStringExtra("status");

        TextView Findtitle =findViewById(R.id.title02);
        Findtitle.setText(title);
        TextView FindBody =findViewById(R.id.body);
        FindBody.setText(body);
        TextView Findstatus = findViewById(R.id.states);
        Findstatus.setText(status);
    }
}