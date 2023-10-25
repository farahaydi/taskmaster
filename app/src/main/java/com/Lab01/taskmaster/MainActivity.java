package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.Lab01.taskmaster.DataBase.ToDoDataBase;
import com.Lab01.taskmaster.adapter.TaskAdapter;
import com.Lab01.taskmaster.model.Task;
import com.Lab01.taskmaster.model.TaskState;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String USERNAME_TAG = "tasks";
    public static final String USERNAME_File = "SettingsFile";
    public static final String DATABASE_NAME = "to_do_task";
    ToDoDataBase toDoDataBase;
    List <Task> tasks=null;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoDataBase = Room.databaseBuilder(
                        getApplicationContext(),
                        ToDoDataBase.class,
                        DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        tasks=toDoDataBase.taskDAO().findAll();

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
                Intent moveToMain2 =new Intent(MainActivity.this, AddTask.class);
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
            Log.d("MainActivity", "Button 1 clicked");
            String taskText = task01.getText().toString();
            Intent taskDetails = new Intent(MainActivity.this, Details.class);
            taskDetails.putExtra("taskText", taskText);
            startActivity(taskDetails);
        });


        task02.setOnClickListener(view -> {
            Log.d("MainActivity", "Button 2 clicked");
            String taskText = task02.getText().toString();
            Intent taskDetails = new Intent(MainActivity.this, Details.class);
            taskDetails.putExtra("taskText", taskText);
            startActivity(taskDetails);
        });

        task03.setOnClickListener(view -> {
            Log.d("MainActivity", "Button 2 clicked");
            String taskText = task03.getText().toString();
            Intent taskDetails = new Intent(MainActivity.this, Details.class);
            taskDetails.putExtra("taskText", taskText);
            startActivity(taskDetails);
        });


        /////////////////>>>>Lab03<<<<////////////////////////
        RecyclerView TaskRecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        TaskRecyclerView.setLayoutManager(layoutManager);

//        List<Task> tasks = new ArrayList<>();
//
//        tasks.add(new Task("Task01","This is my 01 Task", TaskState.NEW));
//        tasks.add(new Task("Task02","This is my 02 Task",TaskState.ASSIGNED));
//        tasks.add(new Task("Task03","This is my 03 Task",TaskState.IN_PROGRESS));
//        tasks.add(new Task("Task04","This is my 04 Task",TaskState.COMPLETE));

        taskAdapter = new TaskAdapter(tasks, this);
        TaskRecyclerView.setAdapter(taskAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        tasks.clear();
        tasks.addAll(toDoDataBase.taskDAO().findAll());
        taskAdapter.notifyDataSetChanged();
    }


    }
