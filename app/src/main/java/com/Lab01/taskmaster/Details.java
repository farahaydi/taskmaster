package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;


public class Details extends AppCompatActivity {

    SharedPreferences preferences;
    String loremIpsumText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);


    }
    @Override
    protected void onResume() {
        super.onResume();

        String userTask = preferences.getString(MainActivity.USER_NICKNAME_TAG, "No Tasks Found");

        ((TextView)findViewById(R.id.textView9)).setText(getString(R.string.task_with_input, userTask));

    }

}