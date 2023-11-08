    package com.Lab01.taskmaster;

    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Spinner;
    import android.widget.TextView;

    import com.amplifyframework.api.graphql.model.ModelMutation;
    import com.amplifyframework.core.Amplify;
    import com.amplifyframework.datastore.generated.model.Task;
    import com.amplifyframework.datastore.generated.model.TaskState;

    public class AddTask extends AppCompatActivity {
        private int num = 0;
        public static final String TAG="AddTask";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_addtask);

            Spinner taskStateSpinner = findViewById(R.id.addState);
            taskStateSpinner.setAdapter(new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    TaskState.values()));

            Button submitTask=findViewById(R.id.addTaskButton);
    //        Toast toast=Toast.makeText(this, "submitted !",Toast.LENGTH_SHORT);
            TextView counter = findViewById(R.id.textView5);
            submitTask.setOnClickListener(view ->  {
//                @Override
//                public void onClick(View view) {
//    //                toast.show();
//                    Task addTask =new Task(
//                            ((EditText)findViewById(R.id.editTextText)).getText().toString(),
//                            ((EditText)findViewById(R.id.editTextText2)).getText().toString(),
//                    TaskState.fromString(taskStateSpinner.getSelectedItem().toString()));
//                    num++;
//                    counter.setText(String.valueOf(num));
////                    toDoDataBase.taskDAO().insertTask(addTask);
//                }
                String task = ((EditText)findViewById(R.id.addTaskEditText)).getText().toString();
                String description =((EditText)findViewById(R.id.addDescEditText)).getText().toString();
                Task newTask=Task.builder()
                        .title(task)
                        .body(description)
                        .state(taskStateSpinner.getSelectedItem()).build();
                Amplify.API.mutate(
                        ModelMutation.create(newTask),
                        successResponse -> Log.i(TAG,"AddTask.onCreate():task created"),
                        failureResponse ->Log.e(TAG,"AddTask.onCreate(): task fail"+failureResponse)
                );

                Intent bb =new Intent (AddTask.this,MainActivity.class);
                startActivity(bb);

            });
        }
    }