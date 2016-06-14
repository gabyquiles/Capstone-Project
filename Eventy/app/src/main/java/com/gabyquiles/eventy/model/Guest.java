package com.gabyquiles.eventy.model;

import android.database.Cursor;

import com.gabyquiles.eventy.data.source.local.EventContract;

import java.util.ArrayList;

/**
 * Represents an invited guest
 *
 * @author gabrielquiles-perez
 */
public class Guest {
    private final String LOG_TAG = Guest.class.getSimpleName();

    public static int INVITED = 1;
    public static int GOING = 2;
    public static int MAYBE = 3;
    public static int NOT_GOING = 4;

    private String mName;
    private String mEmail;
    private int mStatus;
    private String mThing;

    public Guest() {
        mStatus = INVITED;
        mThing = null;
    }

    public Guest(String name, String email, String thing, int status) {
        mName = name;
        mEmail = email;
        mThing = thing;
        mStatus = status;
    }

    public Guest(String name, String email) {
        mName = name;
        mEmail = email;
        mStatus = INVITED;
        mThing = null;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public String getThing() {
        return mThing;
    }

    public void setThing(String thing) {
        this.mThing = thing;
    }

    public static Guest from(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.GuestEntry.COLUMN_NAME));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.GuestEntry.COLUMN_EMAIL));
        String thing = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.GuestEntry.COLUMN_THING));
        int status = cursor.getInt(cursor.getColumnIndexOrThrow(EventContract.GuestEntry.COLUMN_STATUS));
        return new Guest(name, email, thing, status);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(!(o instanceof Guest)) {
            return false;
        }

        Guest guest = (Guest) o;
        if(!mName.equals(guest.mName)) {
            return false;
        }
        if(!mEmail.equals(guest.mEmail)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + mEmail.hashCode();
        return result;
    }
}
