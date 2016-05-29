package com.gabyquiles.eventy.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.gabyquiles.eventy.model.Event;

/**
 * Manages access to Event Data
 *
 * @author gabrielquiles-perez
 */
public class EventProvider extends ContentProvider {
    private final String LOG_TAG = EventProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private EventDBHelper mOpenHelper;

    static final int EVENT = 100;
    static final int GUEST = 103;
    static final int THING = 106;

    private static final SQLiteQueryBuilder sEventWithGuestsAndThingsQueryBuilder;

    // This initializes the SQLiteQueryBuilder. This is the table section, what goes between the
    // from and the where.
    static{
        sEventWithGuestsAndThingsQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //event INNER JOIN guests ON weather.location_id = location._id
        sEventWithGuestsAndThingsQueryBuilder.setTables(
                EventContract.GuestEntry.TABLE_NAME + " INNER JOIN " +
                        EventContract.EventEntry.TABLE_NAME +
                        " ON " + EventContract.GuestEntry.TABLE_NAME +
                        "." + EventContract.GuestEntry.COLUMN_EVENT_KEY +
                        " = " + EventContract.EventEntry.TABLE_NAME +
                        "." + EventContract.EventEntry._ID);
    }

    //event.date = ?
    private static final String sDateSettingSelection =
            EventContract.EventEntry.TABLE_NAME+
                    "." + EventContract.EventEntry.COLUMN_DATE + " = ? ";

    @Override
    public boolean onCreate() {
        mOpenHelper = new EventDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
//            // "weather/*/*"
//            case WEATHER_WITH_LOCATION_AND_DATE:
//            {
//                retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
//                break;
//            }
//            // "weather/*"
//            case WEATHER_WITH_LOCATION: {
//                retCursor = getWeatherByLocationSetting(uri, projection, sortOrder);
//                break;
//            }
//            // "weather"
//            case WEATHER: {
//                retCursor = mOpenHelper.getReadableDatabase().query(
//                        WeatherContract.WeatherEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
//                break;
//            }
            case EVENT: {
//                Extract this into function and add for case including date
                retCursor = getEvent(uri, projection, sortOrder);
                break;
            }
            case GUEST: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        EventContract.GuestEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case THING: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        EventContract.ThingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case EVENT:
                return EventContract.EventEntry.CONTENT_TYPE;
            case GUEST:
                return EventContract.GuestEntry.CONTENT_TYPE;
            case THING:
                return EventContract.ThingEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case EVENT: {
                normalizeDate(values);
                long _id = db.insert(EventContract.EventEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = EventContract.EventEntry.buildEventUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case GUEST: {
                long _id = db.insert(EventContract.GuestEntry.TABLE_NAME, null, values);
                if ( _id > 0 ) {
                    returnUri = EventContract.GuestEntry.buildGuestUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case THING: {
                long _id = db.insert(EventContract.ThingEntry.TABLE_NAME, null, values);
                if ( _id > 0 ) {
                    returnUri = EventContract.ThingEntry.buildThingUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int updatedRows = 0;
        switch (match) {
            case EVENT: {
                updatedRows = db.update(EventContract.EventEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case GUEST: {
                updatedRows = db.update(EventContract.GuestEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case THING: {
                updatedRows = db.update(EventContract.ThingEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        // Student: This is a lot like the delete function.  We return the number of rows impacted
        // by the update.
        return updatedRows;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GUEST:
                db.beginTransaction();
                int guestCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(EventContract.GuestEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            guestCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return guestCount;
            case THING:
                db.beginTransaction();
                int thingCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(EventContract.ThingEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            thingCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return thingCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

    private Cursor getEvent(Uri uri, String[] projection, String sortOrder) {
        long startDate = EventContract.EventEntry.getStartDateFromUri(uri);

        String[] selectionArgs = new String[]{};
        String selection = "";

//        if (startDate != 0) {
//            selectionArgs = new String[]{Long.toString(startDate)};
//            selection = sDateSettingSelection;
//        }

//        return sEventWithGuestsAndThingsQueryBuilder.query(mOpenHelper.getReadableDatabase(),
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                sortOrder
//        );

        return mOpenHelper.getReadableDatabase().query(
                EventContract.EventEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_EVENT, EVENT);
        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_EVENT+"/#", EVENT);
        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_GUESTS, GUEST);
        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_GUESTS+"/#", GUEST);
        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_THINGS, THING);
        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_THINGS+"/#", THING);

        return matcher;
    }

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(EventContract.EventEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(EventContract.EventEntry.COLUMN_DATE);
            values.put(EventContract.EventEntry.COLUMN_DATE, EventContract.normalizeDate(dateValue));
        }
    }
}
