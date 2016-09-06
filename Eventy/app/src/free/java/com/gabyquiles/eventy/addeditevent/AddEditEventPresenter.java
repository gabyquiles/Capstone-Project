package com.gabyquiles.eventy.addeditevent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.gabyquiles.eventy.data.source.EventsDataSource;
import com.gabyquiles.eventy.data.source.EventsRepository;
import com.gabyquiles.eventy.data.source.LoaderProvider;
import com.gabyquiles.eventy.data.source.local.EventContract;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.model.FreeEvent;
import com.gabyquiles.eventy.model.Guest;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import javax.inject.Inject;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class AddEditEventPresenter implements AddEditEventContract.Presenter,
        LoaderManager.LoaderCallbacks<Cursor>, EventsDataSource.GetEventCallback {
    private final String LOG_TAG = AddEditEventPresenter.class.getSimpleName();

    public final static int EVENT_DETAIL_LOADER = 2;
    public final static int GUESTS_LOADER = 3;
    public final static int THINGS_LOADER = 4;

    @NonNull
    private final Context mContext;

    @NonNull
    private final AddEditEventContract.View mEventView;

    @NonNull
    private EventsRepository mRepository;

    @NonNull
    private final LoaderProvider mLoaderProvider;

    @NonNull
    private LoaderManager mLoaderManager;

    @Nullable
    private String mEventId;

    @NonNull
    private FirebaseAnalytics mAnalytics;

    private Cursor mEventCursor;
    private Cursor mGuestsCursor;
    private Cursor mThingsCursor;

    private boolean mEventLoaded = false;
    private boolean mGuestLoaded = false;
    private boolean mThingsLoaded = false;

    @Inject
    AddEditEventPresenter(@NonNull Context context, @NonNull LoaderProvider loaderProvider, @Nullable String eventId, @NonNull EventsRepository eventsRepository,
                          @NonNull AddEditEventContract.View eventsView, @NonNull FirebaseAnalytics analytics) {
        mEventId = eventId;
        mContext = checkNotNull(context);
        mRepository = checkNotNull(eventsRepository);
        mEventView = checkNotNull(eventsView);
        mLoaderProvider = checkNotNull(loaderProvider);
        mEventView.setPresenter(this);
        mAnalytics = analytics;
    }

    public void setLoaderManager(@NonNull LoaderManager manager) {
        mLoaderManager = manager;
    }

    @Override
    public void start() {
        if (!isNewEvent()) {
            populateEvent();
            logEvent("Load existing event");
        } else {
            loadNewEvent();
            logEvent("Creating new event");
        }
    }

    private void loadNewEvent() {
        Event event = new Event();
        long timestamp = event.getDate();
        mEventView.setDate(timestamp);
        mEventView.setTime(timestamp);
    }

    @Override
    public void saveEvent(String title, long date, String place, List<Guest> guests, List<String> things) {
        if (isNewEvent()) {
            createEvent(title, date, place, guests, things);
            logEvent("New event saved");
        } else {
            updateEvent(title, date, place, guests, things);
            logEvent("Event updated");
        }
    }

    @Override
    public void sendInvites() {

    }

    @Override
    public void addGuest(Guest guest) {
        mEventView.addGuest(guest);
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == AddEditEventFragment.PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Uri contactUri = data.getData();
                GetContactAsyncTask task = new GetContactAsyncTask(mContext, this);
                task.execute(contactUri);
            }
        }
    }

    @Override
    public void populateEvent() {
        if (isNewEvent()) {
            throw new RuntimeException("populateEvent() was called but event is new.");
        }
        mRepository.getEvent(mEventId, this);
        if(mLoaderManager.getLoader(EVENT_DETAIL_LOADER) == null) {
            mLoaderManager.initLoader(EVENT_DETAIL_LOADER, null, this);
            mLoaderManager.initLoader(GUESTS_LOADER, null, this);
            mLoaderManager.initLoader(THINGS_LOADER, null, this);
        } else {
            mLoaderManager.restartLoader(EVENT_DETAIL_LOADER, null, this);
            mLoaderManager.restartLoader(GUESTS_LOADER, null, this);
            mLoaderManager.restartLoader(THINGS_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == EVENT_DETAIL_LOADER) {
            mEventLoaded = false;
            return mLoaderProvider.createEventLoader(mEventId);
        } else if(id == GUESTS_LOADER) {
            mGuestLoaded = false;
            return mLoaderProvider.createGuestsLoader(mEventId);
        } else if(id == THINGS_LOADER) {
            mThingsLoaded = false;
            return  mLoaderProvider.createThingsLoader(mEventId);
        }
        return  null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == EVENT_DETAIL_LOADER) {
            mEventLoaded = true;
            if (data != null && data.moveToLast()) {
                mEventCursor = data;
            } else {
                // NO-OP, add mode.
            }
        } else if(loader.getId() == GUESTS_LOADER) {
            mGuestLoaded = true;
            if (data != null && data.moveToFirst()) {
                mGuestsCursor = data;
            }
        } else if(loader.getId() == THINGS_LOADER) {
            mThingsLoaded = true;
            if ( data != null && data.moveToFirst()) {
                mThingsCursor = data;
            }
        }
        joinCursors();
    }

    private void joinCursors() {
        if(mEventLoaded && mGuestLoaded && mThingsLoaded) {
            FreeEvent event = FreeEvent.from(mEventCursor);
            if(mGuestsCursor != null && mGuestsCursor.moveToFirst()) {
                do {
                    Guest guest = Guest.from(mGuestsCursor);
                    event.addGuest(guest);
                } while (mGuestsCursor.moveToNext());
            }
            if(mThingsCursor != null && mThingsCursor.moveToFirst()) {
                do {
                    mEventView.addThing(mThingsCursor.getString(0));
                    event.addThing(mThingsCursor.getString(mThingsCursor.getColumnIndexOrThrow(EventContract.ThingEntry.COLUMN_THING)));
                } while (mThingsCursor.moveToNext());
            }

            onEventLoaded(event);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void errorSelectingGuest() {

    }

    private boolean isNewEvent() {
        return mEventId == null;
    }

    private void createEvent(String title, long date, String place, List<Guest> guests, List<String> things) {
        FreeEvent event = new FreeEvent(title, date, place, guests, things);
        if(event.isValid()) {
            mRepository.saveEvent(event);
        }
//        TODO:
//        Task newTask = new Task(title, description);
//        if (newTask.isEmpty()) {
//            mAddTaskView.showEmptyTaskError();
//        } else {
//            mTasksRepository.saveTask(newTask);
//            mAddTaskView.showTasksList();
//        }
    }

    private void updateEvent(String title, long date, String place, List<Guest> guests, List<String> things) {
        if (isNewEvent()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }

        FreeEvent event = new FreeEvent(mEventId, title, date, place, guests, things);
        if(event.isValid()) {
            mRepository.saveEvent(event);
        }
        mEventView.showEventsList(); // After an edit, go back to the list.
    }

    @Override
    public void onEventLoaded(Event event) {
        mEventView.setTitle(event.getTitle());
        mEventView.setDate(event.getDate());
        mEventView.setTime(event.getDate());
        mEventView.setPlaceName(event.getPlaceName());
        mEventView.refreshGuests(event.getGuestList());
        mEventView.refreshThings(event.getThingList());
    }

    @Override
    public void onDataNotAvailable() {

    }



    public void logEvent(String eventDescription) {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, LOG_TAG);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, eventDescription);

        mAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
//    TODO: Send Emails
//    TODO: Delete previous events
//    TODO: Set the Adds
/**
 * It is recommended to move the Analytics creation to the Application class so that only one
 * instance is used across the project.
 */
//    TODO: Still dropping frames
}
