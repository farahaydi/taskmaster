package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {

    public static final String TEAM_TAG = "team";
    Spinner teamSpinner;
    List<String> teams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupTeamSpinner();
        SharedPreferenceUsername();
        backButton();
    }


    private void SharedPreferenceUsername(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        EditText usernameEditText = findViewById(R.id.username);
        Button saveUsername= findViewById(R.id.button);
        teamSpinner = findViewById(R.id.settingsSpinner);

        saveUsername.setOnClickListener( view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String username = usernameEditText.getText().toString();
            String selectedTeam = teamSpinner.getSelectedItem().toString();

            editor.putString("username", username);
            editor.putString(TEAM_TAG, selectedTeam);
            editor.apply();

            Toast.makeText(Settings.this, "UserName Changed!", Toast.LENGTH_SHORT).show();

        });
    }

    private void setupTeamSpinner() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        teamSpinner = findViewById(R.id.settingsSpinner);

        teams = new ArrayList<>();
        teams.add("Reem");
        teams.add("Fofa");
        teams.add("Farah");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teams);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(adapter);

        String teamString = sharedPreferences.getString(Settings.TEAM_TAG, "No team found");
        setSpinnerToValue(teamSpinner, teamString);
    }

    private void setSpinnerToValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void backButton(){
        Button settingsBackButton=findViewById(R.id.settingsBackButton);
        settingsBackButton.setOnClickListener(view -> {
            Intent backToHomeFromSettings=new Intent(Settings.this, MainActivity.class);
            startActivity(backToHomeFromSettings);
        });
    }
}