package com.gabyquiles.eventy.data.source;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.gabyquiles.eventy.data.source.local.EventContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Manage the loading of Events
 *
 * @author gabrielquiles-perez
 */
public class LoaderProvider {
    private final String LOG_TAG = LoaderProvider.class.getSimpleName();

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
                EventContract.EventEntry.EVENT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    public Loader<Cursor> createEventLoader(String eventId) {

        return new CursorLoader(mContext,EventContract.EventEntry.buildEventUri(Long.valueOf(eventId)),
                null,
                null,
                new String[]{eventId},
                null);
    }
}
