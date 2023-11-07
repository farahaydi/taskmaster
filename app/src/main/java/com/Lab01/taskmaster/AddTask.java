    package com.Lab01.taskmaster;

    import androidx.appcompat.app.AppCompatActivity;

    import android.os.Bundle;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Spinner;
    import android.widget.TextView;

    import com.Lab01.taskmaster.model.Task;
    import com.Lab01.taskmaster.model.TaskState;

    public class AddTask extends AppCompatActivity {
        private int num = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_addtask);



            Spinner taskStateSpinner = findViewById(R.id.addState);
            taskStateSpinner.setAdapter(new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    TaskState.values()));

            Button submitTask=findViewById(R.id.button3);
    //        Toast toast=Toast.makeText(this, "submitted !",Toast.LENGTH_SHORT);
            TextView counter = findViewById(R.id.textView5);
            submitTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
    //                toast.show();
                    Task addTask =new Task(
                            ((EditText)findViewById(R.id.editTextText)).getText().toString(),
                            ((EditText)findViewById(R.id.editTextText2)).getText().toString(),
                    TaskState.fromString(taskStateSpinner.getSelectedItem().toString()));
                    num++;
                    counter.setText(String.valueOf(num));
//                    toDoDataBase.taskDAO().insertTask(addTask);
                }
            });
        }
    }