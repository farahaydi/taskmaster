package com.Lab01.taskmaster.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.Lab01.taskmaster.model.Task;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert
    public void insertTask(Task task);

    @Query("select * from Task")
    public List<Task>findAll();
    @Query("select title from Task")
    public List<Task>findByTitle();

    @Query("select * from Task where id = :id")
    Task findByAnId(long id);

}
