package com.gabyquiles.eventy.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

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
    static final int EVENTS = 101;
    static final int EVENT_WITH_DATE = 102;
    static final int GUEST = 103;
    static final int GUESTS = 104;
    static final int GUESTS_BY_EVENT = 105;
    static final int THING = 106;
    static final int THINGS = 107;
    static final int THINGS_BY_EVENT = 108;

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
            case EVENTS:
            case EVENT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        EventContract.EventEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case GUEST:
            case GUESTS: {
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
            // Student: Uncomment and fill out these two cases
            case EVENT:
                return EventContract.EventEntry.CONTENT_ITEM_TYPE;
            case EVENTS:
                return EventContract.EventEntry.CONTENT_TYPE;
            case GUEST:
                return EventContract.GuestEntry.CONTENT_ITEM_TYPE;
            case GUESTS:
                return EventContract.GuestEntry.CONTENT_TYPE;
            case THING:
                return EventContract.ThingEntry.CONTENT_ITEM_TYPE;
            case THINGS:
                return EventContract.ThingEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_EVENT, EVENTS);
        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_EVENT+"/#", EVENT);
        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_GUESTS, GUESTS);
        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_GUESTS+"/#", GUEST);
        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_THINGS, THINGS);
        matcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_THINGS+"/#", THING);

        return matcher;
    }
}
