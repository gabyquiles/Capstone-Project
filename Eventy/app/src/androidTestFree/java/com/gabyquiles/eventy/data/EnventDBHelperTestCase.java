package com.gabyquiles.eventy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test the management of DB
 *
 * @author gabrielquiles-perez
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class EnventDBHelperTestCase {
    private final String LOG_TAG = EnventDBHelperTestCase.class.getSimpleName();
    private Context mContext;

    @Before
    public void deleteDatabase() {
        mContext = InstrumentationRegistry.getTargetContext();
        mContext.deleteDatabase(EventDBHelper.DATABASE_NAME);
    }

    @Test
    public void createTables() throws Throwable {
        final HashSet<String> tableNamesHashSet = new HashSet<>();
        tableNamesHashSet.add(EventContract.EventEntry.TABLE_NAME);
        tableNamesHashSet.add(EventContract.GuestEntry.TABLE_NAME);
        tableNamesHashSet.add(EventContract.ThingEntry.TABLE_NAME);

        //Make sure the Db does not exists
        mContext.deleteDatabase(EventDBHelper.DATABASE_NAME);

        SQLiteDatabase db = new EventDBHelper(mContext).getWritableDatabase();
        // Check that the db is open
        assertThat(db.isOpen(), is(true));

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertThat("Error: This means that the database has not been created correctly",
                c.moveToFirst(), is(true));

        // verify that the tables have been created
        do {
            String tableName = c.getString(0);
            if(!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")) {
                assertThat("Error: Table name not contained in the original specification",
                        tableNamesHashSet.remove(tableName), is(true));
            }
        } while( c.moveToNext() );


        assertThat("Error: Database created but without correct tables", tableNamesHashSet.isEmpty(), is(true));

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> eventColumnHashSet = new HashSet<>();
        eventColumnHashSet.add(EventContract.EventEntry._ID);
        eventColumnHashSet.add(EventContract.EventEntry.COLUMN_DATE);
        eventColumnHashSet.add(EventContract.EventEntry.COLUMN_PLACE_NAME);
        eventColumnHashSet.add(EventContract.EventEntry.COLUMN_TITLE);

        checkTableColumns(EventContract.EventEntry.TABLE_NAME, db, eventColumnHashSet);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> guestColumnHashSet = new HashSet<>();
        guestColumnHashSet.add(EventContract.GuestEntry._ID);
        guestColumnHashSet.add(EventContract.GuestEntry.COLUMN_NAME);
        guestColumnHashSet.add(EventContract.GuestEntry.COLUMN_EMAIL);
        guestColumnHashSet.add(EventContract.GuestEntry.COLUMN_EVENT_KEY);
        guestColumnHashSet.add(EventContract.GuestEntry.COLUMN_STATUS);
        guestColumnHashSet.add(EventContract.GuestEntry.COLUMN_THING);

        checkTableColumns(EventContract.GuestEntry.TABLE_NAME, db, guestColumnHashSet);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> thingColumnHashSet = new HashSet<>();
        thingColumnHashSet.add(EventContract.ThingEntry._ID);
        thingColumnHashSet.add(EventContract.ThingEntry.COLUMN_THING);
        thingColumnHashSet.add(EventContract.ThingEntry.COLUMN_EVENT_KEY);

        checkTableColumns(EventContract.ThingEntry.TABLE_NAME, db, thingColumnHashSet);

        c.close();
        db.close();
    }

    /**
     * Helper function to test for columns
     * @param tableName
     * @param db
     * @param columns
     */
    private void checkTableColumns(String tableName, SQLiteDatabase db, HashSet<String> columns) {
        // verify that tables has correct columns
        Cursor c = db.rawQuery("PRAGMA table_info(" + tableName + ")",
                null);

        assertThat("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst(), is(true));


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            assertThat("Error: Column name not contained in the original specification",
                    columns.remove(columnName), is(true));
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertThat("Error: The database doesn't contain all of the required " + tableName + " entry columns",
                columns.isEmpty(), is(true));
        c.close();
    }

    @Test
    public void insertEvent() {
//        Open database;
        SQLiteDatabase db = new EventDBHelper(
                this.mContext).getWritableDatabase();
        assertThat(db.isOpen(), is(true));

//        Insert event
        ContentValues eventValues = TestUtilities.createEventValues();
        Long eventRowId = insertValues(db, EventContract.EventEntry.TABLE_NAME, eventValues);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(EventContract.EventEntry.TABLE_NAME, null,
                EventContract.EventEntry._ID + " = ?",
                new String[] {eventRowId.toString()}, null, null, null);

        // Move the cursor to a valid database row
        assertThat(cursor.moveToFirst(), is(true));

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("ERROR: Row not as expected", cursor, eventValues);

        // Finally, close the cursor and database
        cursor.close();
        db.close();
    }

    @Test
    public void insertGuest() {
//        Open database;
        SQLiteDatabase db = new EventDBHelper(
                this.mContext).getWritableDatabase();
        assertThat(db.isOpen(), is(true));

//        Insert event
        ContentValues eventValues = TestUtilities.createEventValues();
        Long eventRowId = insertValues(db, EventContract.EventEntry.TABLE_NAME, eventValues);


        ContentValues guestValues = TestUtilities.createGuestValues(eventRowId);
        Long guestRowId = insertValues(db, EventContract.GuestEntry.TABLE_NAME, guestValues);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(EventContract.GuestEntry.TABLE_NAME, null,
                EventContract.GuestEntry._ID + " = ?",
                new String[] {guestRowId.toString()}, null, null, null);

        // Move the cursor to a valid database row
        assertThat(cursor.moveToFirst(), is(true));

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("ERROR: Row not as expected", cursor, guestValues);

        // Finally, close the cursor and database
        cursor.close();
        db.close();
    }


    @Test
    public void insertThing() {
//        Open database;
        SQLiteDatabase db = new EventDBHelper(
                this.mContext).getWritableDatabase();
        assertThat(db.isOpen(), is(true));

//        Insert event
        ContentValues eventValues = TestUtilities.createEventValues();
        Long eventRowId = insertValues(db, EventContract.EventEntry.TABLE_NAME, eventValues);


        ContentValues thingValues = TestUtilities.createThingValues(eventRowId);
        Long thingRowId = insertValues(db, EventContract.ThingEntry.TABLE_NAME, thingValues);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(EventContract.ThingEntry.TABLE_NAME, null,
                EventContract.ThingEntry._ID + " = ?",
                new String[] {thingRowId.toString()}, null, null, null);

        // Move the cursor to a valid database row
        assertThat(cursor.moveToFirst(), is(true));

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("ERROR: Row not as expected", cursor, thingValues);

        // Finally, close the cursor and database
        cursor.close();
        db.close();
    }

    private long insertValues(SQLiteDatabase db, String tableName, ContentValues values) {

        // Insert ContentValues into database and get a row ID back
        long rowId = db.insert(tableName, null, values);
        assertThat("Error: Record not inserted", rowId, is(not(-1L)));
        return rowId;
    }
}
