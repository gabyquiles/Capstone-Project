package com.gabyquiles.eventy.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.gabyquiles.eventy.BuildConfig;
import com.gabyquiles.eventy.R;
import com.gabyquiles.eventy.Utility;
import com.gabyquiles.eventy.firebase.FirebaseWriter;
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
public class EventDetailsActivityFragment extends Fragment implements ValueEventListener{
    private final String LOG_TAG = EventDetailsActivityFragment.class.getSimpleName();

    static final String EVENT_URI = "event_uri";
    static final int PICK_CONTACT_REQUEST = 1;  // The request code

//    Views
    @BindView(R.id.event_title) TextView mTitle;
    @BindView(R.id.event_date) TextView mDate;
    @BindView(R.id.event_time) TextView mTime;
    @BindView(R.id.event_address) TextView mPlace;

    FirebaseWriter mDBManager;
    Event mEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mEvent = new Event();
        Bundle arguments = getArguments();
        if(arguments != null && arguments.getParcelable(EVENT_URI) != null) {
            String firebase_url = arguments.getParcelable(EVENT_URI).toString();
            mDBManager = new FirebaseWriter(firebase_url);
            mDBManager.addValueEventListener(this);
        } else {
            mDBManager = new FirebaseWriter(null);
        }


        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, rootView);

        mDate.setText(Utility.formatShortDate(mEvent.getDate()));
        mTime.setText(Utility.formatTime(mEvent.getDate()));

        return rootView;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mEvent = dataSnapshot.getValue(Event.class);
        mEvent.setKey(dataSnapshot.getKey());

        mTitle.setText(mEvent.getTitle());
        mDate.setText(Utility.formatShortDate(mEvent.getDate()));
        mTime.setText(Utility.formatTime(mEvent.getDate()));
        mPlace.setText(mEvent.getPlace());
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        if(BuildConfig.DEBUG) {
            Log.v(LOG_TAG, "Floor update canceled: " + firebaseError.getMessage());
        }
    }

    @OnClick(R.id.save_button)
    public void save() {
        mEvent.setTitle(mTitle.getText().toString());
        mEvent.setPlace(mPlace.getText().toString());

        mEvent = mDBManager.save(mEvent);
        mDBManager.addValueEventListener(this);
    }

    @OnClick(R.id.add_guests_button)
    public void addGuests() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
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
            int i =1;
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                String fullNameIdx = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME;
                String emailIdx = ContactsContract.CommonDataKinds.Email.ADDRESS;

                // The user picked a contact.
                Uri contactUri = data.getData();
                String[] fields = { emailIdx, fullNameIdx };

                Cursor cursor = getContext().getContentResolver().query(contactUri, fields, null, null, null);
                if(cursor.moveToFirst()) {
                    int fullnameColumn = cursor.getColumnIndex(fullNameIdx);
                    String fullName = cursor.getString(fullnameColumn);
                    int emailColumn = cursor.getColumnIndex(emailIdx);
                    String email = cursor.getString(emailColumn);

                    Guest guest = new Guest(fullName, email);
                    mEvent.addGuest(guest);
                }
            }
        }
    }
}
