package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_STATUS + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public int getTaskId(String name, String status, String description) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS,
                new String[]{COLUMN_ID},
                COLUMN_NAME + "=? AND " + COLUMN_STATUS + "=? AND " + COLUMN_DESCRIPTION + "=?",
                new String[]{name, status, description},
                null, null, null);

        int taskId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            taskId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            cursor.close();
        }
        db.close();
        return taskId;
    }


    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, task.getName());
        values.put(COLUMN_STATUS, task.getStatus());
        values.put(COLUMN_DESCRIPTION, task.getDescription());

        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_STATUS, COLUMN_DESCRIPTION},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                );
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }
}
