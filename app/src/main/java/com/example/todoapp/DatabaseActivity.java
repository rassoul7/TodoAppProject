package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseActivity extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_DESCRIPTION = "description";

    public DatabaseActivity(Context context) {
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



    public boolean updateTask(int id, String newTaskName, String newStatus, String newDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, newTaskName);
        contentValues.put(COLUMN_STATUS, newStatus);
        contentValues.put(COLUMN_DESCRIPTION, newDescription);
        int affectedRows = db.update(TABLE_TASKS, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close(); // Fermer la base de données après l'opération
        return affectedRows > 0;
    }



    public boolean insertTask(String task, String description, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", task);
        contentValues.put("description", description);
        contentValues.put("status", status);
        long result = db.insert("tasks", null, contentValues);
        return result != -1;
    }

    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);
    }
}