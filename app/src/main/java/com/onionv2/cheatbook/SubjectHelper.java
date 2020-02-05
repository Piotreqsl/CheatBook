package com.onionv2.cheatbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;



public class SubjectHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "subjects_db";
    private static final String TABLE_NAME = "subjects";
    private static final String COL_2 = "subject";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                     "id INTEGER PRIMARY KEY," +
                     "subject TEXT," +
                     "count INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public SubjectHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        ContentValues contentValues= new ContentValues();

        contentValues.put(COL_2, "Maths");contentValues.put("count", 0);db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Literature");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "English");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Norwegian");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Italian");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Spanish");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "German");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Russian");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Polish");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Art");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "History");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Computer Science");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Music");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Citizenship");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Law");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Business studies"); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Biology");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Physics");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Geography");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Chemistry");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Religion");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Drama");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Astronomy");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COL_2, "Others");contentValues.put("count", 0); db.insert(TABLE_NAME, null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY subject COLLATE NOCASE ASC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void incrementSubject(String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE subjects SET count = count + 1 WHERE subject = '" +  subject + "'";
        db.execSQL(query);
    }

    public void decrementSubject(String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE subjects SET count = count - 1 WHERE subject = '" +  subject + "'";
        db.execSQL(query);
    }




}
