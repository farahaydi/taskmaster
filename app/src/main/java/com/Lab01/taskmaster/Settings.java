package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String USERNAME_TAG = "tasks";
    public static final String USERNAME_File = "SettingsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button saveButton = findViewById(R.id.save);
        EditText username = findViewById(R.id.editTextText3);

        saveButton.setOnClickListener(view -> {
            sharedPreferences = getSharedPreferences(USERNAME_File, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String userName = username.getText().toString();
            editor.putString(USERNAME_TAG, userName);
            editor.apply();
            Intent intent =new Intent (Settings.this, MainActivity.class);
            startActivity(intent);
        });
    }
}