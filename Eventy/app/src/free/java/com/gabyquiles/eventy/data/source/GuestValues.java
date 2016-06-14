package com.gabyquiles.eventy.data.source;

import android.content.ContentValues;

import com.gabyquiles.eventy.data.source.local.EventContract;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.model.Guest;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class GuestValues {
    private final String LOG_TAG = GuestValues.class.getSimpleName();

    public static ContentValues from(String eventKey, Guest guest) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventContract.GuestEntry.COLUMN_EVENT_KEY, eventKey);
        contentValues.put(EventContract.GuestEntry.COLUMN_NAME, guest.getName());
        contentValues.put(EventContract.GuestEntry.COLUMN_EMAIL, guest.getEmail());
        contentValues.put(EventContract.GuestEntry.COLUMN_STATUS, guest.getStatus());

        return contentValues;
    }
}
