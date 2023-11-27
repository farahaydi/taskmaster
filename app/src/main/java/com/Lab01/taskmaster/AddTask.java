package com.Lab01.taskmaster;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTask extends AppCompatActivity {
    public static final String TAG = "AddTask";

    Spinner teamSpinner;
    Spinner addStateSpinner;
    CompletableFuture<List<Team>> teamFuture = new CompletableFuture<>();
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Spinner taskStatusSpinner = null;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String s3ImageKey = "";

    private CompletableFuture<Task> taskCompletableFuture = null;

    private Task taskToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        teamFuture = new CompletableFuture<>();
        activityResultLauncher = getImagePickingActivityResultLauncher();

        setUpSpinners();
        setUpSaveButton();
        setUpAddImageButton();
        setUpDeleteImageButton();
        updateImageButtons();
    }
    @Override
    protected void onResume()
    {
        super.onResume();

        Intent callingIntent = getIntent();
        if (callingIntent != null && callingIntent.getType() != null && callingIntent.getType().equals("text/plain")) {
            String callingText = callingIntent.getStringExtra(Intent.EXTRA_TEXT);

            if (callingText != null) {
                String cleanedText = cleanText(callingText);

                ((EditText) findViewById(R.id.titleEditText)).setText(cleanedText);
            }
        }

        if(callingIntent != null && callingIntent.getType() != null && callingIntent.getType().startsWith("image") ){
            Uri incomingImageFileUri= callingIntent.getParcelableExtra(Intent.EXTRA_STREAM);

            if (incomingImageFileUri != null){
                InputStream incomingImageFileInputStream = null;

                try {
                    incomingImageFileInputStream = getContentResolver().openInputStream(incomingImageFileUri);

                    ImageView productImageView = findViewById(R.id.editProductImageImageView);

                    if (productImageView != null) {

                        productImageView.setImageBitmap(BitmapFactory.decodeStream(incomingImageFileInputStream));
                    }else {
                        Log.e(TAG, "ImageView is null for some reasons");
                    }
                }catch (FileNotFoundException fnfe){
                    Log.e(TAG," Could not get file stream from the URI "+fnfe.getMessage(),fnfe);
                }
            }
        }

    }
    private String cleanText(String text) {
        text = text.replaceAll("\\b(?:https?|ftp):\\/\\/\\S+\\b", "");

        text = text.replaceAll("\"", "");

        return text;
    }


    private void setUpSpinners() {
        teamSpinner = findViewById(R.id.teamSpinnerStin);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read Contact Successfully");
                    ArrayList<String> contactNames = new ArrayList<>();
                    ArrayList<Team> contacts = new ArrayList<>();
                    for (Team team : success.getData()) {
                        contacts.add(team);
                        contactNames.add(team.getName());
                    }
                    teamFuture.complete(contacts);
                    runOnUiThread(() -> {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                contactNames
                        ));
                    });
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.i(TAG, "Did not read contacts successfully");
                }
        );

        addStateSpinner = findViewById(R.id.addStateSpinner);
        addStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskState.values()
        ));
    }

    private void setUpSaveButton() {
        Button saveButton = findViewById(R.id.addTaskButton);
        saveButton.setOnClickListener(v -> {
            saveTask(s3ImageKey);
        });
    }

    private void saveTask(String imageS3Key) {
        Button submitTask = findViewById(R.id.addTaskButton);
        submitTask.setOnClickListener(view -> {
            try {
                String task = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
                String description = ((EditText) findViewById(R.id.descriptionEditText)).getText().toString();
                String selectTeam = teamSpinner.getSelectedItem().toString();

                List<Team> teams = teamFuture.get();

                Team selectedContact = teams.stream()
                        .filter(c -> c.getName().equals(selectTeam))
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("Selected team not found"));

                Task newTask = Task.builder()
                        .title(task)
                        .body(description)
                        .state((TaskState) addStateSpinner.getSelectedItem())
                        .teamTask(selectedContact)
                        .taskImageS3Key(imageS3Key)
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

    private void setUpAddImageButton() {
        Button addImageButton = findViewById(R.id.editProductAddImageButton);
        addImageButton.setOnClickListener(b -> {
            launchImageSelectionIntent();
        });
    }

    private void setUpDeleteImageButton() {
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

    private void launchImageSelectionIntent() {
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});

        activityResultLauncher.launch(imageFilePickingIntent);
    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Button addImageButton = findViewById(R.id.editProductAddImageButton);
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (result.getData() != null) {
                                Uri pickedImageFileUri = result.getData().getData();
                                try {
                                    InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                    String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                    Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);

                                    // Part 3: Use our InputStream to upload file to S3
                                    switchFromAddButtonToDeleteButton(addImageButton);
                                    uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename, pickedImageFileUri);

                                } catch (FileNotFoundException fnfe) {
                                    Log.e(TAG, "Could not get file from file picker! " + fnfe.getMessage(), fnfe);
                                }
                            }
                        } else {
                            Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                        }
                    }
                }
        );
    }
    private void switchFromDeleteButtonToAddButton(Button deleteImageButton) {
        Button addImageButton = findViewById(R.id.editProductAddImageButton);
        deleteImageButton.setVisibility(View.INVISIBLE);
        addImageButton.setVisibility(View.VISIBLE);
    }

    private void switchFromAddButtonToDeleteButton(Button addImageButton) {
        Button deleteImageButton = findViewById(R.id.editProductDeleteImageButton);
        deleteImageButton.setVisibility(View.VISIBLE);
        addImageButton.setVisibility(View.INVISIBLE);
    }

    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename,Uri pickedImageFileUri)
    {
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageInputStream,
                success ->
                {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                    saveTask(success.getKey());
                    updateImageButtons();
                    ImageView productImageView = findViewById(R.id.editProductImageImageView);
                    InputStream pickedImageInputStreamCopy = null;
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



    public static TaskState taskStateFromString(String inputProductCategoryText) {
        for (TaskState taskState : TaskState.values()) {
            if (taskState.toString().equals(inputProductCategoryText)) {
                return taskState;
            }
        }
        return null;
    }
}
