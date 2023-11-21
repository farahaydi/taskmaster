package com.Lab01.taskmaster;

import android.app.Application;
import android.util.Log;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;


import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;


public class ToDoAmplifyApplication extends Application {
    public static final String TAG = "toDoApplication";
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException ae) {
            Log.e(TAG, "Error Initializing Amplify " + ae.getMessage(), ae);
        }
    }
}
