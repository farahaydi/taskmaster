package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addTask = findViewById(R.id.button);
        Button allTasks =findViewById(R.id.button2);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToMain2 =new Intent(MainActivity.this,MainActivity2.class);
                startActivity(moveToMain2);
            }
        });

        allTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToMain3 =new Intent(MainActivity.this,MainActivity3.class);
                startActivity(moveToMain3);
            }
        });
    }
}