package com.gabyquiles.eventy.model;

import android.database.Cursor;

import com.gabyquiles.eventy.data.source.local.EventContract;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class Guest extends BaseGuest {
    private final String LOG_TAG = Guest.class.getSimpleName();

    public Guest(String name, String email) {
        super(name, email);
    }

    public Guest(String name, String email, String thing, int status) {
        super(name, email, thing, status);
    }

    public static Guest from(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.GuestEntry.COLUMN_NAME));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.GuestEntry.COLUMN_EMAIL));
        String thing = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.GuestEntry.COLUMN_THING));
        int status = cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.GuestEntry.COLUMN_STATUS));
        return new Guest(name, email, thing, status);
    }
}
