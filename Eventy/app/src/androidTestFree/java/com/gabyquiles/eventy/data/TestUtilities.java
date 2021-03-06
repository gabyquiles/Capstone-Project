package com.gabyquiles.eventy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import com.gabyquiles.eventy.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class TestUtilities {
    private final String LOG_TAG = TestUtilities.class.getSimpleName();

    static final long TEST_DATE = 1419033600L;  // December 20th, 2014
    static final String TEST_EVENT_TITLE = "Let's Party";
    static final String TEST_PLACE_NAME = "North Pole";
    static final String TEST_GUEST_NAME = "Magneto";
    static final String TEST_GUEST_EMAIL = "magneto@xmen.marvel";
    static final int TEST_GUEST_STATUS = 1;
    static final String TEST_THING = "chips";

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertThat("Empty cursor returned. " + error, valueCursor.moveToFirst(), is(true));
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertThat("Column '" + columnName + "' not found. " + error, idx, is(not(-1)));
            String expectedValue = entry.getValue().toString();
            assertThat("Value '" + valueCursor.getString(idx) +
                    "' did not match the expected value '" +
                    expectedValue + "' for column '" + columnName + "'. " + error,
                    valueCursor.getString(idx), is(expectedValue));
        }
    }

    static long insertContentValues(Context context, String tableName, ContentValues testValues) {
        // insert our test records into the database
        EventDBHelper dbHelper = new EventDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long rowId;
        rowId = db.insert(tableName, null, testValues);

        // Verify we got a row back.
        assertThat("Error: Failure to inser an event", rowId, is(not(-1L)));

        return rowId;
    }

    static ContentValues createEventValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(EventContract.EventEntry.COLUMN_PLACE_NAME, TEST_PLACE_NAME);
        testValues.put(EventContract.EventEntry.COLUMN_TITLE, TEST_EVENT_TITLE);
        testValues.put(EventContract.EventEntry.COLUMN_DATE, TEST_DATE);
        return testValues;
    }

    static ContentValues createGuestValues(long event_id) {
        ContentValues testValues = new ContentValues();
        testValues.put(EventContract.GuestEntry.COLUMN_EVENT_KEY, event_id);
        testValues.put(EventContract.GuestEntry.COLUMN_EMAIL, TEST_GUEST_EMAIL);
        testValues.put(EventContract.GuestEntry.COLUMN_NAME, TEST_GUEST_NAME);
        testValues.put(EventContract.GuestEntry.COLUMN_STATUS, TEST_GUEST_STATUS);
        return testValues;
    }

    static ContentValues createThingValues(long event_id) {
        ContentValues testValues = new ContentValues();
        testValues.put(EventContract.ThingEntry.COLUMN_EVENT_KEY, event_id);
        testValues.put(EventContract.ThingEntry.COLUMN_THING, TEST_THING);
        return testValues;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
