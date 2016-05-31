package com.gabyquiles.eventy.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.model.Guest;

import java.util.List;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class EventManager {
    private final String LOG_TAG = EventManager.class.getSimpleName();

    private ContentResolver mResolver;

    public EventManager(Context context) {
        mResolver = context.getContentResolver();
    }

    public Event saveEvent(Uri uri, Event event) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventContract.EventEntry.COLUMN_TITLE, event.getTitle());
        contentValues.put(EventContract.EventEntry.COLUMN_PLACE_NAME, event.getPlaceName());
        contentValues.put(EventContract.EventEntry.COLUMN_DATE, event.getDate());

        if(uri == null) {
            Uri insertedUri = mResolver.insert(EventContract.EventEntry.CONTENT_URI, contentValues);
            Long id = Long.parseLong(insertedUri.getLastPathSegment());
            event.setKey(id.toString());
        } else {
            Long id = EventContract.getIdFromUri(uri);
            mResolver.update(uri, contentValues, null, null);
        }

        saveGuests(Long.valueOf(event.getKey()), event.getGuestList());
        saveThings(Long.valueOf(event.getKey()), event.getThingList());

        return event;
    }

    public void saveGuest(Long eventKey, Guest guest) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventContract.GuestEntry.COLUMN_EVENT_KEY, eventKey);
        contentValues.put(EventContract.GuestEntry.COLUMN_NAME, guest.getName());
        contentValues.put(EventContract.GuestEntry.COLUMN_EMAIL, guest.getEmail());
        contentValues.put(EventContract.GuestEntry.COLUMN_STATUS, guest.getStatus());
        mResolver.insert(EventContract.GuestEntry.CONTENT_URI, contentValues);
    }

    public void saveGuests(Long eventKey, List<Guest> guestList) {
        mResolver.delete(EventContract.GuestEntry.buildEventGuestsUri(eventKey), null, null);
        for(Guest guest:guestList) {
            if (guest != null) {
                saveGuest(eventKey, guest);
            }
        }
    }

    public void saveThing(Long eventKey, String thing) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventContract.ThingEntry.COLUMN_EVENT_KEY, eventKey);
        contentValues.put(EventContract.ThingEntry.COLUMN_THING, thing);
        mResolver.insert(EventContract.ThingEntry.CONTENT_URI, contentValues);
    }

    public void saveThings(Long eventKey, List<String> thingList) {
        mResolver.delete(EventContract.ThingEntry.buildEventThingsUri(eventKey), null, null);
        for(String thing:thingList) {
            if (thing != null) {
                saveThing(eventKey, thing);
            }
        }
    }
}
