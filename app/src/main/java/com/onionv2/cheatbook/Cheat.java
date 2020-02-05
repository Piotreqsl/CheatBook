package com.onionv2.cheatbook;

public class Cheat {
    public static final String TABLE_NAME = "cheats";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_URIS= "uris";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String uris;
    private String timestamp;
    private String subject;
    private String title;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SUBJECT + " TEXT,"
                    + COLUMN_URIS + " TEXT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Cheat() {
    }

    public Cheat(int id,String title, String subject,String uris , String timestamp) {
        this.id = id;
        this.uris = uris;
        this.timestamp = timestamp;
        this.subject = subject;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String note) {
        this.subject = note;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUris(String uris){
        this.uris = uris;
    }

    public String getUris(){
        return uris;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }



}