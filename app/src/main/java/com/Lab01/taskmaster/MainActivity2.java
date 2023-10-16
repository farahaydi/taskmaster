package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button submitTask=findViewById(R.id.button3);
        Toast toast=Toast.makeText(this, "submitted !",Toast.LENGTH_SHORT);
        TextView counter = findViewById(R.id.textView5);
        submitTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.show();
                num++;
                counter.setText(String.valueOf(num));
            }
        });
    }
}