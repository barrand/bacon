package com.barrand.bacon.app.db;
 
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.barrand.bacon.app.model.Trip;


public class TripDatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "tripsManager";
 
    // Trips table name
    private static final String TABLE_TRIPS = "trips";
 
    // Trips Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_ARRIVE_TIME = "arriveTime";
    private static final String KEY_DURATION_TIME = "durationTime";
 
    public TripDatabaseHandler(Context context) {
//    	super(context,"/sdcard/trip/trip.db", null, DATABASE_VERSION);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRIPS_TABLE = "CREATE TABLE " + TABLE_TRIPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        		+ KEY_NAME + " TEXT,"
                + KEY_START_TIME + " INT,"
                + KEY_ARRIVE_TIME + " INT,"
                + KEY_DURATION_TIME + " INT,"
        		+ ")";
        db.execSQL(CREATE_TRIPS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new trip
    public void addTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, trip.name);
        values.put(KEY_START_TIME, trip.startTime);
        values.put(KEY_ARRIVE_TIME, trip.arriveTime);
        values.put(KEY_DURATION_TIME, trip.durationTime);

        // Inserting Row
        trip.sqliteId = db.insert(TABLE_TRIPS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Trips
    public ArrayList<Trip> getAllTrips() {
        ArrayList<Trip> tripList = new ArrayList<Trip>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRIPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Trip trip = new Trip();
                trip.sqliteId = Long.parseLong(cursor.getString(0));
                trip.name = cursor.getString(1);
                trip.startTime = cursor.getLong(2);
                trip.arriveTime = cursor.getLong(3);
                trip.durationTime = cursor.getLong(4);
                trip.mqkey = cursor.getString(5);
                // Adding trip to list
                tripList.add(trip);
            } while (cursor.moveToNext());
        }

        // return trip list
        return tripList;
    }

    // Deleting single trip
    public void deleteTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIPS, KEY_ID + " = ?",
                new String[] { String.valueOf(trip.sqliteId) });
        db.close();
    }

	public int deleteAllTrips() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleted = db.delete(TABLE_TRIPS, null, null);
        return deleted;
	}
 
}