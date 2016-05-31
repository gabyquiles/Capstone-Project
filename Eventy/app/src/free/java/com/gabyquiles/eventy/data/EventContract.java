package com.gabyquiles.eventy.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines tables and columns for the envents database
 *
 * @author gabrielquiles-perez
 */
public class EventContract {
    private final String LOG_TAG = EventContract.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "com.gabyquiles.eventy.free";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EVENT = "events";
    public static final String PATH_GUESTS = "guests";
    public static final String PATH_THINGS = "things";


    public static long getIdFromUri(Uri uri) {
        try {
            long id = Long.valueOf(uri.getLastPathSegment());
            return id;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static final class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "events";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_PLACE_NAME = "place_name";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;

        //content://com.gabyquiles.eventy.free/events/1
        public static Uri buildEventUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //content://com.gabyquiles.eventy.free/events?date=123
        public static Uri buildEventWithDateUri(long date) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_DATE, Long.toString(date)).build();
        }

        public static long getStartDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_DATE);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }
    }

    public static final class GuestEntry implements BaseColumns {
        public static final String TABLE_NAME = "guests";

        //Foreign key to event entry
        public static final String COLUMN_EVENT_KEY = "event_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_THING = "thing";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GUESTS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GUESTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GUESTS;

        //content://com.gabyquiles.eventy.free/guests/1
        public static Uri buildGuestUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //content://com.gabyquiles.eventy.free/guests?event=1
        public static Uri buildEventGuestsUri(long event_id) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_EVENT_KEY, Long.toString(event_id)).build();
        }
    }

    public static final class ThingEntry implements BaseColumns {
        public static final String TABLE_NAME = "things";

        public static final String COLUMN_EVENT_KEY = "event_id";
        public static final String COLUMN_THING = "thing";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_THINGS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THINGS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THINGS;

        //content://com.gabyquiles.eventy.free/things/1
        public static Uri buildThingUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //content://com.gabyquiles.eventy.free/things?event=1
        public static Uri buildEventThingsUri(long event_id) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_EVENT_KEY, Long.toString(event_id)).build();
        }
    }
}
