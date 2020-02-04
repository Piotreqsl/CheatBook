package com.onionv2.cheatbook;


import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "cheats_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Cheat.CREATE_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Cheat.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertCheat(String subject, String uris){
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put(Cheat.COLUMN_SUBJECT, subject);
        values.put(Cheat.COLUMN_URIS, uris);

        long id = db.insert(Cheat.TABLE_NAME, null, values);

        db.close();
        return id;

    }


    public Cheat getCheat(long id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Cheat.TABLE_NAME,
                new String[]{Cheat.COLUMN_ID, Cheat.COLUMN_SUBJECT, Cheat.COLUMN_URIS, Cheat.COLUMN_TIMESTAMP},
                Cheat.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null );

        if(cursor != null) cursor.moveToFirst();

        Cheat cheat = new Cheat(
                cursor.getInt(cursor.getColumnIndex(Cheat.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Cheat.COLUMN_SUBJECT)),
                cursor.getString(cursor.getColumnIndex(Cheat.COLUMN_URIS)),
                cursor.getString(cursor.getColumnIndex(Cheat.COLUMN_TIMESTAMP))
        );

        db.close();

        return cheat;
    }


    public List<Cheat> getAllNotes() {
        List<Cheat> cheats = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Cheat.TABLE_NAME + " ORDER BY " +
                Cheat.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Cheat note = new Cheat();
                note.setId(cursor.getInt(cursor.getColumnIndex(Cheat.COLUMN_ID)));
                note.setSubject(cursor.getString(cursor.getColumnIndex(Cheat.COLUMN_SUBJECT)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Cheat.COLUMN_TIMESTAMP)));
                note.setUris(cursor.getString(cursor.getColumnIndex(Cheat.COLUMN_URIS)));


                cheats.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return cheats;
    }



    public int updateCheat(Cheat cheat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Cheat.COLUMN_URIS, cheat.getUris());
        values.put(Cheat.COLUMN_SUBJECT, cheat.getSubject());

        // updating row
        return db.update(Cheat.TABLE_NAME, values, Cheat.COLUMN_ID + " = ?",
                new String[]{String.valueOf(cheat.getId())});
    }

    public void deleteNote(Cheat note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Cheat.TABLE_NAME, Cheat.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }


}