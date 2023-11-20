package com.Lab01.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AddTask extends AppCompatActivity {
    private int num = 0;
    public static final String TAG = "AddTask";

    Spinner teamSpinner = null;
    Spinner addStateSpinner=null;
    CompletableFuture<List<Team>> contactFuture = new CompletableFuture<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);


        contactFuture = new CompletableFuture<>();
        setUpSpinners();
        setUpSaveButton();
    }

    private void setUpSpinners() {
        teamSpinner = findViewById(R.id.teamSpinnerStin);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success ->
                {
                    Log.i(TAG, "Read Contact Successfully");
                    ArrayList<String> contactNames = new ArrayList<>();
                    ArrayList<Team> contacts = new ArrayList<>();
                    for (Team team : success.getData()) {
                        contacts.add(team);
                        contactNames.add(team.getName());
                    }
                    contactFuture.complete(contacts);
                    runOnUiThread(() ->
                    {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                contactNames
                        ));
                    });
                },
                failure -> {
                    contactFuture.complete(null);
                    Log.i(TAG, "Did not read contacts successfully");
                }
        );

        addStateSpinner = findViewById(R.id.addState);
        addStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()
        ));
    }

    private void setUpSaveButton() {
        Button submitTask = findViewById(R.id.addTaskButton);
        submitTask.setOnClickListener(view -> {
            try {
                String task = ((EditText) findViewById(R.id.addTaskEditText)).getText().toString();
                String description = ((EditText) findViewById(R.id.addDescEditText)).getText().toString();
                String selectTeam = teamSpinner.getSelectedItem().toString();

                List<Team> teams = contactFuture.get();

                Team selectedContact = teams.stream()
                        .filter(c -> c.getName().equals(selectTeam))
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("Selected team not found"));

                Task newTask = Task.builder()
                        .title(task)
                        .body(description)
                        .state((TaskState) addStateSpinner.getSelectedItem())
                        .teamTask(selectedContact)
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(newTask),
                        successResponse -> {
                            Log.i(TAG, "AddTask.onCreate(): task created");
                            Intent bb = new Intent(AddTask.this, MainActivity.class);
                            startActivity(bb);
                            Snackbar.make(findViewById(R.id.addTask), "Task saved!", Snackbar.LENGTH_SHORT).show();
                        },
                        failureResponse -> {
                            Log.e(TAG, "AddTask.onCreate(): task failed", failureResponse);
                            Snackbar.make(findViewById(R.id.addTask), "Failed to save task", Snackbar.LENGTH_SHORT).show();
                        }
                );

            } catch (Exception e) {
                Log.e(TAG, "Error adding task", e);
                Snackbar.make(findViewById(R.id.addTask), "Error adding task", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}