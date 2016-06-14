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

//    private EventDBHelper mDbHelper;
    private ContentResolver mResolver;

    public EventsLocalDataSource(@NonNull ContentResolver contentResolver) {
        checkNotNull(contentResolver);
//        mDbHelper = new EventDBHelper(context);
        mResolver = contentResolver;
    }

    @Override
    public void getEvents(@NonNull GetEventsCallback callback) {
//        List<Event> events = new ArrayList<>();
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        Cursor c = db.query(
//                EventContract.EventEntry.TABLE_NAME, EventContract.EventEntry.EVENT_COLUMNS, null, null, null, null, null);
//
//        if (c != null && c.getCount() > 0) {
//            while (c.moveToNext()) {
//                FreeEvent event = FreeEvent.from(c);
//                events.add(event);
//            }
//        }
//        if (c != null) {
//            c.close();
//        }
//
//        db.close();
//
//        if (events.isEmpty()) {
//            // This will be called if the table is new or just empty.
//            callback.onDataNotAvailable();
//        } else {
//            callback.onEventsLoaded(events);
//        }
        // no-op since the data is loader via Cursor Loader
    }

    @Override
    public void getEvent(@NonNull String taskId, @NonNull GetEventCallback callback) {
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        String selection = EventContract.EventEntry._ID + " LIKE ?";
//        String[] selectionArgs = { taskId };
//
//        Cursor c = db.query(
//                EventContract.EventEntry.TABLE_NAME, EventContract.EventEntry.EVENT_COLUMNS, selection, selectionArgs, null, null, null);
//
//        Event event = null;
//
//        if (c != null && c.getCount() > 0) {
//            c.moveToFirst();
//            event = FreeEvent.from(c);
//        }
//        if (c != null) {
//            c.close();
//        }
//
//        db.close();
//
//        if (event != null) {
//            callback.onEventLoaded(event);
//        } else {
//            callback.onDataNotAvailable();
//        }
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
