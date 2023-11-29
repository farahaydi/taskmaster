package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Lab01.taskmaster.adapter.TaskAdapter;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_ID_TAG = "Task Id Tag";

    private String selectedTeam;
    public static final String TAG = "homeActivity";
    private TaskAdapter taskAdapter;
    List<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //>>>>>>>>>>>>>>>>>>>>>>Lab36<<<<<<<<<<<<<<<<<<<<<<


//        Amplify.Auth.signUp("farahelaydi9@gmail.com",
//                "farah1234",
//                AuthSignUpOptions.builder()
//                        .userAttribute(AuthUserAttributeKey.email(), "farahelaydi9@gmail.com")
//                        .userAttribute(AuthUserAttributeKey.nickname(), "Fefe")
//                        .build(),
//                good ->
//                {
//                    Log.i(TAG, "Signup succeeded: "+ good.toString());
//                },
//                bad ->
//                {
//                    Log.i(TAG, "Signup failed with username: "+ "farahelaydi9@gmail.com"+ " with this message: "+ bad.toString());
//                }
//        );

//        Amplify.Auth.confirmSignUp("farahelaydi9@gmail.com",
//                "225494",
//                success ->
//                {
//                    Log.i(TAG,"verification succeeded: "+ success.toString());
//
//                },
//                failure ->
//                {
//                    Log.i(TAG,"verification failed: "+ failure.toString());
//                }
//        );

//        Amplify.Auth.signIn("farahelaydi9@gmail.com",
//                "farah1234",
//                success ->
//                {
//                    Log.i(TAG, "Login succeeded: " + success.toString());
//                },
//                failure ->
//                {
//                    Log.i(TAG, "Login failed: " + failure.toString());
//                }
//        );

//        Amplify.Auth.signOut(
//                () ->
//                {
//                    Log.i(TAG, "Logout succeeded");
//                },
//                failure ->
//                {
//                    Log.i(TAG, "Logout failed");
//                }
//        );

//        taskDatabase = Room.databaseBuilder(
//                        getApplicationContext(),
//                        TaskDatabase.class,
//                        DATABASE_NAME)
//                .fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
//                .build();
//        tasks= taskDatabase.taskDao().findAll();

        //>>>>>>>>>>>Lab37<<<<<<<<<<<<<<<<<

        String emptyFilename= "emptyTestFileName";
        File emptyFile = new File(getApplicationContext().getFilesDir(), emptyFilename);

        try {
            BufferedWriter emptyFileBufferedWriter= new BufferedWriter(new FileWriter(emptyFile));

            emptyFileBufferedWriter.append("Some text here from Farah\nAnother libe from Farah");

            emptyFileBufferedWriter.close();
        }catch (IOException ioe){
            Log.i(TAG, "could not write locally with filename: "+ emptyFilename);
        }

        String emptyFileS3Key = "someFileOnS3.txt";
        Amplify.Storage.uploadFile(
                emptyFileS3Key,
                emptyFile,
                success ->
                {
                    Log.i(TAG, "S3 upload succeeded and the Key is: " + success.getKey());
                },
                failure ->
                {
                    Log.i(TAG, "S3 upload failed! " + failure.getMessage());
                }
        );




        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selectedTeam = sharedPreferences.getString(Settings.TEAM_TAG, "");

        amplifier();
        setUpTaskListRecyclerView();
        queryTasks();
        AddTaskButton();
        AllTasksButton();
        SettingsButton();
        setUpLoginAndLogoutButton ();
    }

    // Shared Preference
    @Override
    protected void onResume() {
        super.onResume();
//        TextView user = findViewById(R.id.usernameTextView);
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        String username = sharedPreferences.getString("username", "DefaultUsername");
//        user.setText(username + "'s Tasks:");

//        tasks.addAll(taskDatabase.taskDao().findAll());
//        taskAdapter.notifyDataSetChanged();


        AuthUser authUser = Amplify.Auth.getCurrentUser();
        String username = "";
        if (authUser == null) {
            Button loginButton = (Button) findViewById(R.id.productListLoginButton);
            loginButton.setVisibility(View.VISIBLE);
            Button logoutButton = (Button) findViewById(R.id.productListLogoutButton);
            logoutButton.setVisibility(View.INVISIBLE);
        } else {
            username = authUser.getUsername();
            Log.i(TAG, "Username is: " + username);
            Button loginButton = (Button) findViewById(R.id.productListLoginButton);
            loginButton.setVisibility(View.INVISIBLE);
            Button logoutButton = (Button) findViewById(R.id.productListLogoutButton);
            logoutButton.setVisibility(View.VISIBLE);

            String username2 = username;
            Amplify.Auth.fetchUserAttributes(
                    success ->
                    {
                        Log.i(TAG, "Fetch user attributes succeeded for username: " + username2);
                        for (AuthUserAttribute userAttribute : success) {
                            if (userAttribute.getKey().getKeyString().equals("email")) {
                                String userEmail = userAttribute.getValue();
                                runOnUiThread(() ->
                                {
                                    ((TextView) findViewById(R.id.usernameTextView)).setText(userEmail);
                                });
                            }
                        }
                    },
                    failure ->
                    {
                        Log.i(TAG, "Fetch user attributes failed: " + failure.toString());
                    }
            );
        }
    }

    public void amplifier() {
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
                success -> {
                    Log.i(TAG, "Read tasks successfully");
                    tasks.clear();
                    for (Task databaseTask : success.getData()) {
                        ;
                        tasks.add(databaseTask);
                    }
                    runOnUiThread(() -> {
                        taskAdapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "failed to read tasks")
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


    private void setUpTaskListRecyclerView() {
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

    private void setUpLoginAndLogoutButton () {
        Button loginButton = (Button) findViewById(R.id.productListLoginButton);
        loginButton.setOnClickListener(v ->
        {
            Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLogInIntent);
        });

        Button logoutButton = (Button) findViewById(R.id.productListLogoutButton);
        logoutButton.setOnClickListener(v ->
        {
            Amplify.Auth.signOut(
                    () ->
                    {
                        Log.i(TAG, "Logout succeeded");
                        runOnUiThread(() ->
                        {
                            ((TextView) findViewById(R.id.usernameTextView)).setText("");
                        });
                        Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(goToLogInIntent);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Logout failed");
                        runOnUiThread(() ->
                        {
                            Toast.makeText(MainActivity.this, "Log out failed", Toast.LENGTH_LONG);
                        });
                    }
            );
        });
    }
}