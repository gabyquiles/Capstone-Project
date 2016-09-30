package com.gabyquiles.eventy.model;

import android.database.Cursor;

import com.gabyquiles.eventy.data.source.local.EventContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapt Event to the free version and parse it from Cursor
 *
 * @author gabrielquiles-perez
 */
public class Event extends BaseEvent {
    private final String LOG_TAG = Event.class.getSimpleName();

    public Event() {
        super();
    }

    public Event(String title, long date, String placeName, List<BaseGuest> guests, List<String> things) {
        super(title, date, placeName, guests, things);
    }

    public Event(String key, String title, long date, String placeName, List<BaseGuest> guests, List<String> things) {
        super(key, title, date, placeName, guests, things);
    }

    public static Event from(Cursor cursor) {
        String key = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry._ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_TITLE));
        long date = cursor.getLong(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_DATE));
        String placeName = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_PLACE_NAME));
        ArrayList<BaseGuest> guests = new ArrayList<>();
        ArrayList<String> things = new ArrayList<>();
        return new Event(key, title, date, placeName, guests, things);
    }

    public String[] getGuestsEmail() {
        String[] emailArray = new String[mGuestList.size()];
        for(int i = 0; i < mGuestList.size(); i++) {
            emailArray[i] = mGuestList.get(i).getEmail();
        }
        return emailArray;
    }

    public String getThingsString() {
        StringBuilder strBuilder = new StringBuilder();
        int i = 0;
        for(String thing: mThingList) {
            strBuilder.append(thing);
            if(++i < mThingList.size()) {
                strBuilder.append(", ");
            }
        }
        return strBuilder.toString();
    }
}
