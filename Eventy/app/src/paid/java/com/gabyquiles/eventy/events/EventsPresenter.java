package com.gabyquiles.eventy.events;

import android.support.annotation.NonNull;

import com.gabyquiles.eventy.analytics.AnalyticsManager;
import com.gabyquiles.eventy.analytics.AnalyticsManagerInterface;
import com.gabyquiles.eventy.firebase.authentication.AuthenticationManager;
import com.gabyquiles.eventy.firebase.database.DatabaseManager;
import com.gabyquiles.eventy.model.BaseEvent;

import javax.inject.Inject;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class EventsPresenter implements EventsContract.Presenter, EventsContract.EventItemListener {
    private final String LOG_TAG = EventsPresenter.class.getSimpleName();

    private final EventsContract.View mEventsView;

    private AnalyticsManagerInterface mAnalytics;

    private AuthenticationManager mAuthManager;

    private DatabaseManager mDBManager;

    private EventsAdapter mAdapter;

    @Inject
    public EventsPresenter(@NonNull EventsContract.View eventsView,
                           @NonNull AnalyticsManager analytics, @NonNull AuthenticationManager auth,
                           @NonNull DatabaseManager db) {
        mEventsView = checkNotNull(eventsView, "eventsView can not be null");
        mEventsView.setPresenter(this);
        mAnalytics = analytics;
        mAuthManager = auth;
        mDBManager = db;
    }


    @Inject
    void setupListeners() {
        mEventsView.setPresenter(this);
    }

    @Override
    public void addNewEvent() {

    }

    @Override
    public void openEventDetails(@NonNull BaseEvent event) {

    }

    @Override
    public void deleteEvent(@NonNull BaseEvent event) {

    }

    @Override
    public void start() {
        showLoginForm();
        mAnalytics.logEvent("Event List");

        mAdapter = new EventsAdapter(mDBManager.getEventsList(), this, new EventsAdapter.LoaderSubscriber() {
            @Override
            public void loadedEvents(int eventsCount) {
                if(eventsCount > 0) {
                    mEventsView.showEvents();
                } else {
                    mEventsView.showNoEvents();
                }
            }
        });
        mEventsView.setAdapter(mAdapter);
    }

    protected void showLoginForm() {
        if(!mAuthManager.isSignedIn()) {
            mEventsView.showLoginActivity();
        }
    }

    @Override
    public void onEventClick(BaseEvent clickedEvent) {

    }

    @Override
    public void onDeleteEvent(BaseEvent deleteEvent) {

    }
}
