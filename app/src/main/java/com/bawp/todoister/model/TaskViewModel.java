package com.bawp.todoister.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bawp.todoister.data.TodoisterRepo;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    public static TodoisterRepo repo;
    public final LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repo = new TodoisterRepo(application);
        allTasks = repo.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<Task> get(long id) {
        return repo.get(id);
    }

    public static void insert(Task task) {
        repo.insert(task);
    }

    public static void update(Task task) {
        repo.update(task);
    }

    public static void delete(Task task) {
        repo.delete(task);
    }

}
