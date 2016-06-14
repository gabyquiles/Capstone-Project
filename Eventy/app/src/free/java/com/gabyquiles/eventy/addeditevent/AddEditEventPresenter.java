package com.gabyquiles.eventy.addeditevent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.gabyquiles.eventy.data.source.EventsDataSource;
import com.gabyquiles.eventy.data.source.EventsRepository;
import com.gabyquiles.eventy.data.source.LoaderProvider;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.model.FreeEvent;
import com.gabyquiles.eventy.model.Guest;

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

    @NonNull
    private final Context mContext;

    @NonNull
    private final AddEditEventContract.View mEventView;

    @NonNull
    private EventsRepository mRepository;

    @NonNull
    private final LoaderProvider mLoaderProvider;

    @NonNull
    private final LoaderManager mLoaderManager;

    @Nullable
    private String mEventId;

    private Event mEvent;

    @Inject
    AddEditEventPresenter(@NonNull Context context, @NonNull LoaderProvider loaderProvider, @NonNull LoaderManager manager, @Nullable String eventId, @NonNull EventsRepository eventsRepository,
                          @NonNull AddEditEventContract.View eventsView) {
        mEventId = eventId;
        mContext = checkNotNull(context);
        mRepository = checkNotNull(eventsRepository);
        mEventView = checkNotNull(eventsView);
        mLoaderProvider = checkNotNull(loaderProvider);
        mLoaderManager = checkNotNull(manager);
        mEventView.setPresenter(this);
        mEvent = new Event();
    }

    @Override
    public void start() {
        if (!isNewEvent()) {
            populateEvent();
        }
    }

    @Override
    public void saveEvent(String title, long date, String place, List<Guest> guests, List<String> things) {
        if (isNewEvent()) {
            createEvent(title, date, place, guests, things);
        } else {
            updateEvent(title, date, place, guests, things);
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
            throw new RuntimeException("populateTask() was called but task is new.");
        }
        mRepository.getEvent(mEventId, this);
        if(mLoaderManager.getLoader(EVENT_DETAIL_LOADER) == null) {
            mLoaderManager.initLoader(EVENT_DETAIL_LOADER, null, this);
        } else {
            mLoaderManager.restartLoader(EVENT_DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == EVENT_DETAIL_LOADER) {
            return mLoaderProvider.createEventLoader(mEventId);
        } else if(id == GUESTS_LOADER) {
            return mLoaderProvider.createGuestsLoader(mEventId);
        }
        return  null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == EVENT_DETAIL_LOADER) {
            if (data != null && data.moveToLast()) {
                FreeEvent event = FreeEvent.from(data);
                onEventLoaded(event);
                mLoaderManager.initLoader(GUESTS_LOADER, null, this);
            } else {
                // NO-OP, add mode.
            }
        } else if(loader.getId() == GUESTS_LOADER) {
            if (data != null && data.moveToFirst()) {
                do {
                    Guest guest = Guest.from(data);
                    mEventView.addGuest(guest);
                } while (data.moveToNext());
            }
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
//        TODO
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

}
