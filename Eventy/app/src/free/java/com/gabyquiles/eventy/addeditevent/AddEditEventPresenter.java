package com.gabyquiles.eventy.addeditevent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;
import com.gabyquiles.eventy.admob.AdHost;
import com.gabyquiles.eventy.admob.AdsManager;
import com.gabyquiles.eventy.analytics.AnalyticsManager;
import com.gabyquiles.eventy.analytics.AnalyticsManagerInterface;
import com.gabyquiles.eventy.data.source.EventsDataSource;
import com.gabyquiles.eventy.data.source.EventsRepository;
import com.gabyquiles.eventy.data.source.LoaderProvider;
import com.gabyquiles.eventy.data.source.local.EventContract;
import com.gabyquiles.eventy.model.BaseEvent;
import com.gabyquiles.eventy.model.BaseGuest;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.model.Guest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class AddEditEventPresenter implements AddEditEventContract.Presenter,
        LoaderManager.LoaderCallbacks<Cursor>, EventsDataSource.GetEventCallback, AdHost {
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
    private AnalyticsManagerInterface mAnalytics;

    @NonNull
    private AdsManager mAds;

    private Cursor mEventCursor;
    private Cursor mGuestsCursor;
    private Cursor mThingsCursor;

    private boolean mEventLoaded = false;
    private boolean mGuestLoaded = false;
    private boolean mThingsLoaded = false;

    @Inject
    AddEditEventPresenter(@NonNull Context context, @NonNull LoaderProvider loaderProvider, @Nullable String eventId, @NonNull EventsRepository eventsRepository,
                          @NonNull AddEditEventContract.View eventsView, @NonNull AnalyticsManager analytics, @NonNull AdsManager adsManager) {
        mEventId = eventId;
        mContext = checkNotNull(context);
        mRepository = checkNotNull(eventsRepository);
        mEventView = checkNotNull(eventsView);
        mLoaderProvider = checkNotNull(loaderProvider);
        mEventView.setPresenter(this);
        mAnalytics = analytics;
        mAds = adsManager;
    }

    public void setLoaderManager(@NonNull LoaderManager manager) {
        mLoaderManager = manager;
    }

    @Override
    public void start() {
        if (!isNewEvent()) {
            populateEvent();
            mAnalytics.logEvent("Load existing event");
        } else {
            loadNewEvent();
            mAnalytics.logEvent("Creating new event");
        }
//        Request new ad
        mAds.requestNewInterstitial();
    }

    private void loadNewEvent() {
        Event event = new Event();
        long timestamp = event.getDate();
        mEventView.setDate(timestamp);
        mEventView.setTime(timestamp);
    }

    @Override
    public void saveEvent(String title, long date, String place, List<BaseGuest> guests, List<String> things) {
        if (isNewEvent()) {
            createEvent(title, date, place, guests, things);
            mAnalytics.logEvent("New event saved");
        } else {
            updateEvent(title, date, place, guests, things);
            mAnalytics.logEvent("Event updated");
        }
    }

    @Override
    public void sendInvites(String title, long date, String place, List<BaseGuest> guests, List<String> things) {
        Intent sendIntent = createEmailIntent(title, date, place, guests, things);
        Intent intentChooser = Intent.createChooser(sendIntent, "Select how to send email invites");
        intentChooser.addFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intentChooser);

        mAnalytics.logEvent("Sending invites");
    }

    private Intent createEmailIntent(String title, long date, String place, List<BaseGuest> guests, List<String> things) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
        emailIntent.setType("text/plain");
        String[] emails = getGuestsEmail(guests);

        String dateStr = Utility.formatFullDate(date);
        String inviteText = mContext.getString(R.string.invite_text, title, dateStr, place);
        String requestText = mContext.getString(R.string.attribution);
        if(!things.isEmpty()) {
            String thingsStr = TextUtils.join(",", things);
            requestText = mContext.getString(R.string.things_text, thingsStr) + requestText;
        }

        emailIntent.setType("text/email");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emails);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        emailIntent.putExtra(Intent.EXTRA_TEXT, inviteText + requestText);
        return emailIntent;
    }

    private String[] getGuestsEmail(List<BaseGuest> guestList) {
        ArrayList<String> emails = new ArrayList<>();
        for (BaseGuest guest: guestList) {
            emails.add(guest.getEmail());
        }
        return emails.toArray(new String[emails.size()]);
    }

    @Override
    public void addGuest(BaseGuest guest) {
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
            Event event = Event.from(mEventCursor);
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

    private void createEvent(String title, long date, String place, List<BaseGuest> guests, List<String> things) {
        Event event = new Event(title, date, place, guests, things);
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

    private void updateEvent(String title, long date, String place, List<BaseGuest> guests, List<String> things) {
        if (isNewEvent()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }

        Event event = new Event(mEventId, title, date, place, guests, things);
        if(event.isValid()) {
            mRepository.saveEvent(event);
        }

//        Show Interstitial Ad
        mAds.showInterstitial(this);
        mEventView.showEventsList();
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

    @Override
    public void onClosedAd() {
    }
//    TODO: Delete previous events
//    TODO: Still dropping frames
}
