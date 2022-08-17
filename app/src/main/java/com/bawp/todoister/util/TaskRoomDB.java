package com.bawp.todoister.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bawp.todoister.data.TaskDao;
import com.bawp.todoister.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TaskRoomDB extends RoomDatabase {
    public static final int NUMBER_OF_THREADS = 4;
    public static volatile TaskRoomDB INSTANCE;
    public static final String DB_NAME = "todoister_db";

    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final RoomDatabase.Callback sRoomDBCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriterExecutor.execute(() -> {
                // invoke Dao and write
                TaskDao taskDao = INSTANCE.taskDao();
                taskDao.deleteAll(); // clean slate! (task_table)

            });
        }
    };

    public static TaskRoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskRoomDB.class, DB_NAME).addCallback(sRoomDBCallBack).build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract TaskDao taskDao();
}
