package com.gabyquiles.eventy.events;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.gabyquiles.eventy.R;
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
    public void menuSelected(int itemId) {
        //Logout from the app
        if (itemId == R.id.action_logout) {
            mAuthManager.signOut();
            mEventsView.showLoginActivity();
        }
    }

    @Override
    public void start() {
        if(!mAuthManager.isSignedIn()) {
            mEventsView.showLoginActivity();
        } else {
            mAnalytics.logEvent("Event List");

            try {
                mAdapter = new EventsAdapter(mDBManager.getEventsList(), this);
                mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        listModified(itemCount);
                    }

                    @Override
                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                        super.onItemRangeRemoved(positionStart, itemCount);
                        listModified(itemCount);
                    }
                });
                mEventsView.setAdapter(mAdapter);
            } catch (Exception e) {
//                TODO: Fix to real exception
            }
        }
    }

    @Override
    public void onEventClick(String key) {
        mEventsView.showEventDetails(key);
    }

    @Override
    public void onDeleteEvent(BaseEvent deleteEvent) {

    }

    private void listModified(int count) {
        if (count > 0) {
            mEventsView.showEvents();
        } else {
            mEventsView.showNoEvents();
        }
    }

//    TODO: Add Swipe Refresh
//    TODO: Add loading when sign in
}
