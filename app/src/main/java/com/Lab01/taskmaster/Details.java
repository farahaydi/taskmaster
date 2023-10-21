package com.Lab01.taskmaster;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
public class Details extends AppCompatActivity {
    public static final String TASK01_TAG="task1";
    public static final String TASK02_TAG="task2";
    public static final String TASK03_TAG="task3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView taskView = findViewById(R.id.textView9);

        Intent getTasks = getIntent();

        String task01 = getTasks.getStringExtra(TASK01_TAG);
        if (task01 != null) {
            taskView.setText(task01);
        }

        String task02 = getTasks.getStringExtra(TASK02_TAG);
        if (task02 != null) {
            taskView.setText(task02);
        }

        String task03 = getTasks.getStringExtra(TASK03_TAG);
        if (task03 != null) {
            taskView.setText(task03);
        }
    }

}