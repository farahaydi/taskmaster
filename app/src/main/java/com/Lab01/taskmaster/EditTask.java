package com.Lab01.taskmaster;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskState;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String s3ImageKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        taskCompletableFuture = new CompletableFuture<>();
        teamFuture = new CompletableFuture<>();
        activityResultLauncher = getImagePickingActivityResultLauncher();


        setUpEditItems();
        setUpSaveButton();
        setUpDeleteButton();
        setUpAddImageButton();
        setUpDeleteImageButton();
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
        s3ImageKey =taskToEdit.getTaskImageS3Key();
        if (s3ImageKey != null && !s3ImageKey.isEmpty()) {
            Amplify.Storage.downloadFile(
                    s3ImageKey,
                    new File(getApplication().getFilesDir(), s3ImageKey),
                    success -> {
                        ImageView productImageView = findViewById(R.id.editProductImageImageView);
                        productImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                    },
                    failure -> {
                        Log.e(TAG, "Unable to get image from S3 for the product for S3 key: " + s3ImageKey + " for reason: " + failure.getMessage());
                    }
            );
        }
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
        taskStatusSpinner.setSelection(getSpinnerIndex(taskStatusSpinner, taskToEdit.getState().toString()));
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
            saveTask(s3ImageKey);
        });
    }
    private void saveTask(String imageS3Key)
    {
        List<Team> contacts = null;
        String contactToSaveString = teamSpinner.getSelectedItem().toString();
        try
        {
            contacts = teamFuture.get();
        }
        catch (InterruptedException ie)
        {
            Log.e(TAG, "InterruptedException while getting product");
            Thread.currentThread().interrupt();
        }
        catch (ExecutionException ee)
        {
            Log.e(TAG, "ExecutionException while getting product");
        }
        Team contactToSave = contacts.stream().filter(c -> c.getName().equals(contactToSaveString)).findAny().orElseThrow(RuntimeException::new);

        Task productToSave = Task.builder()
                .title(titleEditText.getText().toString())
                .body(descriptionEditText.getText().toString())
                .teamTask(contactToSave)
                .state(taskStateFromString(taskStatusSpinner.getSelectedItem().toString()))
                .taskImageS3Key(imageS3Key)
                .id(taskToEdit.getId())
                .build();

        Amplify.API.mutate(
                ModelMutation.update(productToSave),
                successResponse -> {
                    Log.i(TAG, "EditProductActivity.onCreate(): edited a product successfully");
                    Snackbar.make(findViewById(R.id.editTask), "Product saved!", Snackbar.LENGTH_SHORT).show();
                },
                failureResponse -> Log.i(TAG, "EditProductActivity.onCreate(): failed with this response: " + failureResponse)
        );
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

    private void setUpAddImageButton()
    {
        Button addImageButton = (Button) findViewById(R.id.editProductAddImageButton);
        addImageButton.setOnClickListener(b ->
        {
            launchImageSelectionIntent();
        });

    }

    private void launchImageSelectionIntent()
    {

        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");  // only allow one kind or category of file; if you don't have this, you get a very cryptic error about "No activity found to handle Intent"
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
        // Below is simple version for testing
        //startActivity(imageFilePickingIntent);

        // Part 2: Create an image picking activity result launcher
        activityResultLauncher.launch(imageFilePickingIntent);

    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher()
    {
        // Part 2: Create an image picking activity result launcher
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>()
                        {
                            @Override
                            public void onActivityResult(ActivityResult result)
                            {
                                Button addImageButton = findViewById(R.id.editProductAddImageButton);
                                if (result.getResultCode() == Activity.RESULT_OK)
                                {
                                    if (result.getData() != null)
                                    {
                                        Uri pickedImageFileUri = result.getData().getData();
                                        try
                                        {
                                            InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                            String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                            Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
                                            // Part 3: Use our InputStream to upload file to S3
                                            switchFromAddButtonToDeleteButton(addImageButton);
                                            uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename,pickedImageFileUri);

                                        } catch (FileNotFoundException fnfe)
                                        {
                                            Log.e(TAG, "Could not get file from file picker! " + fnfe.getMessage(), fnfe);
                                        }
                                    }
                                }
                                else
                                {
                                    Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                                }
                            }
                        }
                );

        return imagePickingActivityResultLauncher;
    }

    private void switchFromDeleteButtonToAddButton(Button deleteImageButton) {
        Button addImageButton = findViewById(R.id.editProductAddImageButton);
        deleteImageButton.setVisibility(View.INVISIBLE);
        addImageButton.setVisibility(View.VISIBLE);
    }

    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private void updateImageButtons() {
        Button addImageButton = findViewById(R.id.editProductAddImageButton);
        Button deleteImageButton = findViewById(R.id.editProductDeleteImageButton);
        runOnUiThread(() -> {
            if (s3ImageKey.isEmpty()) {
                deleteImageButton.setVisibility(View.INVISIBLE);
                addImageButton.setVisibility(View.VISIBLE);
            } else {
                deleteImageButton.setVisibility(View.VISIBLE);
                addImageButton.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void switchFromAddButtonToDeleteButton(Button addImageButton) {
        Button deleteImageButton = findViewById(R.id.editProductDeleteImageButton);
        deleteImageButton.setVisibility(View.VISIBLE);
        addImageButton.setVisibility(View.INVISIBLE);
    }

    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename,Uri pickedImageFileUri)
    {
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,  // S3 key
                pickedImageInputStream,
                success ->
                {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                    // Part 4: Update/save our Product object to have an image key
                    saveTask(success.getKey());
                    updateImageButtons();
                    ImageView productImageView = findViewById(R.id.editProductImageImageView);
                    InputStream pickedImageInputStreamCopy = null;  // need to make a copy because InputStreams cannot be reused!
                    try
                    {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    }
                    catch (FileNotFoundException fnfe)
                    {
                        Log.e(TAG, "Could not get file stream from URI! " + fnfe.getMessage(), fnfe);
                    }
                    productImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));

                },
                failure ->
                {
                    Log.e(TAG, "Failure in uploading file to S3 with filename: " + pickedImageFilename + " with error: " + failure.getMessage());
                }
        );
    }

    private void setUpDeleteImageButton()
    {
        Button deleteImageButton = (Button)findViewById(R.id.editProductDeleteImageButton);
        String s3ImageKey = this.s3ImageKey;
        deleteImageButton.setOnClickListener(v ->
        {
            Amplify.Storage.remove(
                    s3ImageKey,
                    success ->
                    {
                        Log.i(TAG, "Succeeded in deleting file on S3! Key is: " + success.getKey());

                    },
                    failure ->
                    {
                        Log.e(TAG, "Failure in deleting file on S3 with key: " + s3ImageKey + " with error: " + failure.getMessage());
                    }
            );
            ImageView productImageView = findViewById(R.id.editProductImageImageView);
            productImageView.setImageResource(android.R.color.transparent);

            saveTask("");
            switchFromDeleteButtonToAddButton(deleteImageButton);
        });
    }



}
