package com.example.todo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Todo.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();

    private static final String DB_NAME = "todo_db";
    private static TodoDatabase instance;

    static TodoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context,
                    TodoDatabase.class,
                    DB_NAME
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
