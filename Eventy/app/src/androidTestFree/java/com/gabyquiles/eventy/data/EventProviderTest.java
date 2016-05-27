package com.gabyquiles.eventy.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
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
        assertThat("Error: the EventEntry CONTENT_URI with id should return EventEnty.CONTENT_ITEM_TYPE", type,
                is(EventContract.EventEntry.CONTENT_ITEM_TYPE));

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
        // insert our test records into the database
        EventDBHelper dbHelper = new EventDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues eventValues = TestUtilities.createEventValues();
        long eventRowId = TestUtilities.insertContentValues(mContext,
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
    public void eventByDateQuery() {
        assertThat(false, is(true));
    }

    @Test
    public void insertReadEvent() {
        assertThat(false, is(true));
    }

    @Test
    public void updateEvent() {
        assertThat(false, is(true));
    }

    @Test
    public void guestQuery() {
        // insert our test records into the database
        EventDBHelper dbHelper = new EventDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

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

    @Test
    public void guestByEventQuery() {
        assertThat(false, is(true));
    }

    @Test
    public void insertReadGuest() {
        assertThat(false, is(true));
    }

    @Test
    public void thingQuery() {
        assertThat(false, is(true));
    }

    @Test
    public void thingByEventQuery() {
        assertThat(false, is(true));
    }

    @Test
    public void insertReadThing() {
        assertThat(false, is(true));
    }
}