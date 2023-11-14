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
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EditTask extends AppCompatActivity {
    public static final String TAG = "editTaskActivity";
    private CompletableFuture<Task> taskCompletableFuture = null;
    private CompletableFuture<List<Team>> teamFuture = null;
    private Task taskToEdit = null;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Spinner taskStatusSpinner = null;
    private Spinner teamSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        taskCompletableFuture = new CompletableFuture<>();
        teamFuture = new CompletableFuture<>();

        setUpEditItems();
        setUpSaveButton();
        setUpDeleteButton();
    }

    private void setUpEditItems() {
        Intent callingIntent = getIntent();
        String taskId = null;
        if (callingIntent != null) {
            taskId = callingIntent.getStringExtra(MainActivity.TASK_ID_TAG);
        }
        String taskId2 = taskId;
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read tasks Successfully");

                    for (Task databaseproduct : success.getData()) {
                        if (databaseproduct.getId().equals(taskId2)) {
                            taskCompletableFuture.complete(databaseproduct);
                        }
                    }

                    runOnUiThread(() -> {
                    });
                },
                failure -> Log.i(TAG, "Did not read task successfully")
        );
        try {
            taskToEdit = taskCompletableFuture.get();
        } catch (InterruptedException ie) {
            Log.e(TAG, "InterruptedException while getting task");
            Thread.currentThread().interrupt();
        } catch (ExecutionException ee) {
            Log.e(TAG, "ExecutionException while getting task");
        }

        titleEditText = findViewById(R.id.titleEdit);
        titleEditText.setText(taskToEdit.getTitle());
        descriptionEditText = findViewById(R.id.DescEdit);
        descriptionEditText.setText(taskToEdit.getBody());
        setUpSpinners();
    }

    private void setUpSpinners() {
        teamSpinner = findViewById(R.id.spinnerTeamEdit);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read teams successfully!");
                    ArrayList<String> contactNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for (Team team : success.getData()) {
                        teams.add(team);
                        contactNames.add(team.getName());
                    }
                    teamFuture.complete(teams);

                    runOnUiThread(() -> {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                contactNames));
                        teamSpinner.setSelection(getSpinnerIndex(teamSpinner, taskToEdit.getTeamTask().getName()));
                    });
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.i(TAG, "Did not read contacts successfully!");
                }
        );

        taskStatusSpinner = findViewById(R.id.spinner_statusEdit);
        taskStatusSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));
        taskStatusSpinner.setSelection(getSpinnerIndex(taskStatusSpinner, taskToEdit.getTeamTask().toString()));
    }

    private int getSpinnerIndex(Spinner spinner, String stringValueToCheck) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringValueToCheck)) {
                return i;
            }
        }
        return 0;
    }

    private void setUpSaveButton() {
        Button saveButton = findViewById(R.id.saveEdit);
        saveButton.setOnClickListener(v -> {
            if (taskToEdit != null) {
                updateTask();
            }
        });
    }


    private void updateTask() {
        List<Team> teams = null;
        String contactToSaveString = teamSpinner.getSelectedItem().toString();
        try {
            teams = teamFuture.get();
        } catch (InterruptedException ie) {
            Log.e(TAG, "InterruptedException while getting product");
            Thread.currentThread().interrupt();
        } catch (ExecutionException ee) {
            Log.e(TAG, "ExecutionException while getting product");
        }
        Team contactToSave = teams.stream().filter(c -> c.getName().equals(contactToSaveString)).findAny().orElseThrow(RuntimeException::new);
        Task taskToSave = Task.builder()
                .title(titleEditText.getText().toString())
                .body(descriptionEditText.getText().toString())
                .teamTask(contactToSave)
                .state(taskStateFromString(taskStatusSpinner.getSelectedItem().toString()))
                .id(taskToEdit.getId())
                .build();

        Amplify.API.mutate(
                ModelMutation.update(taskToSave),
                successResponse -> {
                    Log.i(TAG, "EditTaskActivity.onCreate(): edited a task successfully");
                    Snackbar.make(findViewById(R.id.editTask), "Task saved!", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditTask.this, MainActivity.class);
                    startActivity(intent);
                },
                failureResponse -> Log.i(TAG, "EditTaskActivity.onCreate(): failed with this response: " + failureResponse)
        );
    }

    public static TaskState taskStateFromString(String inputProductCategoryText) {
        for (TaskState taskState : TaskState.values()) {
            if (taskState.toString().equals(inputProductCategoryText)) {
                return taskState;
            }
        }
        return null;
    }

    private void setUpDeleteButton() {
        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(v -> {
            if (taskToEdit != null) {
                Amplify.API.mutate(
                        ModelMutation.delete(taskToEdit),
                        successResponse -> {
                            Log.i(TAG, "EditTaskActivity.onCreate(): deleted a task successfully");
                            Intent goToProductListActivity = new Intent(EditTask.this, MainActivity.class);
                            startActivity(goToProductListActivity);
                        },
                        failureResponse -> Log.i(TAG, "EditTaskActivity.onCreate(): failed with this response: " + failureResponse)
                );
            }
        });
    }
}
