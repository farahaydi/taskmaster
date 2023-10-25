package com.Lab01.taskmaster.model;

import androidx.annotation.NonNull;

public enum TaskState {
    NEW("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete");

    private final String displayName;

    private TaskState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TaskState fromString(String possibleTaskText){
        for(TaskState task : TaskState.values()){
            if (task.displayName.equals(possibleTaskText)){
                return task;
            }
        }
        return null;
    }
    @NonNull
    @Override
    public String toString(){
        if(displayName == null){
            return "";
        }
        return displayName;
    }
}
