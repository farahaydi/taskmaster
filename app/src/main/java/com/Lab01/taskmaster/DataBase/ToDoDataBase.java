package com.Lab01.taskmaster.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.Lab01.taskmaster.DAO.TaskDAO;
import com.Lab01.taskmaster.model.Task;

@Database(entities = {Task.class},version = 1)
public abstract class ToDoDataBase extends RoomDatabase {
    public abstract TaskDAO taskDAO();
}
