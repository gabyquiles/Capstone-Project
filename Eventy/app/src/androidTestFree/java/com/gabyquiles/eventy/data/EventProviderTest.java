package com.gabyquiles.eventy.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test Content Provider for events
 *
 * @author gabrielquiles-perez
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class EventProviderTest {
    private Context mContext;

    private void deleteAllRecordsFromDB() {
        EventDBHelper dbHelper = new EventDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(EventContract.EventEntry.TABLE_NAME, null, null);
        db.delete(EventContract.GuestEntry.TABLE_NAME, null, null);
        db.delete(EventContract.ThingEntry.TABLE_NAME, null, null);
        db.close();
    }

    @Before
    public void clearDatabase() {
        mContext = InstrumentationRegistry.getTargetContext();
        deleteAllRecordsFromDB();
    }

    @Test
    public void providerRegistration() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                EventProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertThat("Error: EventProvider is registered with authority: " + providerInfo.authority +
                " instead of authority: " + EventContract.CONTENT_AUTHORITY,
                    providerInfo.authority, is(EventContract.CONTENT_AUTHORITY));
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertThat("Error: EventProvider not registered at " + mContext.getPackageName(), true, is(false));
        }
    }

    @Test
    public void getType() {
        // content://com.example.android.eventy.free/events
        String type = mContext.getContentResolver().getType(EventContract.EventEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.eventy.free/events
        assertThat("Error: the EventEntry CONTENT_URI should return EventEnty.CONTENT_TYPE", type,
                is(EventContract.EventEntry.CONTENT_TYPE));

        // content://com.example.android.eventy.free/events/1
        type = mContext.getContentResolver().getType(EventContract.EventEntry.buildEventUri(1));
        // vnd.android.cursor.dir/com.example.android.eventy.free/events/1
        assertThat("Error: the EventEntry CONTENT_URI with id should return EventEnty.CONTENT_TYPE", type,
                is(EventContract.EventEntry.CONTENT_TYPE));

        long testDate = 1419120000L; // December 21st, 2014
        // content://com.example.android.eventy.free/events?date=20140612
        type = mContext.getContentResolver().getType(EventContract.EventEntry.buildEventWithDateUri(testDate));
        // vnd.android.cursor.dir/com.example.android.eventy.free/events?date=20140612
        assertThat("Error: the EventEntry CONTENT_URI with date should return EventEnty.CONTENT_TYPE", type,
                is(EventContract.EventEntry.CONTENT_TYPE));

        long testEventId = 1L;
        // content://com.example.android.eventy.free/guests?event=1
        type = mContext.getContentResolver().getType(EventContract.GuestEntry.buildEventGuestsUri(testEventId));
        // vnd.android.cursor.dir/com.example.android.eventy.free/guests?event=1
        assertThat("Error: the GuestEnty CONTENT_URI with date should return GuestEnty.CONTENT_TYPE", type,
                is(EventContract.GuestEntry.CONTENT_TYPE));

        // content://com.example.android.eventy.free/things?event=1
        type = mContext.getContentResolver().getType(EventContract.ThingEntry.buildEventThingsUri(testEventId));
        // vnd.android.cursor.dir/com.example.android.eventy.free/things?event=1
        assertThat("Error: the ThingEntry CONTENT_URI with date should return ThingEntry.CONTENT_TYPE", type,
                is(EventContract.ThingEntry.CONTENT_TYPE));

    }

    @Test
    public void eventQuery() {
        ContentValues eventValues = TestUtilities.createEventValues();
        TestUtilities.insertContentValues(mContext,
                EventContract.EventEntry.TABLE_NAME, eventValues);

        // Test the basic content provider query
        Cursor eventCursor = mContext.getContentResolver().query(
                EventContract.EventEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("Error: Event Cursor does not match the event content values",
                eventCursor, eventValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertThat("Error: Event Query did not properly set NotificationUri",
                    eventCursor.getNotificationUri(), is(EventContract.EventEntry.CONTENT_URI));
        }
    }

    @Test
    public void insertReadEvent() {
        ContentValues testEventValues = TestUtilities.createEventValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(EventContract.EventEntry.CONTENT_URI, true, tco);
        Uri eventUri = mContext.getContentResolver().insert(EventContract.EventEntry.CONTENT_URI, testEventValues);

        // If this fails, insert event isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long eventRowId = ContentUris.parseId(eventUri);

        // Verify we got a row back.
        assertThat("Error: No row was inserted", eventRowId, is(not(-1L)));

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor eventCursor = mContext.getContentResolver().query(
                EventContract.EventEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating EventEntry.",
                eventCursor, testEventValues);

        // Now add guests
        ContentValues guestValues = TestUtilities.createGuestValues(eventRowId);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(EventContract.GuestEntry.CONTENT_URI, true, tco);

        Uri guestsInsertUri = mContext.getContentResolver()
                .insert(EventContract.GuestEntry.CONTENT_URI, guestValues);
        assertThat("Error: Invalid URI for inserting Guests", guestsInsertUri, is(not(nullValue())));

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor guestCursor = mContext.getContentResolver().query(
                EventContract.GuestEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating GuestEntry insert.",
                guestCursor, guestValues);

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        guestValues.putAll(testEventValues);


        // Now add guests
        ContentValues thingValues = TestUtilities.createThingValues(eventRowId);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(EventContract.ThingEntry.CONTENT_URI, true, tco);

        Uri thingsInsertUri = mContext.getContentResolver()
                .insert(EventContract.ThingEntry.CONTENT_URI, thingValues);
        assertThat("Error: Invalid URI for inserting Guests", thingsInsertUri, is(not(nullValue())));

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor thingCursor = mContext.getContentResolver().query(
                EventContract.ThingEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating ThingsEntry insert.",
                thingCursor, thingValues);

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        thingValues.putAll(testEventValues);

//        TODO: Uncomment if it is going to be used
//        // Get the joined Weather and Location data with a start date
//        eventCursor = mContext.getContentResolver().query(
//                EventContract.EventEntry.buildEventWithDateUri(TestUtilities.TEST_DATE),
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Event, Guests " +
//                "and Things Data with start date.",
//                eventCursor, guestValues);
    }

    @Test
    public void updateEvent() {
        // Create a new map of values, where column names are the keys
        ContentValues eventValues = TestUtilities.createEventValues();

        Uri eventUri = mContext.getContentResolver().
                insert(EventContract.EventEntry.CONTENT_URI, eventValues);
        long eventRowId = ContentUris.parseId(eventUri);

        // Verify we got a row back.
        assertThat("Error: No row was inserted", eventRowId, is(not(-1L)));

        ContentValues updatedValues = new ContentValues(eventValues);
        updatedValues.put(EventContract.EventEntry._ID, eventRowId);
        updatedValues.put(EventContract.EventEntry.COLUMN_TITLE, "Santa's Birthday");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor eventCursor = mContext.getContentResolver().query(EventContract.EventEntry.CONTENT_URI, null, null, null, null);

        assertThat("Error: Event Cursor is null", eventCursor, is(not(nullValue())));

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        eventCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                EventContract.EventEntry.CONTENT_URI, updatedValues, EventContract.EventEntry._ID + "= ?",
                new String[] { Long.toString(eventRowId)});
        assertThat("Error: Number of rows updated does not match expected number", count, is(1));

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        eventCursor.unregisterContentObserver(tco);
        eventCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                EventContract.EventEntry.CONTENT_URI,
                null,   // projection
                EventContract.EventEntry._ID + " = " + eventRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateEvent.  Error validating event entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    @Test
    public void guestQuery() {
        ContentValues eventValues = TestUtilities.createEventValues();
        long eventRowId = TestUtilities.insertContentValues(mContext,
                EventContract.EventEntry.TABLE_NAME, eventValues);

        ContentValues guestValues = TestUtilities.createGuestValues(eventRowId);
        TestUtilities.insertContentValues(mContext,
                EventContract.GuestEntry.TABLE_NAME, guestValues);

        // Test the basic content provider query
        Cursor guestCursor = mContext.getContentResolver().query(
                EventContract.GuestEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("Error: Guest Cursor does not match the guest content values",
                guestCursor, guestValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertThat("Error: Guest Query did not properly set NotificationUri",
                    guestCursor.getNotificationUri(), is(EventContract.GuestEntry.CONTENT_URI));
        }
    }

//    @Test
//    public void guestByEventQuery() {
//        assertThat(false, is(true));
//    }

    @Test
    public void thingQuery() {
        // insert our test records into the database
        EventDBHelper dbHelper = new EventDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues eventValues = TestUtilities.createEventValues();
        long eventRowId = TestUtilities.insertContentValues(mContext,
                EventContract.EventEntry.TABLE_NAME, eventValues);

        ContentValues thingValues = TestUtilities.createThingValues(eventRowId);
        TestUtilities.insertContentValues(mContext,
                EventContract.ThingEntry.TABLE_NAME, thingValues);

        // Test the basic content provider query
        Cursor thingCursor = mContext.getContentResolver().query(
                EventContract.ThingEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("Error: Guest Cursor does not match the guest content values",
                thingCursor, thingValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertThat("Error: Guest Query did not properly set NotificationUri",
                    thingCursor.getNotificationUri(), is(EventContract.ThingEntry.CONTENT_URI));
        }
    }

//    @Test
//    public void thingByEventQuery() {
//        assertThat(false, is(true));
//    }

}