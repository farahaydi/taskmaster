package com.Lab01.taskmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Lab01.taskmaster.Details;
import com.Lab01.taskmaster.MainActivity;
import com.Lab01.taskmaster.MainActivity4;
import com.Lab01.taskmaster.R;
import com.Lab01.taskmaster.model.Task;
import com.Lab01.taskmaster.model.TaskState;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    List<Task> tasks;
    Context callingActivity;

    public TaskAdapter(List<Task> tasks, Context callingActivity) {
        this.tasks = tasks;
        this.callingActivity = callingActivity;
    }    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent,false);
        return new TaskViewHolder(taskFragment);
    }

//    @Override
//    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
//        TextView taskFragmentTextView = (TextView) holder.itemView.findViewById(R.id.productFragmentTextView);
//        String taskTitle = tasks.get(position).getTitle();
//        String taskBody =tasks.get(position).getBody();
//        TaskState taskStatus=tasks.get(position).getState();
//        taskFragmentTextView.setText(position +". "+ taskTitle);
//
//
//        TextView productFragmentTextView = (TextView) holder.itemView.findViewById(R.id.productFragmentTextView);
//        Task task = tasks.get(position);
//        productFragmentTextView.setText(position +". "+ task.getTitle());
//
//
//
//        View productViewHolder = holder.itemView;
//        TextView findTaskBody=holder.itemView.findViewById(R.id.body);
//        productViewHolder.setOnClickListener(view -> {
//            Intent goToOrderFormIntent = new Intent(callingActivity, Details.class);
//            goToOrderFormIntent.putExtra("title", task.getTitle());
//            goToOrderFormIntent.putExtra("body", task.getBody());
//            goToOrderFormIntent.putExtra("status", task.getState().toString());
//            callingActivity.startActivity(goToOrderFormIntent);
//        });
//    }
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        TextView taskFragmentTextView = holder.itemView.findViewById(R.id.productFragmentTextView);
        taskFragmentTextView.setText(position + ". " + task.getTitle());

        holder.itemView.setOnClickListener(view -> {
            Intent goToDetailsIntent = new Intent(callingActivity, Details.class);
            goToDetailsIntent.putExtra("title", task.getTitle());
            goToDetailsIntent.putExtra("body", task.getBody());
            goToDetailsIntent.putExtra("status", task.getState().toString());
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