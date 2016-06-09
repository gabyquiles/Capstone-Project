package com.gabyquiles.eventy.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Manage the loading of Events
 *
 * @author gabrielquiles-perez
 */
public class LoaderProvider {
    private final String LOG_TAG = LoaderProvider.class.getSimpleName();

//    TODO: Maybe retrieve all columns???
    private static final String[] EVENT_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            EventContract.EventEntry.TABLE_NAME + "." + EventContract.EventEntry._ID,
            EventContract.EventEntry.COLUMN_TITLE,
            EventContract.EventEntry.COLUMN_DATE,
            EventContract.EventEntry.COLUMN_PLACE_NAME
    };

    // These indices are tied to EVENT_COLUMNS.  If EVENT_COLUMNS changes, these
    // must change.
    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_TITLE = 1;
    static final int COL_EVENT_DATE = 2;
    static final int COL_EVENT_PLACE = 3;

    @NonNull
    private final Context mContext;

    public LoaderProvider(@NonNull Context context) {
        mContext = checkNotNull(context, "context cannot be null");
    }

    public Loader<Cursor> createEventsLoader() {
        String sortOrder = EventContract.EventEntry.COLUMN_DATE + " ASC";

        Uri eventUri = EventContract.EventEntry.buildEventWithDateUri(System.currentTimeMillis());

        return new CursorLoader(mContext,
                eventUri,
                EVENT_COLUMNS,
                null,
                null,
                sortOrder);
    }

//    public Loader<Cursor> createEventLoader(String eventId) {
//        return new CursorLoader(mContext, )
//    }
}
