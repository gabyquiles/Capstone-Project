package com.gabyquiles.eventy.addeditevent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gabyquiles.eventy.analytics.AnalyticsManager;
import com.gabyquiles.eventy.analytics.AnalyticsManagerInterface;
import com.gabyquiles.eventy.firebase.authentication.AuthenticationManager;
import com.gabyquiles.eventy.firebase.database.DatabaseManager;
import com.gabyquiles.eventy.model.BaseGuest;
import com.gabyquiles.eventy.model.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import javax.inject.Inject;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class AddEditEventPresenter implements AddEditEventContract.Presenter {
    private final String LOG_TAG = AddEditEventPresenter.class.getSimpleName();

    private final AddEditEventContract.View mEventView;

    private AnalyticsManagerInterface mAnalytics;
    private AuthenticationManager mAuthManager;
    private DatabaseManager mDatabaseManager;
    private Event mEvent;

    @Inject
    public AddEditEventPresenter(@NonNull AddEditEventContract.View eventView,
                                 @NonNull AnalyticsManager analytics,
                                 @NonNull AuthenticationManager auth,
                                 @NonNull DatabaseManager db, @Nullable String eventKey) {
        mEventView = checkNotNull(eventView, "eventsView can not be null");
        mEventView.setPresenter(this);
        mAnalytics = analytics;
        mAuthManager = auth;
        mDatabaseManager = db;

        if (eventKey != null) {
            mDatabaseManager.getEvent(eventKey, new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mEvent = dataSnapshot.getValue(Event.class);
                    populateEvent();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void saveEvent(String title, long date, String place, List<BaseGuest> guests, List<String> things) {

    }

    @Override
    public void sendInvites(String title, long date, String place, List<BaseGuest> guests, List<String> things) {

    }

    @Override
    public void addGuest(BaseGuest guest) {

    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void populateEvent() {
        mEventView.setTitle(mEvent.getTitle());
        mEventView.setDate(mEvent.getDate());
        mEventView.setTime(mEvent.getDate());
        mEventView.setPlaceName(mEvent.getPlaceName());
        mEventView.refreshGuests(mEvent.getGuestList());
        mEventView.refreshThings(mEvent.getThingList());
    }

    @Override
    public void errorSelectingGuest() {

    }

    @Override
    public void start() {

    }
}
