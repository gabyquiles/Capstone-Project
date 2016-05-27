package com.gabyquiles.eventy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gabyquiles.eventy.data.EventContract.EventEntry;
import com.gabyquiles.eventy.data.EventContract.GuestEntry;
import com.gabyquiles.eventy.data.EventContract.ThingEntry;

/**
 * Manages local database
 *
 * @author gabrielquiles-perez
 */
public class EventDBHelper extends SQLiteOpenHelper{
    private final String LOG_TAG = EventDBHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "events.db";

    public EventDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " + EventEntry.TABLE_NAME + "(" +
                EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EventEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                EventEntry.COLUMN_TITLE + " TEXT NULL, " +
                EventEntry.COLUMN_PLACE_NAME + " TEXT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_EVENTS_TABLE);

        final String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + GuestEntry.TABLE_NAME + "(" +
                GuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GuestEntry.COLUMN_EVENT_KEY + " INTEGER NOT NULL, " +
                GuestEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                GuestEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                GuestEntry.COLUMN_STATUS + " INTEGER NOT NULL," +
                GuestEntry.COLUMN_THING + " TEXT NULL," +
                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + GuestEntry.COLUMN_EVENT_KEY + ") REFERENCES " +
                EventEntry.TABLE_NAME + " (" + EventEntry._ID + "));";
        sqLiteDatabase.execSQL(SQL_CREATE_GUESTS_TABLE);

        final String SQL_CREATE_THINGS_TABLE = "CREATE TABLE " + ThingEntry.TABLE_NAME + "(" +
                ThingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ThingEntry.COLUMN_THING + " TEXT NOT NULL, " +
                ThingEntry.COLUMN_EVENT_KEY + " INTEGER NOT NULL, " +
                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + ThingEntry.COLUMN_EVENT_KEY + ") REFERENCES " +
                EventEntry.TABLE_NAME + " (" + EventEntry._ID + "));";
        sqLiteDatabase.execSQL(SQL_CREATE_THINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Make sure this is the first time installing the DB
        if(newVersion == 1 && oldVersion < newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ThingEntry.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GuestEntry.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
