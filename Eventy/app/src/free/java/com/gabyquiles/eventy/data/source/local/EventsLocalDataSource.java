package com.gabyquiles.eventy.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.gabyquiles.eventy.data.source.EventValues;
import com.gabyquiles.eventy.data.source.EventsDataSource;
import com.gabyquiles.eventy.data.source.GuestValues;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.model.FreeEvent;
import com.gabyquiles.eventy.model.Guest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
@Singleton
public class EventsLocalDataSource implements EventsDataSource {
    private final String LOG_TAG = EventsLocalDataSource.class.getSimpleName();

    private ContentResolver mResolver;

    public EventsLocalDataSource(@NonNull ContentResolver contentResolver) {
        checkNotNull(contentResolver);
        mResolver = contentResolver;
    }

    @Override
    public void getEvents(@NonNull GetEventsCallback callback) {
        // no-op since the data is loader via Cursor Loader
    }

    @Override
    public void getEvent(@NonNull String taskId, @NonNull GetEventCallback callback) {
        // no-op since the data is loader via Cursor Loader
    }

    @Override
    public void saveEvent(@NonNull Event event) {
        checkNotNull(event);

        ContentValues values = EventValues.from(event);
        Uri insertedUri = mResolver.insert(EventContract.EventEntry.CONTENT_URI, values);
        Long id = Long.parseLong(insertedUri.getLastPathSegment());
        saveGuests(id, event.getGuestList());
        saveThings(id, event.getThingList());
    }

    @Override
    public void updateEvent(@NonNull Event event) {
        checkNotNull(event);

        ContentValues values = EventValues.from(event);

        Long id = Long.valueOf(event.getKey());
        Uri updateUri = EventContract.EventEntry.buildEventUri(id);
        mResolver.update(updateUri, values, null, null);
        saveGuests(id, event.getGuestList());
        saveThings(id, event.getThingList());
    }

    public void saveGuest(Long eventKey, Guest guest) {
        ContentValues contentValues = GuestValues.from(eventKey.toString(), guest);
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
