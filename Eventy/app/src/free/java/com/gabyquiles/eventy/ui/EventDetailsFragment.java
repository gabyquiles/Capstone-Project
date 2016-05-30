package com.gabyquiles.eventy.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;
import com.gabyquiles.eventy.data.EventContract;
import com.gabyquiles.eventy.model.Event;
import com.gabyquiles.eventy.model.Guest;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * Fragment that shows the details of an event
 *
 * @author gabrielquiles-perez
 */
public class EventDetailsFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = EventDetailsFragment.class.getSimpleName();

    static final String EVENT_URI = "event_uri";
    static final int PICK_CONTACT_REQUEST = 1;  // The request code

    private static final int DETAIL_LOADER = 0;

    private static final String[] EVENT_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            EventContract.EventEntry.TABLE_NAME + "." + EventContract.EventEntry._ID,
            EventContract.EventEntry.COLUMN_TITLE,
            EventContract.EventEntry.COLUMN_DATE,
            EventContract.EventEntry.COLUMN_PLACE_NAME
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_TITLE = 1;
    static final int COL_EVENT_DATE = 2;
    static final int COL_EVENT_PLACE = 3;

//    Views
    @BindView(R.id.event_title) EditText mTitle;
    @BindView(R.id.event_date) EditText mDate;
    @BindView(R.id.event_time) EditText mTime;
    @BindView(R.id.event_address) EditText mAddress;
    @BindView(R.id.new_thing) EditText mNewThing;
    @BindView(R.id.guest_list) RecyclerView mGuestList;
    @BindView(R.id.things_list) RecyclerView mThingsList;

    private Uri mUri;

    private Event mEvent;
    private GuestsAdapter mGuestAdapter;
    private ThingsAdapter mThingsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mEvent = new Event();
        Bundle arguments = getArguments();
        if(arguments != null && arguments.getParcelable(EVENT_URI) != null) {
            mUri = arguments.getParcelable(EVENT_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, rootView);

        mDate.setText(Utility.formatShortDate(mEvent.getDate()));
        mTime.setText(Utility.formatTime(mEvent.getDate()));

        mGuestList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGuestAdapter = new GuestsAdapter(getActivity(), mEvent.getGuestList(), null);
        mGuestList.setAdapter(mGuestAdapter);

        mThingsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mThingsAdapter = new ThingsAdapter(getActivity(), mEvent.getThingList(), null);
        mThingsList.setAdapter(mThingsAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        /*
         * Initializes the CursorLoader. The URL_LOADER value is eventually passed
         * to onCreateLoader().
         */
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstance);
    }

    public void save() {
        String eventTitle = mTitle.getText().toString().isEmpty()?getString(R.string.no_title):mTitle.getText().toString();
        mEvent.setTitle(eventTitle);
        mEvent.setPlaceName(mAddress.getText().toString());

        ContentValues eventValues = new ContentValues();
        eventValues.put(EventContract.EventEntry.COLUMN_TITLE, mEvent.getTitle());
        eventValues.put(EventContract.EventEntry.COLUMN_PLACE_NAME, mEvent.getPlaceName());
        eventValues.put(EventContract.EventEntry.COLUMN_DATE, mEvent.getDate());

        Long id = EventContract.getIdFromUri(mUri);
        if(id == 0) {
            Uri insertedUri = getContext().getContentResolver().insert(EventContract.EventEntry.CONTENT_URI, eventValues);
            id = Long.parseLong(insertedUri.getLastPathSegment());
        } else {
            getContext().getContentResolver().update(mUri, eventValues, null, null);
        }

        mEvent.setKey(id.toString());
        Log.v(LOG_TAG, "Event id: " + id.toString());
    }

    @OnClick(R.id.add_guests_button)
    public void addGuests() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @OnClick(R.id.add_things_button)
    public void addThings() {
        String thing = mNewThing.getText().toString();
        updateThingsList(thing);
    }

    @OnFocusChange(R.id.event_date)
    public void showDatePicker(boolean focused) {
        if(focused) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mEvent.getDate());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);

            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int new_year, int new_month, int new_day) {
                    long timestamp = Utility.createDate(mEvent.getDate(), new_year, new_month, new_day);
                    mDate.setText(Utility.formatShortDate(timestamp));
                    mEvent.setDate(timestamp);
                }
            }, year, month, day);

            datePicker.show();
        }
    }

    @OnFocusChange(R.id.event_time)
    public void showTimePicker(boolean focused) {
        if(focused) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int new_hour, int new_minutes) {
                    long timestamp = Utility.createTime(mEvent.getDate(), new_hour, new_minutes);
                    mTime.setText(Utility.formatTime(timestamp));
                    mEvent.setDate(timestamp);
                }
            }, hour, minutes, false);
            timePicker.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                String fullNameIdx = ContactsContract.Contacts.DISPLAY_NAME;
                String emailIdx = ContactsContract.CommonDataKinds.Email.ADDRESS;

                // The user picked a contact.
                Uri contactUri = data.getData();
                String[] fields = { emailIdx, fullNameIdx};

                Cursor cursor = getContext().getContentResolver().query(contactUri, fields, null, null, null);
                if(cursor != null) {
                    if (cursor.moveToFirst()) {
                        int fullnameColumn = cursor.getColumnIndex(fullNameIdx);
                        String fullName = cursor.getString(fullnameColumn);
                        int emailColumn = cursor.getColumnIndex(emailIdx);
                        String email = cursor.getString(emailColumn);

                        Guest guest = new Guest(fullName, email);
                        updateGuestList(guest);
                    }
                    cursor.close();
                }
            }
        }
    }

    private void updateGuestList(Guest guest) {
        if (guest != null) {
            mEvent.addGuest(guest);
        }
        mGuestAdapter.updateList(mEvent.getGuestList());
    }

    private void updateThingsList(String thing) {
        if (thing != null) {
            mEvent.addThing(thing);
        }
        mThingsAdapter.updateList(mEvent.getThingList());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.

            return new CursorLoader(getActivity(),
                    mUri,
                    EVENT_COLUMNS,
                    null,
                    null,
                    null);
        }

        ViewParent vp = getView().getParent();
        if ( vp instanceof CardView) {
            ((View)vp).setVisibility(View.INVISIBLE);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) { return; }
        mTitle.setText(data.getString(COL_EVENT_TITLE));
        long date = data.getLong(COL_EVENT_DATE);
        mDate.setText(Utility.formatShortDate(date));
        mTime.setText(Utility.formatTime(date));
        mAddress.setText(data.getString(COL_EVENT_PLACE));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
