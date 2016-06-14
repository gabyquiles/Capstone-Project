package com.gabyquiles.eventy.data.source;

import android.content.ContentValues;

import com.gabyquiles.eventy.data.source.local.EventContract;
import com.gabyquiles.eventy.model.Event;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class EventValues {
    private final String LOG_TAG = EventValues.class.getSimpleName();

    public static ContentValues from(Event event) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventContract.EventEntry.COLUMN_TITLE, event.getTitle());
        contentValues.put(EventContract.EventEntry.COLUMN_PLACE_NAME, event.getPlaceName());
        contentValues.put(EventContract.EventEntry.COLUMN_DATE, event.getDate());

        return contentValues;
    }
}
