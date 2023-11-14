package com.Lab01.taskmaster.adapter;

import static com.Lab01.taskmaster.MainActivity.TASK_ID_TAG;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Lab01.taskmaster.Details;
import com.Lab01.taskmaster.EditTask;
import com.Lab01.taskmaster.MainActivity;
import com.Lab01.taskmaster.MainActivity4;
import com.Lab01.taskmaster.R;
import com.Lab01.taskmaster.model.TaskState;
import com.amplifyframework.datastore.generated.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    List<Task> tasks;
    Context callingActivity;

    public TaskAdapter(List<Task> tasks, Context callingActivity) {
        this.tasks = tasks;
        this.callingActivity = callingActivity;
    }



    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent,false);
        return new TaskViewHolder(taskFragment);
    }
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        TextView taskFragmentTextView = holder.itemView.findViewById(R.id.productFragmentTextView);
        taskFragmentTextView.setText(position + ". " + task.getTitle());

        holder.itemView.setOnClickListener(view -> {
            Intent goToDetailsIntent = new Intent(callingActivity, EditTask.class);
            goToDetailsIntent.putExtra("title", task.getTitle());
            goToDetailsIntent.putExtra("body", task.getBody());
            goToDetailsIntent.putExtra("status", task.getState().toString());
            goToDetailsIntent.putExtra(TASK_ID_TAG, task.getId());
            callingActivity.startActivity(goToDetailsIntent);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}