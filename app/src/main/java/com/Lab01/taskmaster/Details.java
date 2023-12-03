package com.Lab01.taskmaster;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.InputStream;
import android.media.MediaPlayer;

import com.amplifyframework.core.Amplify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Details extends AppCompatActivity {
    public static final String TAG = "DetailsActivity";
    private MediaPlayer mp = null;
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
            Findtitle.setText(title);
            FindBody.setText(body);
            Findstatus.setText(statusString);

            mp= new MediaPlayer();
            setUpSpeakButton();
        }}


        private void setUpSpeakButton () {
            Button speakButton = (Button) findViewById(R.id.textToSpeach);
            speakButton.setOnClickListener(b ->
            {
                String productName = ((TextView) findViewById(R.id.title04)).getText().toString();

                Amplify.Predictions.convertTextToSpeech(
                        productName,
                        result -> playAudio(result.getAudioData()),
                        error -> Log.e(TAG, "conversion failed ", error)
                );
            });
        }

        private void playAudio(InputStream data) {
            File mp3File = new File(getCacheDir(), "audio.mp3");

            try (OutputStream out = new FileOutputStream(mp3File)) {
                byte[] buffer = new byte[8 * 1_024];
                int bytesRead;
                while ((bytesRead = data.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                mp.reset();
                mp.setOnPreparedListener(MediaPlayer::start);
                mp.setDataSource(new FileInputStream(mp3File).getFD());
                mp.prepareAsync();
            } catch (IOException error) {
                Log.e("MyAmplifyApp", "Error writing audio file", error);
            }
        }
    }
