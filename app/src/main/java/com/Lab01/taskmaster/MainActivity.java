package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String USERNAME_TAG="tasks";
    public static final String USERNAME_File="SettingsFile";
    public static final String TASK02_TAG="task2";
    public static final String TASK03_TAG="task3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addTask = findViewById(R.id.button);
        Button allTasks =findViewById(R.id.button2);
        Button task01 =findViewById(R.id.button5);
        Button task02 =findViewById(R.id.button6);
        Button task03 =findViewById(R.id.button7);
        Button settings =findViewById(R.id.button8);
        TextView ourUsername =findViewById(R.id.maniUserName);
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
        ////////////////////>>>>>>>Lab02>>>>>>//////////////////////////

        settings.setOnClickListener(view ->
        {
            Intent intent =new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        });
        sharedPreferences = getSharedPreferences(USERNAME_File, Context.MODE_PRIVATE);
        String getUserName = sharedPreferences.getString(USERNAME_TAG, "Username NotFound !");
        ourUsername.setText(getUserName);


        task01.setOnClickListener(view -> {
            String taskText01 = task01.getText().toString();
            Intent taskDetails = new Intent(MainActivity.this, Details.class);
            taskDetails.putExtra(Details.TASK01_TAG, taskText01);
            startActivity(taskDetails);
        });


        task02.setOnClickListener(view -> {
            String taskTest02=task02.getText().toString();
            Intent taskDetails = new Intent(MainActivity.this, Details.class);
            taskDetails.putExtra(TASK02_TAG,taskTest02);
            startActivity(taskDetails);
        });
        task03.setOnClickListener(view -> {
            String taskText03=task03.getText().toString();
            Intent taskDetails = new Intent(MainActivity.this, Details.class);
            taskDetails.putExtra(TASK03_TAG,taskText03);
            startActivity(taskDetails);
        });

    }



    }
