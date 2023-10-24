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

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    List<Task> tasks;
    Context callingActivity;

    public TaskAdapter(List<Task> products, Context callingActivity) {
        this.tasks = products;
        this.callingActivity = callingActivity;
    }    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent,false);
        return new TaskViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        TextView taskFragmentTextView = (TextView) holder.itemView.findViewById(R.id.productFragmentTextView);
        String taskTitle = tasks.get(position).getTitle();
        String taskBody =tasks.get(position).getBody();
        String taskStatus=tasks.get(position).getState();
        taskFragmentTextView.setText(position +". "+ taskTitle);
        View productViewHolder = holder.itemView;
        TextView findTaskBody=holder.itemView.findViewById(R.id.body);


        productViewHolder.setOnClickListener(view -> {
            Intent goToOrderFormIntent = new Intent(callingActivity, MainActivity4.class);
            goToOrderFormIntent.putExtra("title", taskTitle);
            goToOrderFormIntent.putExtra("body", taskBody);
            goToOrderFormIntent.putExtra("status", taskStatus);
            callingActivity.startActivity(goToOrderFormIntent);
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
