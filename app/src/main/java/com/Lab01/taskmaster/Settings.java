package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends AppCompatActivity {

    public static final String UserNAME_EXTRA_TAG="productName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUpSaveButton();
    }



    private void setUpSaveButton(){

        Button SaveButton = (Button) findViewById(R.id.save);

        SaveButton.setOnClickListener(V -> {

            String productName = ((EditText)findViewById(R.id.editTextText3)).getText().toString();

            Intent goToOrderFromIntent = new Intent(Settings.this, MainActivity.class);
            goToOrderFromIntent.putExtra(UserNAME_EXTRA_TAG, productName);
            startActivity(goToOrderFromIntent);
        });
    }
}