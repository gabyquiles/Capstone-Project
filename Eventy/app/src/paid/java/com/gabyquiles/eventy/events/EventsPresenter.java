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
//        loadEvents(true);
        mAnalytics.logEvent("Event List");

        mAdapter = new EventsAdapter(mDBManager.getEventsList(), this);
//        public EventsAdapter(Query ref, View emptyView, RecyclerViewAdapterFactory.AdapterOnClickHandler clickHandler) {
        mEventsView.setAdapter(mAdapter);
//        mEmptyView.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
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
