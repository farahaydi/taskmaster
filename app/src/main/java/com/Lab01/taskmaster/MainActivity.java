package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

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
    public static final String USER_NICKNAME_TAG="tasks";

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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        task01.setOnClickListener(view -> {
            SharedPreferences.Editor preferneceEditor= sharedPreferences.edit();
            String userNicknameString = task01.getText().toString();

            preferneceEditor.putString(USER_NICKNAME_TAG, userNicknameString);//k,v
            preferneceEditor.apply();

            Intent moveDetails01 = new Intent(MainActivity.this, Details.class);
            startActivity(moveDetails01);
        });
        task02.setOnClickListener(view -> {
            SharedPreferences.Editor preferneceEditor= sharedPreferences.edit();
            String userNicknameString = task02.getText().toString();

            preferneceEditor.putString(USER_NICKNAME_TAG, userNicknameString);//k,v
            preferneceEditor.apply();

            Intent moveDetails02 = new Intent(MainActivity.this, Details.class);
            startActivity(moveDetails02);
        });
        task03.setOnClickListener(view -> {
            SharedPreferences.Editor preferneceEditor= sharedPreferences.edit();
            String userNicknameString = task03.getText().toString();

            preferneceEditor.putString(USER_NICKNAME_TAG, userNicknameString);//k,v
            preferneceEditor.apply();

            Intent moveDetails03 = new Intent(MainActivity.this, Details.class);
            startActivity(moveDetails03);
        });
        settings.setOnClickListener(view -> {
            Intent settingsPage =new Intent(MainActivity.this,Settings.class);
            startActivity(settingsPage);

        });
        Intent callingIntent = getIntent();
        String productNameString = null;

        if(callingIntent != null){
            productNameString = callingIntent.getStringExtra(Settings.UserNAME_EXTRA_TAG);
        }

        TextView orderFormTextView = (TextView) findViewById(R.id.orderFormTextView);

        if (productNameString != null){
            orderFormTextView.setText(productNameString);
        }else {
            orderFormTextView.setText("please enter user name !");
        }



    }

    }
