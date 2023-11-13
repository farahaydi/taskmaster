package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.Lab01.taskmaster.adapter.TaskAdapter;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //    public static  final String DATABASE_NAME = "tasks_stuff";
//    TaskDatabase taskDatabase;
    private String selectedTeam;
    public static final String TAG="homeActivity";
    private TaskAdapter taskAdapter;
    List<Task> tasks=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        taskDatabase = Room.databaseBuilder(
//                        getApplicationContext(),
//                        TaskDatabase.class,
//                        DATABASE_NAME)
//                .fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
//                .build();
//        tasks= taskDatabase.taskDao().findAll();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selectedTeam = sharedPreferences.getString(Settings.TEAM_TAG, "");

        amplifier();
        setUpTaskListRecyclerView();
        queryTasks();
        AddTaskButton();
        AllTasksButton();
        SettingsButton();
    }

    // Shared Preference
    @Override
    protected void onResume() {
        super.onResume();
        TextView user=findViewById(R.id.usernameTextView);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "DefaultUsername");
        user.setText(username +"'s Tasks:");

//        tasks.addAll(taskDatabase.taskDao().findAll());
//        taskAdapter.notifyDataSetChanged();
    }

    public void amplifier(){
//        Team team1=Team.builder()
//                .name("Reneh").build();
//
//        Team team2=Team.builder()
//                .name("Balqees").build();
//
//        Team team3=Team.builder()
//                .name("Farah").build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team1),
//                successResponse->Log.i(TAG,"HomeActivity.amplifier(): made team successfully."),
//                failedResponse->Log.i(TAG,"HomeActivity.amplifier(): failed to make team."+failedResponse)
//        );
//
//        Amplify.API.mutate(
//                ModelMutation.create(team2),
//                successResponse->Log.i(TAG,"HomeActivity.amplifier(): made team successfully."),
//                failedResponse->Log.i(TAG,"HomeActivity.amplifier(): failed to make team."+failedResponse)
//        );
//
//        Amplify.API.mutate(
//                ModelMutation.create(team3),
//                successResponse->Log.i(TAG,"HomeActivity.amplifier(): made team successfully."),
//                failedResponse->Log.i(TAG,"HomeActivity.amplifier(): failed to make team."+failedResponse)
//        );

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success->{
                    Log.i(TAG,"Read tasks successfully");
                    tasks.clear();
                    for (Task databaseTask : success.getData()) {;
                        tasks.add(databaseTask);
                    }
                    runOnUiThread(() -> {
                        taskAdapter.notifyDataSetChanged();
                    });
                },
                failure-> Log.i(TAG,"failed to read tasks")
        );
    }

    private void queryTasks() {
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read Task successfully");
                    tasks.clear();
                    for (Task databaseTask : success.getData()) {
                        Team teamTask = databaseTask.getTeamTask();
                        if (teamTask != null && teamTask.getName().equals(selectedTeam)) {
                            tasks.add(databaseTask);
                        }
                    }
                    runOnUiThread(() -> {
                        taskAdapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "Couldn't read tasks from DynamoDB ")
        );
    }


    private void setUpTaskListRecyclerView(){
        RecyclerView taskListRecycleReview = (RecyclerView) findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecycleReview.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(tasks, this);
        taskListRecycleReview.setAdapter(taskAdapter);

    }

    private void AddTaskButton() {
        Button addTaskButton = findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTaskFormIntent = new Intent(MainActivity.this, AddTask.class);
            startActivity(goToAddTaskFormIntent);
        });
    }

    private void AllTasksButton() {
        Button allTaskButton = findViewById(R.id.allTasks);
        allTaskButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(MainActivity.this, MainActivity3.class);
            startActivity(goToAllTasksIntent);
        });
    }


    private void SettingsButton() {
        Button settingsPage = findViewById(R.id.settingsButton);
        settingsPage.setOnClickListener(view -> {
            Intent goToSettings = new Intent(MainActivity.this, Settings.class);
            startActivity(goToSettings);
        });
    }
}