package com.gabyquiles.eventy.events;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.AbsListView;

import com.gabyquiles.eventy.analytics.AnalyticsManager;
import com.gabyquiles.eventy.analytics.AnalyticsManagerInterface;
import com.gabyquiles.eventy.data.source.LoaderProvider;
import com.gabyquiles.eventy.data.source.EventsDataSource;
import com.gabyquiles.eventy.data.source.EventsRepository;
import com.gabyquiles.eventy.model.BaseEvent;
import com.gabyquiles.eventy.model.Event;

import java.util.List;

import javax.inject.Inject;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link EventsFragment}), retrieves the data and updates the
 * UI as required. It is implemented as a non UI {@link Fragment} to make use of the
 * {@link LoaderManager} mechanism for managing loading and updating data asynchronously.
 */
public class EventsPresenter implements EventsContract.Presenter, LoaderManager.LoaderCallbacks<Cursor>, EventsDataSource.GetEventsCallback {
    private final String LOG_TAG = EventsPresenter.class.getSimpleName();

    public final static int EVENTS_LOADER = 1;

    private final EventsContract.View mEventsView;

    private final LoaderProvider mLoaderProvider;

    private LoaderManager mLoaderManager;

    private EventsRepository mRepository;

    private AnalyticsManagerInterface mAnalytics;

    private EventsAdapter mAdapter;

    /**
     * Listener for clicks on tasks in the ListView.
     */
    EventsContract.EventItemListener mItemListener = new EventsContract.EventItemListener() {
        @Override
        public void onEventClick(BaseEvent clickedEvent) {
            openEventDetails(clickedEvent);
        }

        @Override
        public void onDeleteEvent(BaseEvent deleteEvent) {
            deleteEvent(deleteEvent);
        }
    };

    @Inject
    public EventsPresenter(@NonNull LoaderProvider provider, @NonNull EventsRepository eventsRepository, @NonNull EventsContract.View eventsView, @NonNull AnalyticsManager analytics) {
        mLoaderProvider = checkNotNull(provider, "loaderProvider can not be null");
        mEventsView = checkNotNull(eventsView, "eventsView can not be null");
        mRepository = checkNotNull(eventsRepository, "eventsRepository can not be null");
        mEventsView.setPresenter(this);
        mAnalytics = analytics;
    }

    @Inject
    void setupListeners() {
        mEventsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadEvents(true);

        mAnalytics.logEvent("Event List");

        mAdapter = new EventsAdapter(((Fragment) mEventsView).getActivity(), mItemListener, AbsListView.CHOICE_MODE_NONE);
        mEventsView.setAdapter(mAdapter);
        loadManager();
    }

    private void loadManager() {
        if (mLoaderManager == null) {
            mLoaderManager = ((Fragment) mEventsView).getLoaderManager();
        }
    }

    public void loadEvents(boolean forceUpdate) {
        loadManager();
        if (forceUpdate) {
            mRepository.getEvents(this);
        }

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
    public void openEventDetails(@NonNull BaseEvent event) {
        checkNotNull(event, "event cannot be null!");
        mEventsView.showEventDetails(event.getKey());
    }

    @Override
    public void deleteEvent(@NonNull BaseEvent event) {
        checkNotNull(event, "event cannot be null!");
        mRepository.deleteEvent((Event) event);
        loadEvents(true);
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

    @Override
    public void onEventsLoaded(List<Event> tasks) {

    }

    @Override
    public void onDataNotAvailable() {
//        TODO: Show error
    }

    private void onDataEmpty() {
        mEventsView.showNoEvents();
    }

    private void onDataLoaded(Cursor data) {
        mAdapter.swapCursor(data);
        mEventsView.showEvents(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
