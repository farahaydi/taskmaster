package com.Lab01.taskmaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Lab01.taskmaster.adapter.TaskAdapter;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String USERNAME_TAG = "tasks";
    public static final String USERNAME_File = "SettingsFile";
    List<Task> tasks = new ArrayList<>();
    TaskAdapter taskAdapter;
    public final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = new ArrayList<>();
        RecyclerView TaskRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        TaskRecyclerView.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(tasks, this);

        TaskRecyclerView.setAdapter(taskAdapter);

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read Tasks successfully");
                    tasks.clear();
                    for (Task databaseTask : success.getData()){
                        tasks.add(databaseTask);
                    }
                    runOnUiThread(() ->{
                        taskAdapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.e(TAG, "Failed to read tasks")
        );


        Button addTask = findViewById(R.id.button);
        Button allTasks = findViewById(R.id.button2);
        Button settings = findViewById(R.id.button8);
        TextView ourUsername = findViewById(R.id.maniUserName);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToMain2 = new Intent(MainActivity.this, AddTask.class);
                startActivity(moveToMain2);
            }
        });

        allTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToMain3 = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(moveToMain3);
            }
        });

        settings.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        });

        sharedPreferences = getSharedPreferences(USERNAME_File, Context.MODE_PRIVATE);
        String getUserName = sharedPreferences.getString(USERNAME_TAG, "Username NotFound !");
        ourUsername.setText(getUserName);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
