package com.bawp.todoister.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.TaskRoomDB;

import java.util.List;

public class TodoisterRepo {
    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;


    public TodoisterRepo(Application application) {
        TaskRoomDB db = TaskRoomDB.getDatabase(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        TaskRoomDB.databaseWriterExecutor.execute(() -> taskDao.insertTask(task));
    }

    public LiveData<Task> get(long id) {
        return taskDao.get(id);
    }

    public void update(Task task) {
        TaskRoomDB.databaseWriterExecutor.execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        TaskRoomDB.databaseWriterExecutor.execute(() -> taskDao.delete(task));
    }

}
