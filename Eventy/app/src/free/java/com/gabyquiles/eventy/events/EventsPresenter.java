package com.gabyquiles.eventy.events;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.gabyquiles.eventy.data.LoaderProvider;
import com.gabyquiles.eventy.model.Event;

import javax.inject.Inject;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link EventsFragment}), retrieves the data and updates the
 * UI as required. It is implemented as a non UI {@link Fragment} to make use of the
 * {@link LoaderManager} mechanism for managing loading and updating data asynchronously.
 */
public class EventsPresenter implements EventsContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = EventsPresenter.class.getSimpleName();

    public final static int EVENTS_LOADER = 1;

    private final EventsContract.View mEventsView;

    private final LoaderProvider mLoaderProvider;

    private LoaderManager mLoaderManager;

    @Inject
    public EventsPresenter(@NonNull LoaderProvider provider, @NonNull LoaderManager manager, @NonNull EventsContract.View eventsView) {
        mLoaderProvider = provider;
        mLoaderManager = manager;
        mEventsView = eventsView;
        mEventsView.setPresenter(this);
    }

    @Inject
    void setupListeners() {
        mEventsView.setPresenter(this);
    }

    public void setLoaderManager(@NonNull LoaderManager manager) {
        mLoaderManager = manager;
    }

    @Override
    public void start() {
        loadEvents();
    }

    public void loadEvents() {

        if(mLoaderManager.getLoader(EVENTS_LOADER) == null) {
            mLoaderManager.initLoader(EVENTS_LOADER, null, this);
        } else {
            mLoaderManager.restartLoader(EVENTS_LOADER, null, this);
        }
    }

    @Override
    public void addNewEvent() {
        mEventsView.showAddEvent();
    }

    @Override
    public void openEventDetails(@NonNull Event event) {
        checkNotNull(event, "event cannot be null!");
        mEventsView.showEventDetails(event.getKey());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mLoaderProvider.createEventsLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            if(data.moveToLast()) {
                onDataLoaded(data);
            } else {
                onDataEmpty();
            }
        } else {
            onDataNotAvailable();
        }

    }

    private void onDataNotAvailable() {

    }

    private void onDataEmpty() {
    }

    private void onDataLoaded(Cursor data) {
        mEventsView.showEvents(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
