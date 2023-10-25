package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.Lab01.taskmaster.model.TaskState;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView taskView = findViewById(R.id.textView9);
        TextView bodyy = findViewById(R.id.textView11);
        TextView Findtitle = findViewById(R.id.title04);
        TextView FindBody = findViewById(R.id.body);
        TextView Findstatus = findViewById(R.id.states);

        Intent getTasks = getIntent();
        String taskText = getTasks.getStringExtra("taskText");

        if (taskText != null) {
            taskView.setText(taskText);
            bodyy.setVisibility(View.VISIBLE);
        } else {
            String title = getTasks.getStringExtra("title");
            String body = getTasks.getStringExtra("body");
            String statusString = getTasks.getStringExtra("status");
            TaskState status = TaskState.valueOf(statusString);
            Findtitle.setText(title);
            FindBody.setText(body);
            Findstatus.setText(statusString);
        }
    }
}
