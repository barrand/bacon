package com.barrand.bacon.app.db;
 
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class VisitDatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "visitsManager";
 
    // Visits table name
    private static final String TABLE_VISITS = "visits";
 
    // Visits Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ENTRY_TIME = "entryTime";
    private static final String KEY_EXIT_TIME = "exitTime";
    private static final String KEY_DWELL_TIME = "dwellTime";
    private static final String KEY_MQ_KEY = "mqKey";
 
    public VisitDatabaseHandler(Context context) {
//    	super(context,"/sdcard/visit/visit.db", null, DATABASE_VERSION);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VISITS_TABLE = "CREATE TABLE " + TABLE_VISITS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        		+ KEY_NAME + " TEXT,"
                + KEY_ENTRY_TIME + " INT,"
                + KEY_EXIT_TIME + " INT,"
                + KEY_DWELL_TIME + " INT,"
                + KEY_MQ_KEY + " TEXT"
        		+ ")";
        db.execSQL(CREATE_VISITS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITS);

        // Create tables again
        onCreate(db);
    }
//
//    /**
//     * All CRUD(Create, Read, Update, Delete) Operations
//     */
//
//    // Adding new visit
//    public void addVisit(Visit visit) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, visit.name);
//        values.put(KEY_ENTRY_TIME, visit.entryTime);
//        values.put(KEY_EXIT_TIME, visit.exitTime);
//        values.put(KEY_DWELL_TIME, visit.dwellTime);
//        values.put(KEY_MQ_KEY, visit.mqkey);
//
//        // Inserting Row
//        visit.sqliteId = db.insert(TABLE_VISITS, null, values);
//        db.close(); // Closing database connection
//    }
//
//    // Getting All Visits
//    public ArrayList<Visit> getAllVisits() {
//        ArrayList<Visit> visitList = new ArrayList<Visit>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_VISITS;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Visit visit = new Visit();
//                visit.sqliteId = Long.parseLong(cursor.getString(0));
//                visit.name = cursor.getString(1);
//                visit.entryTime = cursor.getLong(2);
//                visit.exitTime = cursor.getLong(3);
//                visit.dwellTime = cursor.getLong(4);
//                visit.mqkey = cursor.getString(5);
//                // Adding visit to list
//                visitList.add(visit);
//            } while (cursor.moveToNext());
//        }
//
//        // return visit list
//        return visitList;
//    }
//
//    // Deleting single visit
//    public void deleteVisit(Visit visit) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_VISITS, KEY_ID + " = ?",
//                new String[] { String.valueOf(visit.sqliteId) });
//        db.close();
//    }
//
//	public int deleteAllVisits() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int deleted = db.delete(TABLE_VISITS, null, null);
//        return deleted;
//	}
 
}